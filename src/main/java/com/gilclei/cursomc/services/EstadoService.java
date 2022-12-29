package com.gilclei.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gilclei.cursomc.domain.Estado;
import com.gilclei.cursomc.repositories.EstadoRepository;
import com.gilclei.cursomc.services.exeptions.DatabaseException;
import com.gilclei.cursomc.services.exeptions.IntegrityConstraintViolationException;
import com.gilclei.cursomc.services.exeptions.ResourceNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repository;

	@Transactional
	public Estado insert(Estado obj) {
		try {
			checkUnique(obj);
			return repository.save(obj);
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityConstraintViolationException(e.getCause().getLocalizedMessage());
		}
	}

	private void checkUnique(Estado obj) {
		if (obj.getId() == null) {
			if (repository.findByNomeIgnoreCase(obj.getNome()).size() > 0) {
				throw new IntegrityConstraintViolationException("Nome já cadastrado");
			}
			if (repository.findBySiglaIgnoreCase(obj.getSigla()).size() > 0) {
				throw new IntegrityConstraintViolationException("Sigla já cadastrada");
			}
		} else {
			if (repository.findByNotIdAndNome(obj.getId(), obj.getNome()).size() > 0) {
				throw new IntegrityConstraintViolationException("Nome já cadastrado");
			}
			if (repository.findByNotIdAndSigla(obj.getId(), obj.getSigla()).size() > 0) {
				throw new IntegrityConstraintViolationException("Sigla já cadastrado");
			}
		}
	}

	@Transactional
	public Estado findById(Integer id) {
		Optional<Estado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public List<Estado> findAll() {
		return repository.findAllByOrderByNome();
	}

	public List<Estado> findByname(String nome) {
		return repository.findByNomeIgnoreCase(nome);
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

	public Estado update(Integer id, Estado obj) {
		try {
			Estado entity = findById(id);
			updateData(entity, obj);
			checkUnique(entity);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Estado entity, Estado obj) {
		entity.setNome(obj.getNome());
	}

	

}
