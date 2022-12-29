package com.gilclei.cursomc.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gilclei.cursomc.domain.Cidade;
import com.gilclei.cursomc.domain.Estado;
import com.gilclei.cursomc.dto.CidadeDTO;
import com.gilclei.cursomc.dto.EstadoDTO;
import com.gilclei.cursomc.services.CidadeService;
import com.gilclei.cursomc.services.EstadoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Estados")
@CrossOrigin(origins = "*")
public class EstadoResource {
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;

	
	
	@GetMapping("/estados")
	@ApiOperation(value = "Retorna Lista de Estados")
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> estados = service.findAll();
		List<EstadoDTO>  listDto = estados.stream().map(x-> new EstadoDTO(x)).toList();
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping(value = "/estados/{id}")
	public ResponseEntity<Estado> findById(@PathVariable Integer id) {
		Estado estado = service.findById(id);
		return ResponseEntity.ok().body(estado);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/estados")
	public ResponseEntity<Estado> insert(@RequestBody Estado obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/estados/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/estados/{id}")
	public ResponseEntity<Estado> update(@PathVariable Integer id,@RequestBody Estado obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value = "/estados/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> list = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).toList();
		return ResponseEntity.ok().body(listDto);
	}

}
