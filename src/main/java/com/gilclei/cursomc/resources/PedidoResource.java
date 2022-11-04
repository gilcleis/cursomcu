package com.gilclei.cursomc.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gilclei.cursomc.domain.Pedido;
import com.gilclei.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	
	@GetMapping
	public ResponseEntity<List<Pedido>> findAll() {
		List<Pedido> pedidos = service.findAll();
		return ResponseEntity.ok().body(pedidos);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable Integer id) {
		Pedido pedido = service.findById(id);
		return ResponseEntity.ok().body(pedido);
	}
	
	@PostMapping
	public ResponseEntity<Pedido> insert(@RequestBody Pedido obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Pedido> update(@PathVariable Integer id,@RequestBody Pedido obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}

}
