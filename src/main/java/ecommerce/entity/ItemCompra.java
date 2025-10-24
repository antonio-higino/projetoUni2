package ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemCompra
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne // Vários itens podem se referir ao mesmo produto
	@JoinColumn(name = "produto_id")
	private Produto produto;

	private Long quantidade;

	public ItemCompra()
	{
	}

	public ItemCompra(Long id, Produto produto, Long quantidade)
	{
		this.id = id;
		this.produto = produto;
		this.quantidade = quantidade;
	}

	// Getters e Setters
	public Long getId()
	{
		return id;
	}

	public void setId(Long id) throws IllegalArgumentException
	{
		if(id >= 1) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("Id inválido");
		}
	}

	public Produto getProduto()
	{
		return produto;
	}

	public void setProduto(Produto produto)
	{
		this.produto = produto;
	}

	public Long getQuantidade()
	{
		return quantidade;
	}

	public void setQuantidade(Long quantidade) throws IllegalArgumentException
	{
		if(quantidade >= 1) {
			this.quantidade = quantidade;
		} else {
			throw new IllegalArgumentException("Quantidade inválida");
		}
	}
}
