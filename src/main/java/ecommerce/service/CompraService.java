package ecommerce.service;

import java.math.BigDecimal;
import java.math.MathContext;
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
import ecommerce.entity.TipoProduto;
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
		final BigDecimal DESCONTO_5 = new BigDecimal("0.95");
		final BigDecimal DESCONTO_10 = new BigDecimal("0.90");
		final BigDecimal DESCONTO_15 = new BigDecimal("0.85");
		final BigDecimal DESCONTO_20 = new BigDecimal("0.80");
		
		MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
		
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal frete = BigDecimal.ZERO;
		BigDecimal custoTotal = BigDecimal.ZERO;
		
		int quantidadeFragil = 0;
		int quantidadeEletronico = 0;
		int quantidadeRoupa = 0;
		int quantidadeAlimento = 0;
		int quantidadeLivro = 0;
		int quantidadeMovel = 0;
		
		//Contagem de quantidade por categoria
		for (ItemCompra item : carrinho.getItens()) {
			if (item.getProduto().isFragil()) {
				quantidadeFragil += item.getQuantidade().intValue();
			}
			if (item.getProduto().getTipo().equals(TipoProduto.ELETRONICO)) {
				quantidadeEletronico += item.getQuantidade().intValue();
			}
			if (item.getProduto().getTipo().equals(TipoProduto.ROUPA)) {
				quantidadeRoupa += item.getQuantidade().intValue();
			}
			if (item.getProduto().getTipo().equals(TipoProduto.ALIMENTO)) {
				quantidadeAlimento += item.getQuantidade().intValue();
			}
			if (item.getProduto().getTipo().equals(TipoProduto.LIVRO)) {
				quantidadeLivro += item.getQuantidade().intValue();
			}
			if (item.getProduto().getTipo().equals(TipoProduto.MOVEL)) {
				quantidadeMovel += item.getQuantidade().intValue();
			}
		}
		
		//Cálculo do subtotal
		for (ItemCompra item : carrinho.getItens()) {
			BigDecimal precoTotal = item.getProduto().getPreco();
			precoTotal.multiply(BigDecimal.valueOf(item.getQuantidade()));
			
			if (item.getProduto().getTipo().equals(TipoProduto.ELETRONICO)) {
				if (quantidadeEletronico >= 3 && quantidadeEletronico <= 4) {
					precoTotal = precoTotal.multiply(DESCONTO_5);
				}
				if (quantidadeEletronico >= 5 && quantidadeEletronico <= 7) {
					precoTotal = precoTotal.multiply(DESCONTO_10);
				}
				if (quantidadeEletronico >= 8) {
					precoTotal = precoTotal.multiply(DESCONTO_15);
				}
			}
			if (item.getProduto().getTipo().equals(TipoProduto.ROUPA)) {
				if (quantidadeRoupa >= 3 && quantidadeRoupa <= 4) {
					precoTotal = precoTotal.multiply(DESCONTO_5);
				}
				if (quantidadeRoupa >= 5 && quantidadeRoupa <= 7) {
					precoTotal = precoTotal.multiply(DESCONTO_10);
				}
				if (quantidadeRoupa >= 8) {
					precoTotal = precoTotal.multiply(DESCONTO_15);
				}
			}
			if (item.getProduto().getTipo().equals(TipoProduto.ALIMENTO)) {
				if (quantidadeAlimento >= 3 && quantidadeAlimento <= 4) {
					precoTotal = precoTotal.multiply(DESCONTO_5);
				}
				if (quantidadeAlimento >= 5 && quantidadeAlimento <= 7) {
					precoTotal = precoTotal.multiply(DESCONTO_10);
				}
				if (quantidadeAlimento >= 8) {
					precoTotal = precoTotal.multiply(DESCONTO_15);
				}
			}
			if (item.getProduto().getTipo().equals(TipoProduto.LIVRO)) {
				if (quantidadeLivro >= 3 && quantidadeLivro <= 4) {
					precoTotal = precoTotal.multiply(DESCONTO_5);
				}
				if (quantidadeLivro >= 5 && quantidadeLivro <= 7) {
					precoTotal = precoTotal.multiply(DESCONTO_10);
				}
				if (quantidadeLivro >= 8) {
					precoTotal = precoTotal.multiply(DESCONTO_15);
				}
			}
			if (item.getProduto().getTipo().equals(TipoProduto.MOVEL)) {
				if (quantidadeMovel >= 3 && quantidadeMovel <= 4) {
					precoTotal = precoTotal.multiply(DESCONTO_5);
				}
				if (quantidadeMovel >= 5 && quantidadeMovel <= 7) {
					precoTotal = precoTotal.multiply(DESCONTO_10);
				}
				if (quantidadeMovel >= 8) {
					precoTotal = precoTotal.multiply(DESCONTO_15);
				}
			}
			subtotal = subtotal.add(precoTotal);
		}
		
		//Desconto por valor do carrinho
		if(subtotal.compareTo(new BigDecimal("1000")) == 1) {
			subtotal = subtotal.multiply(DESCONTO_20);
		} else if(subtotal.compareTo(new BigDecimal("500")) == 1) {
			subtotal = subtotal.multiply(DESCONTO_10);
		}
		
		/*for (ItemCompra item : carrinho.getItens()) {
			BigDecimal precoTotal = item.getProduto().getPreco();
			precoTotal.multiply(BigDecimal.valueOf(item.getQuantidade()));
			subtotal.add(precoTotal);
		}*/
		
		BigDecimal pesoTotal = BigDecimal.ZERO;
		
		//Cálculo do Peso Total
		for (ItemCompra item : carrinho.getItens()) {
			BigDecimal pesoCubico = BigDecimal.ZERO;
			BigDecimal pesoTributavel = BigDecimal.ZERO;
			
			pesoCubico = item.getProduto().getComprimento().multiply(item.getProduto().getLargura());
			pesoCubico = pesoCubico.multiply(item.getProduto().getAltura());
			pesoCubico = pesoCubico.divide(new BigDecimal("6000"), mc);
			
			pesoTributavel = pesoCubico.max(item.getProduto().getPesoFisico());
			pesoTributavel = pesoTributavel.multiply(BigDecimal.valueOf(item.getQuantidade()));
			
			pesoTotal = pesoTotal.add(pesoTributavel);
		}
		
		//Taxa de manuseio especial
		frete = frete.add(new BigDecimal("5").multiply(BigDecimal.valueOf(quantidadeFragil)));
		
		//Cálculo do frete por peso
		if (pesoTotal.compareTo(new BigDecimal("5.00")) == 1) {
			frete = frete.add(new BigDecimal("12.00"));
			if (pesoTotal.compareTo(new BigDecimal("10.00")) != 1) {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("2.00")));
				
			} else if (pesoTotal.compareTo(new BigDecimal("50.00")) != 1) {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("4.00")));
				
			} else {
				frete = frete.add(pesoTotal.multiply(new BigDecimal("7.00")));
			}
		}
		
		//Fator da região
		if (regiao.equals(Regiao.SUL)) {
			frete = frete.multiply(new BigDecimal("1.05"));
		}
		if (regiao.equals(Regiao.NORDESTE)) {
			frete = frete.multiply(new BigDecimal("1.10"));
		}
		if (regiao.equals(Regiao.CENTRO_OESTE)) {
			frete = frete.multiply(new BigDecimal("1.20"));
		}
		if (regiao.equals(Regiao.NORTE)) {
			frete = frete.multiply(new BigDecimal("1.30"));
		}
		
		//Benefício do cliente
		if (tipoCliente.equals(TipoCliente.OURO)) {
			frete = frete.multiply(BigDecimal.ZERO);
		}
		if (tipoCliente.equals(TipoCliente.PRATA)) {
			frete = frete.multiply(new BigDecimal("0.50"));
		}
		
		custoTotal = subtotal.add(frete, mc);
		
		return custoTotal;
	}
}
