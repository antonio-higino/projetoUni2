package ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.dto.CompraDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.external.fake.EstoqueSimulado;
import ecommerce.external.fake.PagamentoSimulado;

@ExtendWith(MockitoExtension.class)
public class CompraServiceFinalizarCompraCenario1Test {

	@Mock
	private CarrinhoDeComprasService carrinhoService;

	@Mock
	private ClienteService clienteService;

	private EstoqueSimulado estoqueFake;
	private PagamentoSimulado pagamentoFake;
	private CompraService service;

	@BeforeEach
	void setUp() {
		estoqueFake = new EstoqueSimulado();
		pagamentoFake = new PagamentoSimulado();
		service = new CompraService(carrinhoService, clienteService, estoqueFake, pagamentoFake);
	}

	@Test
	void deveFinalizarCompraComSucesso() {
		Long carrinhoId = 1L;
		Long clienteId = 1L;

		Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Produto Teste");
		produto.setPreco(new BigDecimal("100.00"));
		produto.setPesoFisico(new BigDecimal("2.0"));
		produto.setComprimento(new BigDecimal("10"));
		produto.setLargura(new BigDecimal("10"));
		produto.setAltura(new BigDecimal("10"));
		produto.setFragil(false);
		produto.setTipo(TipoProduto.ELETRONICO);

		ItemCompra item = new ItemCompra();
		item.setId(1L);
		item.setProduto(produto);
		item.setQuantidade(2L);

		List<ItemCompra> itens = new ArrayList<>();
		itens.add(item);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
		carrinho.setId(carrinhoId);
		carrinho.setCliente(cliente);
		carrinho.setItens(itens);
		carrinho.setData(LocalDate.now());

		when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
		when(carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente)).thenReturn(carrinho);

		estoqueFake.adicionarProduto(1L, 10L);

		CompraDTO resultado = service.finalizarCompra(carrinhoId, clienteId);

		verify(clienteService).buscarPorId(clienteId);
		verify(carrinhoService).buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		assertThat(resultado.sucesso()).isTrue();
		assertThat(resultado.transacaoPagamentoId()).isNotNull();
		assertThat(resultado.mensagem()).isEqualTo("Compra finalizada com sucesso.");
		assertThat(pagamentoFake.estaAutorizado(resultado.transacaoPagamentoId())).isTrue();
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueIndisponivel() {
		Long carrinhoId = 1L;
		Long clienteId = 1L;

		Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Produto Teste");
		produto.setPreco(new BigDecimal("100.00"));
		produto.setPesoFisico(new BigDecimal("2.0"));
		produto.setComprimento(new BigDecimal("10"));
		produto.setLargura(new BigDecimal("10"));
		produto.setAltura(new BigDecimal("10"));
		produto.setFragil(false);
		produto.setTipo(TipoProduto.ELETRONICO);

		ItemCompra item = new ItemCompra();
		item.setId(1L);
		item.setProduto(produto);
		item.setQuantidade(2L);

		List<ItemCompra> itens = new ArrayList<>();
		itens.add(item);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
		carrinho.setId(carrinhoId);
		carrinho.setCliente(cliente);
		carrinho.setItens(itens);
		carrinho.setData(LocalDate.now());

		when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
		when(carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente)).thenReturn(carrinho);

		estoqueFake.simularIndisponibilidade(true);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			service.finalizarCompra(carrinhoId, clienteId);
		});

		verify(clienteService).buscarPorId(clienteId);
		verify(carrinhoService).buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		assertThat(exception.getMessage()).isEqualTo("Itens fora de estoque.");
	}

	@Test
	void deveLancarExcecaoQuandoPagamentoNaoAutorizado() {
		Long carrinhoId = 1L;
		Long clienteId = 1L;

		Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Produto Teste");
		produto.setPreco(new BigDecimal("100.00"));
		produto.setPesoFisico(new BigDecimal("2.0"));
		produto.setComprimento(new BigDecimal("10"));
		produto.setLargura(new BigDecimal("10"));
		produto.setAltura(new BigDecimal("10"));
		produto.setFragil(false);
		produto.setTipo(TipoProduto.ELETRONICO);

		ItemCompra item = new ItemCompra();
		item.setId(1L);
		item.setProduto(produto);
		item.setQuantidade(2L);

		List<ItemCompra> itens = new ArrayList<>();
		itens.add(item);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
		carrinho.setId(carrinhoId);
		carrinho.setCliente(cliente);
		carrinho.setItens(itens);
		carrinho.setData(LocalDate.now());

		when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
		when(carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente)).thenReturn(carrinho);

		estoqueFake.adicionarProduto(1L, 10L);
		pagamentoFake.simularPagamentoNaoAutorizado(true);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			service.finalizarCompra(carrinhoId, clienteId);
		});

		verify(clienteService).buscarPorId(clienteId);
		verify(carrinhoService).buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		assertThat(exception.getMessage()).isEqualTo("Pagamento não autorizado.");
	}

	@Test
	void deveCancelarPagamentoQuandoErroBaixaEstoque() {
		Long carrinhoId = 1L;
		Long clienteId = 1L;

		Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Produto Teste");
		produto.setPreco(new BigDecimal("100.00"));
		produto.setPesoFisico(new BigDecimal("2.0"));
		produto.setComprimento(new BigDecimal("10"));
		produto.setLargura(new BigDecimal("10"));
		produto.setAltura(new BigDecimal("10"));
		produto.setFragil(false);
		produto.setTipo(TipoProduto.ELETRONICO);

		ItemCompra item = new ItemCompra();
		item.setId(1L);
		item.setProduto(produto);
		item.setQuantidade(2L);

		List<ItemCompra> itens = new ArrayList<>();
		itens.add(item);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
		carrinho.setId(carrinhoId);
		carrinho.setCliente(cliente);
		carrinho.setItens(itens);
		carrinho.setData(LocalDate.now());

		when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
		when(carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente)).thenReturn(carrinho);

		estoqueFake.adicionarProduto(1L, 10L);
		estoqueFake.simularErroNaBaixa(true);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			service.finalizarCompra(carrinhoId, clienteId);
		});

		verify(clienteService).buscarPorId(clienteId);
		verify(carrinhoService).buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		assertThat(exception.getMessage()).isEqualTo("Erro ao dar baixa no estoque.");
		assertThat(pagamentoFake.getPagamentosCancelados()).isNotEmpty();
	}
}
