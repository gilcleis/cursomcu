package com.gilclei.cursomc.domain.pk;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gilclei.cursomc.domain.Pedido;
import com.gilclei.cursomc.domain.Produto;

@Embeddable
public class ItemPedidoPk implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;

	@JsonIgnore
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	@JsonIgnore
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pedido, produto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedidoPk other = (ItemPedidoPk) obj;
		return Objects.equals(pedido, other.pedido) && Objects.equals(produto, other.produto);
	}
	
	

}
