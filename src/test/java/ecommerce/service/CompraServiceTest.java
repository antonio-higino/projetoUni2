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
		"0, 0, 10, 0, 0, SUDESTE, BRONZE, 255.00",
		"2, 0, 10, 0, 0, SUDESTE, BRONZE, 391.00",
		"10, 0, 2, 0, 0, SUL, PRATA, 540.65",
		"0, 0, 7, 0, 50, NORDESTE, OURO, 831.20",
		"0, 0, 40, 0, 0, CENTRO_OESTE, PRATA, 871.20",
		"0, 0, 11, 0, 0, NORTE, BRONZE, 310.40",
		"1, 0, 8, 0, 0, SUDESTE, BRONZE, 259.00",
		"10, 0, 0, 0, 0, SUDESTE, BRONZE, 507.00",
		"0, 0, 0, 0, 10, SUDESTE, BRONZE, 1582.00",
		"2, 0, 1, 0, 0, SUDESTE, BRONZE, 140.00",
		"0, 0, 0, 0, 3, SUDESTE, BRONZE, 489.00",
		"10, 0, 0, 0, 0, NORDESTE, PRATA, 470.10",
		"10, 0, 1, 0, 0, SUDESTE, BRONZE, 559.00",
		"20, 0, 0, 0, 0, SUDESTE, BRONZE, 957.00",
		"21, 0, 0, 0, 0, SUDESTE, BRONZE, 1004.25",
		"1, 0, 1, 0, 0, SUDESTE, BRONZE, 85.00",
		"1, 0, 2, 0, 0, SUDESTE, BRONZE, 115.00",
		"2, 0, 2, 0, 0, SUDESTE, BRONZE, 170.00",
		"1, 0, 4, 0, 0, SUDESTE, BRONZE, 169.00",
		"1, 0, 6, 0, 0, SUDESTE, BRONZE, 217.00",
		"2, 0, 6, 0, 0, SUDESTE, BRONZE, 272.00",
		"0, 0, 0, 0, 10, SUDESTE, BRONZE, 1582.00",
		"0, 0, 0, 0, 10, SUDESTE, PRATA, 876.00",
		"0, 0, 0, 0, 10, SUDESTE, OURO, 170.00",
		"0, 0, 4, 0, 0, SUDESTE, BRONZE, 114.00",
		"0, 0, 0, 0, 10, SUL, BRONZE, 1652.60",
		"0, 0, 0, 0, 10, NORDESTE, BRONZE, 1723.20",
		"0, 0, 0, 0, 10, CENTRO_OESTE, BRONZE, 1864.40",
		"0, 0, 0, 0, 10, NORTE, BRONZE, 2005.60",
		"0, 0, 7, 0, 0, SUDESTE, BRONZE, 189.00",
		"1, 0, 0, 0, 0, SUDESTE, BRONZE, 55.00",
		"1, 1, 0, 0, 0, SUDESTE, BRONZE, 80.00",
		"1, 1, 0, 1, 0, SUDESTE, BRONZE, 105.00",
		"5, 0, 0, 0, 0, SUDESTE, BRONZE, 250.00",
		"1, 0, 0, 0, 0, SUDESTE, BRONZE, 55.00",
		"2, 0, 0, 0, 0, SUDESTE, BRONZE, 110.00",
		"3, 0, 0, 0, 0, SUDESTE, BRONZE, 157.50",
		"4, 0, 0, 0, 0, SUDESTE, BRONZE, 210.00",
		"5, 0, 0, 0, 0, SUDESTE, BRONZE, 250.00",
		"6, 0, 0, 0, 0, SUDESTE, BRONZE, 324.00",
		"7, 0, 0, 0, 0, SUDESTE, BRONZE, 376.00",
		"8, 0, 0, 0, 0, SUDESTE, BRONZE, 408.00",
		"10, 0, 0, 0, 0, SUDESTE, BRONZE, 507.00",
		"0, 0, 10, 0, 40, SUDESTE, OURO, 841.50",
		"10, 5, 0, 0, 0, NORTE, PRATA, 549.95",
		"1, 1, 1, 1, 1, SUDESTE, BRONZE, 255.40",
		"5, 5, 5, 0, 0, SUL, BRONZE, 534.00",
		"20, 0, 10, 0, 20, NORTE, BRONZE, 5169.10",
		"0, 10, 0, 0, 0, SUDESTE, BRONZE, 220.00",
		"3, 2, 3, 2, 1, CENTRO_OESTE, PRATA, 417.88",
		"0, 1, 0, 0, 0, SUDESTE, BRONZE, 25.00",
		"0, 0, 1, 0, 0, SUDESTE, BRONZE, 30.00",
		"10, 0, 0, 0, 0, SUDESTE, OURO, 425.00",
		"0, 4, 0, 0, 0, SUDESTE, BRONZE, 96.00",
		"50, 0, 0, 0, 0, SUDESTE, BRONZE, 2162.00",
		"0, 0, 0, 0, 10, SUDESTE, PRATA, 876.00",
		"0, 0, 0, 0, 20, NORTE, BRONZE, 3995.60",
		"0, 0, 5, 0, 0, SUDESTE, BRONZE, 135.00",
		"0, 0, 0, 0, 30, SUDESTE, BRONZE, 4671.00",
		"0, 0, 5, 5, 5, SUDESTE, BRONZE, 1080.00",
		"0, 0, 0, 4, 0, NORDESTE, PRATA, 87.00",
		"0, 0, 0, 7, 0, NORTE, PRATA, 148.75",
		"0, 0, 0, 8, 0, SUL, OURO, 136.00"
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

		/*// Ao trabalhar com BigDecimal, evite comparar com equals() -- método que o
		// assertEquals usa,
		// pois ela leva em conta escala (ex: 10.0 != 10.00).
		// Use o método compareTo().
		BigDecimal esperado = new BigDecimal("0.00");
		assertEquals(0, custoTotal.compareTo(esperado), "Valor calculado incorreto: " + custoTotal);

		// Uma alternativa mais elegante, é usar a lib AssertJ
		// O método isEqualByComparingTo não leva em conta escala
		// e não precisa instanciar um BigDecimal para fazer a comparação
		assertThat(custoTotal).as("Custo Total da Compra").isEqualByComparingTo(custoEsperado);*/
	}
}
