package com.gilclei.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gilclei.cursomc.domain.Pedido;
import com.gilclei.cursomc.repositories.PedidoRepository;
import com.gilclei.cursomc.services.exeptions.DatabaseException;
import com.gilclei.cursomc.services.exeptions.IntegrityConstraintViolationException;
import com.gilclei.cursomc.services.exeptions.ResourceNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;

	public Pedido insert(Pedido obj) {
		try {

			return repository.save(obj);
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
