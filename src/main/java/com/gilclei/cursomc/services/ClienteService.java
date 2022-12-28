package com.gilclei.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gilclei.cursomc.domain.Cidade;
import com.gilclei.cursomc.domain.Cliente;
import com.gilclei.cursomc.domain.Endereco;
import com.gilclei.cursomc.domain.enums.Perfil;
import com.gilclei.cursomc.domain.enums.TipoCliente;
import com.gilclei.cursomc.dto.ClienteDTO;
import com.gilclei.cursomc.dto.ClienteNewDTO;
import com.gilclei.cursomc.repositories.ClienteRepository;
import com.gilclei.cursomc.repositories.EnderecoRepository;
import com.gilclei.cursomc.security.UserSS;
import com.gilclei.cursomc.services.exeptions.AuthorizationException;
import com.gilclei.cursomc.services.exeptions.DatabaseException;
import com.gilclei.cursomc.services.exeptions.IntegrityConstraintViolationException;
import com.gilclei.cursomc.services.exeptions.ResourceNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private S3Service s3Service;

	@Transactional
	public Cliente insert(Cliente obj) {
		try {
			obj.setId(null);
			obj = repository.save(obj);
			enderecoRepository.saveAll(obj.getEnderecos());
			return obj;
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityConstraintViolationException(e.getCause().getLocalizedMessage());
		}
	}

	public Cliente findById(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public void delete(Integer id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Cliente update(Integer id, Cliente obj) {
		try {
			Cliente entity = findById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Cliente entity, Cliente obj) {
		entity.setNome(obj.getNome());
		entity.setEmail(obj.getEmail());
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), null,TipoCliente.toEnum(objDto.getTipo()),passwordEncoder.encode(objDto.getSenha()));
		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),	objDto.getBairro(), objDto.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3()!=null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		return cliente;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user==null ) {
			throw new AuthorizationException("Acesso negado");
		}
		
		URI uri = s3Service.uploadFile(multipartFile);
		
		Cliente cliente = repository.findById(user.getId()).get();
		cliente.setImageUrl(uri.toString());
		repository.save(cliente);
		
		return uri;
	}

}
