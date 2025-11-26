package ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;

public class CompraServiceTest {

	@ParameterizedTest
	@CsvSource({
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"0, 0, 3, 0, 0, SUDESTE, BRONZE, 90.00",
		"0, 0, 5, 0, 0, SUDESTE, BRONZE, 150.00",
		"0, 0, 8, 0, 0, SUDESTE, BRONZE, 240.00",
		"0, 1, 0, 0, 0, SUDESTE, BRONZE, 25.00",
		"0, 0, 0, 0, 1, SUDESTE, BRONZE, 20.00",
		"0, 0, 0, 0, 2, SUDESTE, BRONZE, 40.00",
		"0, 0, 1, 1, 0, SUDESTE, BRONZE, 55.00",
		"0, 0, 1, 2, 0, SUDESTE, BRONZE, 80.00",
		"1, 0, 1, 1, 0, SUDESTE, BRONZE, 110.00"
	})
	public void deveCalcularCustoTotalCorretamente(
			Long quantidadeEletronico, 
			Long quantidadeRoupa, 
			Long quantidadeAlimento, 
			Long quantidadeLivro, 
			Long quantidadeMovel, 
			Regiao regiao, 
			TipoCliente tipoCliente, 
			BigDecimal custoEsperado) {
		
		// Arrange
		CompraService service = new CompraService(null, null, null, null);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();

		List<ItemCompra> itens = new ArrayList<>();

		// Criar produtos de teste
		Produto produto1 = new Produto();
		produto1.setNome("Eletronico");
		produto1.setPreco(new BigDecimal("50"));
		produto1.setPesoFisico(new BigDecimal("1"));
		produto1.setComprimento(new BigDecimal("10"));
		produto1.setLargura(new BigDecimal("10"));
		produto1.setAltura(new BigDecimal("10"));
		produto1.setFragil(true);
		produto1.setTipo(TipoProduto.ELETRONICO);
		
		Produto produto2 = new Produto();
		produto2.setNome("Roupa");
		produto2.setPreco(new BigDecimal("20.00"));
		produto2.setPesoFisico(new BigDecimal("0.3"));
		produto2.setComprimento(new BigDecimal("8"));
		produto2.setLargura(new BigDecimal("8"));
		produto2.setAltura(new BigDecimal("8"));
		produto2.setFragil(true);
		produto2.setTipo(TipoProduto.ROUPA);

		Produto produto3 = new Produto();
		produto3.setNome("Alimento");
		produto3.setPreco(new BigDecimal("30.00"));
		produto3.setPesoFisico(new BigDecimal("0.5"));
		produto3.setComprimento(new BigDecimal("5"));
		produto3.setLargura(new BigDecimal("5"));
		produto3.setAltura(new BigDecimal("5"));
		produto3.setFragil(false);
		produto3.setTipo(TipoProduto.ALIMENTO);

		Produto produto4 = new Produto();
		produto4.setNome("Livro");
		produto4.setPreco(new BigDecimal("20.00"));
		produto4.setPesoFisico(new BigDecimal("0.3"));
		produto4.setComprimento(new BigDecimal("8"));
		produto4.setLargura(new BigDecimal("8"));
		produto4.setAltura(new BigDecimal("8"));
		produto4.setFragil(true);
		produto4.setTipo(TipoProduto.LIVRO);
		
		Produto produto5 = new Produto();
		produto5.setNome("Movel");
		produto5.setPreco(new BigDecimal("20.00"));
		produto5.setPesoFisico(new BigDecimal("1.0"));
		produto5.setComprimento(new BigDecimal("20"));
		produto5.setLargura(new BigDecimal("60"));
		produto5.setAltura(new BigDecimal("100"));
		produto5.setFragil(false);
		produto5.setTipo(TipoProduto.MOVEL);

		// Criar itens de compra
		if (quantidadeEletronico > 0) {
			ItemCompra item1 = new ItemCompra();
			item1.setProduto(produto1);
			item1.setQuantidade(quantidadeEletronico);
			itens.add(item1);
		}
		
		if (quantidadeRoupa > 0) {
			ItemCompra item2 = new ItemCompra();
			item2.setProduto(produto2);
			item2.setQuantidade(quantidadeRoupa);
			itens.add(item2);
		}
		
		if (quantidadeAlimento > 0) {
			ItemCompra item3 = new ItemCompra();
			item3.setProduto(produto3);
			item3.setQuantidade(quantidadeAlimento);
			itens.add(item3);
		}
		
		if (quantidadeLivro > 0) {
			ItemCompra item4 = new ItemCompra();
			item4.setProduto(produto4);
			item4.setQuantidade(quantidadeLivro);
			itens.add(item4);
		}
		
		if (quantidadeMovel > 0) {
			ItemCompra item5 = new ItemCompra();
			item5.setProduto(produto5);
			item5.setQuantidade(quantidadeMovel);
			itens.add(item5);
		}
		
		// Inserir no carrinho
		carrinho.setItens(itens);
		
		// Act
		BigDecimal custoTotal = service.calcularCustoTotal(carrinho, regiao, tipoCliente);
		
		// Assert
		assertThat(custoTotal).as("Custo Total da Compra").isEqualByComparingTo(custoEsperado);
	}
}
