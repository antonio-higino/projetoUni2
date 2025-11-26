package ecommerce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.dto.CompraDTO;
import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import jakarta.transaction.Transactional;

@Service
public class CompraService
{

	private final CarrinhoDeComprasService carrinhoService;
	private final ClienteService clienteService;

	private final IEstoqueExternal estoqueExternal;
	private final IPagamentoExternal pagamentoExternal;

	@Autowired
	public CompraService(CarrinhoDeComprasService carrinhoService, ClienteService clienteService,
			IEstoqueExternal estoqueExternal, IPagamentoExternal pagamentoExternal)
	{
		this.carrinhoService = carrinhoService;
		this.clienteService = clienteService;

		this.estoqueExternal = estoqueExternal;
		this.pagamentoExternal = pagamentoExternal;
	}

	@Transactional
	public CompraDTO finalizarCompra(Long carrinhoId, Long clienteId)
	{
		Cliente cliente = clienteService.buscarPorId(clienteId);
		CarrinhoDeCompras carrinho = carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		List<Long> produtosIds = carrinho.getItens().stream().map(i -> i.getProduto().getId())
				.collect(Collectors.toList());
		List<Long> produtosQtds = carrinho.getItens().stream().map(i -> i.getQuantidade()).collect(Collectors.toList());

		DisponibilidadeDTO disponibilidade = estoqueExternal.verificarDisponibilidade(produtosIds, produtosQtds);

		if (!disponibilidade.disponivel())
		{
			throw new IllegalStateException("Itens fora de estoque.");
		}

		BigDecimal custoTotal = calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

		PagamentoDTO pagamento = pagamentoExternal.autorizarPagamento(cliente.getId(), custoTotal.doubleValue());

		if (!pagamento.autorizado())
		{
			throw new IllegalStateException("Pagamento não autorizado.");
		}

		EstoqueBaixaDTO baixaDTO = estoqueExternal.darBaixa(produtosIds, produtosQtds);

		if (!baixaDTO.sucesso())
		{
			pagamentoExternal.cancelarPagamento(cliente.getId(), pagamento.transacaoId());
			throw new IllegalStateException("Erro ao dar baixa no estoque.");
		}

		CompraDTO compraDTO = new CompraDTO(true, pagamento.transacaoId(), "Compra finalizada com sucesso.");

		return compraDTO;
	} 

	public BigDecimal calcularCustoTotal(CarrinhoDeCompras carrinho, Regiao regiao, TipoCliente tipoCliente)
	{
		final BigDecimal DESCONTO_10 = new BigDecimal("0.90");
		final BigDecimal DESCONTO_20 = new BigDecimal("0.80");
		
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal frete = BigDecimal.ZERO;
		BigDecimal custoTotal = BigDecimal.ZERO;
		
		int quantidadeFragil = 0;
		
		//Contagem de quantidade por categoria
		for (ItemCompra item : carrinho.getItens()) {
			if (item.getProduto().isFragil()) {
				quantidadeFragil += item.getQuantidade().intValue();
			}
		}
		
		//Cálculo do subtotal
		for (ItemCompra item : carrinho.getItens()) {
			BigDecimal precoItem = item.getProduto().getPreco();
			precoItem = precoItem.multiply(BigDecimal.valueOf(item.getQuantidade()));
			subtotal = subtotal.add(precoItem);
		}
		
		subtotal = subtotal.setScale(2);
		
		//Desconto por valor do carrinho
		if(subtotal.compareTo(new BigDecimal("1000.00")) != -1) {
			subtotal = subtotal.multiply(DESCONTO_20);
		} else if(subtotal.compareTo(new BigDecimal("500.00")) != -1) {
			subtotal = subtotal.multiply(DESCONTO_10);
		}
		
		BigDecimal pesoTotal = BigDecimal.ZERO;
		
		//Cálculo do Peso Total
		for (ItemCompra item : carrinho.getItens()) {
			BigDecimal pesoItem = BigDecimal.ZERO;
			
			pesoItem = item.getProduto().getPesoFisico().setScale(2);
			pesoItem = pesoItem.multiply(BigDecimal.valueOf(item.getQuantidade()).setScale(2));
			
			pesoTotal = pesoTotal.add(pesoItem).setScale(2);
		}
		
		//Taxa de manuseio especial
		frete = frete.add(new BigDecimal("5.00").multiply(BigDecimal.valueOf(quantidadeFragil).setScale(2)));
		
		//Cálculo do frete por peso
		if (pesoTotal.compareTo(new BigDecimal("5.00")) == 1) {
			if (pesoTotal.compareTo(new BigDecimal("10.00")) != 1) {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("2.00")));
				
			} else if (pesoTotal.compareTo(new BigDecimal("50.00")) != 1) {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("4.00")));
				
			} else {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("7.00")));
			}
		}
		
		frete = frete.setScale(2);
		
		//Cálculo do custo total
		custoTotal = subtotal.add(frete);
		custoTotal = custoTotal.setScale(2, RoundingMode.HALF_UP);
		
		return custoTotal;
	}
}
