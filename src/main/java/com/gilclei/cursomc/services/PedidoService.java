package com.gilclei.cursomc.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gilclei.cursomc.domain.ItemPedido;
import com.gilclei.cursomc.domain.PagamentoComBoleto;
import com.gilclei.cursomc.domain.Pedido;
import com.gilclei.cursomc.domain.enums.EstadoPagamento;
import com.gilclei.cursomc.repositories.ItemPedidoRepository;
import com.gilclei.cursomc.repositories.PagamentoRepository;
import com.gilclei.cursomc.repositories.PedidoRepository;
import com.gilclei.cursomc.services.exeptions.DatabaseException;
import com.gilclei.cursomc.services.exeptions.IntegrityConstraintViolationException;
import com.gilclei.cursomc.services.exeptions.ResourceNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository; 
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional
	public Pedido insert(Pedido obj) {
		try {
			obj.setId(null);
			obj.setInstante(new Date());
			obj.setCliente(clienteService.findById(obj.getCliente().getId()));
			obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
			obj.getPagamento().setPedido(obj);
			if(obj.getPagamento() instanceof PagamentoComBoleto) {
				PagamentoComBoleto pgto = (PagamentoComBoleto) obj.getPagamento();
				boletoService.preencherPagamentoComBoleto(pgto,obj.getInstante());
			}
			repository.save(obj);
			pagamentoRepository.save(obj.getPagamento());
			
			for(ItemPedido ip : obj.getItens()) {
				ip.setDesconto(0.0);
				ip.setProduto(produtoService.findById(ip.getProduto().getId()));
				ip.setPreco(ip.getProduto().getPreco());
				ip.setPedido(obj);
			}
			itemPedidoRepository.saveAll(obj.getItens());
			emailService.sendOrderConfirmationEmail(obj);
			return obj;
		} catch (DataIntegrityViolationException e) {
			throw new IntegrityConstraintViolationException(e.getCause().getLocalizedMessage());
		}
	}

	public Pedido findById(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public List<Pedido> findAll() {
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

	public Pedido update(Integer id, Pedido obj) {
		try {
			Pedido entity = findById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Pedido entity, Pedido obj) {
		entity.setInstante(obj.getInstante());

	}

}
