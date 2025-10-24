package ecommerce.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produto
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	/** Preço unitário em reais (R$). */
	private BigDecimal preco;

	/** Peso físico em quilogramas (kg). */
	private BigDecimal pesoFisico;

	/** Dimensões em centímetros (cm). */
	private BigDecimal comprimento;
	private BigDecimal largura;
	private BigDecimal altura;

	/** Indica se o produto é frágil. */
	private Boolean fragil;

	@Enumerated(EnumType.STRING)
	private TipoProduto tipo;

	public Produto()
	{
	}

	public Produto(Long id, String nome, String descricao, BigDecimal preco, BigDecimal pesoFisico,
			BigDecimal comprimento, BigDecimal largura, BigDecimal altura, Boolean fragil, TipoProduto tipo)
	{
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.pesoFisico = pesoFisico;
		this.comprimento = comprimento;
		this.largura = largura;
		this.altura = altura;
		this.fragil = fragil;
		this.tipo = tipo;
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

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public BigDecimal getPreco()
	{
		return preco;
	}

	public void setPreco(BigDecimal preco) throws IllegalArgumentException
	{
		if(preco.compareTo(BigDecimal.ZERO) != -1) {
			this.preco = preco;
		} else {
			throw new IllegalArgumentException("Preço inválido");
		}
	}

	public BigDecimal getPesoFisico()
	{
		return pesoFisico;
	}

	public void setPesoFisico(BigDecimal pesoFisico) throws IllegalArgumentException
	{
		if(pesoFisico.compareTo(BigDecimal.ZERO) == 1) {
			this.pesoFisico = pesoFisico;
		} else {
			throw new IllegalArgumentException("Peso físico inválido");
		}
	}

	public BigDecimal getComprimento()
	{
		return comprimento;
	}

	public void setComprimento(BigDecimal comprimento) throws IllegalArgumentException
	{
		if(comprimento.compareTo(BigDecimal.ZERO) == 1) {
			this.comprimento = comprimento;
		} else {
			throw new IllegalArgumentException("Comprimento inválido");
		}
	}

	public BigDecimal getLargura()
	{
		return largura;
	}

	public void setLargura(BigDecimal largura) throws IllegalArgumentException
	{
		if(largura.compareTo(BigDecimal.ZERO) == 1) {
			this.largura = largura;
		} else {
			throw new IllegalArgumentException("Largura inválida");
		}
	}

	public BigDecimal getAltura()
	{
		return altura;
	}

	public void setAltura(BigDecimal altura) throws IllegalArgumentException
	{
		if(altura.compareTo(BigDecimal.ZERO) == 1) {
			this.altura = altura;
		} else {
			throw new IllegalArgumentException("Altura inválida");
		}
	}

	public Boolean isFragil()
	{
		return fragil;
	}

	public void setFragil(Boolean fragil)
	{
		this.fragil = fragil;
	}

	public TipoProduto getTipo()
	{
		return tipo;
	}

	public void setTipo(TipoProduto tipo)
	{
		this.tipo = tipo;
	}
}