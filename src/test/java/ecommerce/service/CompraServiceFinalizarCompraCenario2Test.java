package ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
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
import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import ecommerce.service.fake.CarrinhoDeComprasServiceFake;
import ecommerce.service.fake.ClienteServiceFake;

@ExtendWith(MockitoExtension.class)
public class CompraServiceFinalizarCompraCenario2Test {

    @Mock
    private IEstoqueExternal estoqueExternal;

    @Mock
    private IPagamentoExternal pagamentoExternal;

    private ClienteServiceFake clienteServiceFake;
    private CarrinhoDeComprasServiceFake carrinhoServiceFake;
    private CompraService service;

    @BeforeEach
    void setUp() {
        clienteServiceFake = new ClienteServiceFake();
        carrinhoServiceFake = new CarrinhoDeComprasServiceFake();
        service = new CompraService(carrinhoServiceFake, clienteServiceFake, estoqueExternal, pagamentoExternal);
    }

    @Test
    void deveFinalizarCompraComSucesso() {
        Long carrinhoId = 1L;
        Long clienteId = 1L;

        Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);
        clienteServiceFake.adicionarCliente(cliente);

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

        carrinhoServiceFake.adicionarCarrinho(carrinho);

        when(estoqueExternal.verificarDisponibilidade(anyList(), anyList()))
                .thenReturn(new DisponibilidadeDTO(true, new ArrayList<>()));
        when(pagamentoExternal.autorizarPagamento(anyLong(), anyDouble()))
                .thenReturn(new PagamentoDTO(true, 1001L));
        when(estoqueExternal.darBaixa(anyList(), anyList()))
                .thenReturn(new EstoqueBaixaDTO(true));

        CompraDTO resultado = service.finalizarCompra(carrinhoId, clienteId);

        verify(estoqueExternal).verificarDisponibilidade(anyList(), anyList());
        verify(pagamentoExternal).autorizarPagamento(anyLong(), anyDouble());
        verify(estoqueExternal).darBaixa(anyList(), anyList());

        assertThat(resultado.sucesso()).isTrue();
        assertThat(resultado.transacaoPagamentoId()).isEqualTo(1001L);
        assertThat(resultado.mensagem()).isEqualTo("Compra finalizada com sucesso.");
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueIndisponivel() {
        Long carrinhoId = 1L;
        Long clienteId = 1L;

        Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);
        clienteServiceFake.adicionarCliente(cliente);

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

        carrinhoServiceFake.adicionarCarrinho(carrinho);

        when(estoqueExternal.verificarDisponibilidade(anyList(), anyList()))
                .thenReturn(new DisponibilidadeDTO(false, List.of(1L)));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.finalizarCompra(carrinhoId, clienteId);
        });

        verify(estoqueExternal).verificarDisponibilidade(anyList(), anyList());

        assertThat(exception.getMessage()).isEqualTo("Itens fora de estoque.");
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoAutorizado() {
        Long carrinhoId = 1L;
        Long clienteId = 1L;

        Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);
        clienteServiceFake.adicionarCliente(cliente);

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

        carrinhoServiceFake.adicionarCarrinho(carrinho);

        when(estoqueExternal.verificarDisponibilidade(anyList(), anyList()))
                .thenReturn(new DisponibilidadeDTO(true, new ArrayList<>()));
        when(pagamentoExternal.autorizarPagamento(anyLong(), anyDouble()))
                .thenReturn(new PagamentoDTO(false, null));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.finalizarCompra(carrinhoId, clienteId);
        });

        verify(estoqueExternal).verificarDisponibilidade(anyList(), anyList());
        verify(pagamentoExternal).autorizarPagamento(anyLong(), anyDouble());

        assertThat(exception.getMessage()).isEqualTo("Pagamento não autorizado.");
    }

    @Test
    void deveCancelarPagamentoQuandoErroBaixaEstoque() {
        Long carrinhoId = 1L;
        Long clienteId = 1L;

        Cliente cliente = new Cliente(clienteId, "Cliente Teste", Regiao.SUDESTE, TipoCliente.BRONZE);
        clienteServiceFake.adicionarCliente(cliente);

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

        carrinhoServiceFake.adicionarCarrinho(carrinho);

        when(estoqueExternal.verificarDisponibilidade(anyList(), anyList()))
                .thenReturn(new DisponibilidadeDTO(true, new ArrayList<>()));
        when(pagamentoExternal.autorizarPagamento(anyLong(), anyDouble()))
                .thenReturn(new PagamentoDTO(true, 1001L));
        when(estoqueExternal.darBaixa(anyList(), anyList()))
                .thenReturn(new EstoqueBaixaDTO(false));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.finalizarCompra(carrinhoId, clienteId);
        });

        verify(estoqueExternal).verificarDisponibilidade(anyList(), anyList());
        verify(pagamentoExternal).autorizarPagamento(anyLong(), anyDouble());
        verify(estoqueExternal).darBaixa(anyList(), anyList());
        verify(pagamentoExternal).cancelarPagamento(anyLong(), anyLong());

        assertThat(exception.getMessage()).isEqualTo("Erro ao dar baixa no estoque.");
    }
}
