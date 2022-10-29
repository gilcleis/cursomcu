package com.gilclei.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilclei.cursomc.domain.Categoria;
import com.gilclei.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public Categoria insert(Categoria obj) {
		return repository.save(obj);
	}

	public Categoria findById(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		return obj.get();
	}

	public List<Categoria> findAll() {
		return repository.findAll();
	}

	public void delete(Integer id) {
		repository.deleteById(id);
	}

	public Categoria update(Integer id, Categoria obj) {
		Categoria entity = findById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Categoria entity, Categoria obj) {
		entity.setNome(obj.getNome());

	}

}
