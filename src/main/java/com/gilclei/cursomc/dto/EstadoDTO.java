package com.gilclei.cursomc.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.gilclei.cursomc.domain.Categoria;
import com.gilclei.cursomc.domain.Estado;

public class EstadoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min=5, max=50, message="O tamanho deve ser entre 5 e 50 caracteres")
	private String nome;
	
	@Column(unique = true, length = 2, nullable = false)
	private String sigla;

	public EstadoDTO() {
	}
	
	public EstadoDTO(Estado obj) {
		id = obj.getId();
		nome = obj.getNome();
	}

	public EstadoDTO(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
