package ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
			"SUDESTE, BRONZE",
			"SUL, PRATA",
			"NORDESTE, OURO",
			"CENTRO_OESTE, BRONZE",
			"NORTE, PRATA"
	})
	public void deveCalcularCustoTotalCorretamente(Regiao regiao, TipoCliente tipoCliente, BigDecimal custoEsperado) {
		// Arrange
		CompraService service = new CompraService(null, null, null, null);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();

		List<ItemCompra> itens = new ArrayList<>();

		// Criar produtos de teste
		Produto produto1 = new Produto();
		produto1.setNome("Produto 1");
		produto1.setPreco(new BigDecimal("50.00"));
		produto1.setPesoFisico(new BigDecimal("1.0"));
		produto1.setComprimento(new BigDecimal("10"));
		produto1.setLargura(new BigDecimal("10"));
		produto1.setAltura(new BigDecimal("10"));
		produto1.setFragil(false);
		produto1.setTipo(TipoProduto.ELETRONICO);

		Produto produto2 = new Produto();
		produto2.setNome("Produto 2");
		produto2.setPreco(new BigDecimal("30.00"));
		produto2.setPesoFisico(new BigDecimal("0.5"));
		produto2.setComprimento(new BigDecimal("5"));
		produto2.setLargura(new BigDecimal("5"));
		produto2.setAltura(new BigDecimal("5"));
		produto2.setFragil(false);
		produto2.setTipo(TipoProduto.LIVRO);

		Produto produto3 = new Produto();
		produto3.setNome("Produto 3");
		produto3.setPreco(new BigDecimal("20.00"));
		produto3.setPesoFisico(new BigDecimal("0.3"));
		produto3.setComprimento(new BigDecimal("8"));
		produto3.setLargura(new BigDecimal("8"));
		produto3.setAltura(new BigDecimal("8"));
		produto3.setFragil(true);
		produto3.setTipo(TipoProduto.ROUPA);

		// Criar itens de compra
		ItemCompra item1 = new ItemCompra();
		item1.setProduto(produto1);
		item1.setQuantidade(1L);

		ItemCompra item2 = new ItemCompra();
		item2.setProduto(produto2);
		item2.setQuantidade(1L);

		ItemCompra item3 = new ItemCompra();
		item3.setProduto(produto3);
		item3.setQuantidade(1L);

		itens.add(item1);
		itens.add(item2);
		itens.add(item3);
		carrinho.setItens(itens);

		BigDecimal custoTotal = service.calcularCustoTotal(carrinho, regiao, tipoCliente);

		// Ao trabalhar com BigDecimal, evite comparar com equals() -- método que o
		// assertEquals usa,
		// pois ela leva em conta escala (ex: 10.0 != 10.00).
		// Use o método compareTo().
		BigDecimal esperado = new BigDecimal("0.00");
		assertEquals(0, custoTotal.compareTo(esperado), "Valor calculado incorreto: " + custoTotal);

		// Uma alternativa mais elegante, é usar a lib AssertJ
		// O método isEqualByComparingTo não leva em conta escala
		// e não precisa instanciar um BigDecimal para fazer a comparação
		assertThat(custoTotal).as("Custo Total da Compra").isEqualByComparingTo(custoEsperado);
	}
}
