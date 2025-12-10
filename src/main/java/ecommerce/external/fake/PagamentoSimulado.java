package ecommerce.external.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import ecommerce.dto.PagamentoDTO;
import ecommerce.external.IPagamentoExternal;

public class PagamentoSimulado implements IPagamentoExternal
{
	private AtomicLong contadorTransacoes;
	private boolean simularPagamentoNaoAutorizado;
	private List<Long> pagamentosCancelados;
	private List<Long> pagamentosAutorizados;

	public PagamentoSimulado()
	{
		this.contadorTransacoes = new AtomicLong(1000);
		this.simularPagamentoNaoAutorizado = false;
		this.pagamentosCancelados = new ArrayList<>();
		this.pagamentosAutorizados = new ArrayList<>();
	}

	public void simularPagamentoNaoAutorizado(boolean simular)
	{
		this.simularPagamentoNaoAutorizado = simular;
	}

	@Override
	public PagamentoDTO autorizarPagamento(Long clienteId, Double custoTotal)
	{
		if (simularPagamentoNaoAutorizado)
		{
			return new PagamentoDTO(false, null);
		}

		Long transacaoId = contadorTransacoes.incrementAndGet();
		pagamentosAutorizados.add(transacaoId);

		return new PagamentoDTO(true, transacaoId);
	}

	@Override
	public void cancelarPagamento(Long clienteId, Long pagamentoTransacaoId)
	{
		pagamentosCancelados.add(pagamentoTransacaoId);
		pagamentosAutorizados.remove(pagamentoTransacaoId);
	}

	public boolean foiCancelado(Long transacaoId)
	{
		return pagamentosCancelados.contains(transacaoId);
	}

	public boolean estaAutorizado(Long transacaoId)
	{
		return pagamentosAutorizados.contains(transacaoId);
	}

	public List<Long> getPagamentosCancelados()
	{
		return new ArrayList<>(pagamentosCancelados);
	}

	public List<Long> getPagamentosAutorizados()
	{
		return new ArrayList<>(pagamentosAutorizados);
	}

	public void limpar()
	{
		contadorTransacoes.set(1000);
		simularPagamentoNaoAutorizado = false;
		pagamentosCancelados.clear();
		pagamentosAutorizados.clear();
	}
}
