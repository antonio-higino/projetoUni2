package ecommerce.external.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.external.IEstoqueExternal;

public class EstoqueSimulado implements IEstoqueExternal {
	private Map<Long, Long> estoque;
	private boolean simularIndisponibilidade;
	private boolean simularErroNaBaixa;
	private List<Long> produtosIndisponiveis;

	public EstoqueSimulado() {
		this.estoque = new HashMap<>();
		this.simularIndisponibilidade = false;
		this.simularErroNaBaixa = false;
		this.produtosIndisponiveis = new ArrayList<>();
	}

	public void adicionarProduto(Long produtoId, Long quantidade) {
		estoque.put(produtoId, quantidade);
	}

	public void simularIndisponibilidade(boolean simular) {
		this.simularIndisponibilidade = simular;
	}

	public void setProdutosIndisponiveis(List<Long> ids) {
		this.produtosIndisponiveis = ids;
	}

	public void simularErroNaBaixa(boolean simular) {
		this.simularErroNaBaixa = simular;
	}

	@Override
	public DisponibilidadeDTO verificarDisponibilidade(List<Long> produtosIds, List<Long> produtosQuantidades) {
		if (simularIndisponibilidade) {
			return new DisponibilidadeDTO(false, produtosIds);
		}

		if (!produtosIndisponiveis.isEmpty()) {
			return new DisponibilidadeDTO(false, produtosIndisponiveis);
		}

		List<Long> indisponiveis = new ArrayList<>();
		for (int i = 0; i < produtosIds.size(); i++) {
			Long produtoId = produtosIds.get(i);
			Long quantidadeSolicitada = produtosQuantidades.get(i);
			Long quantidadeDisponivel = estoque.getOrDefault(produtoId, 0L);

			if (quantidadeDisponivel < quantidadeSolicitada) {
				indisponiveis.add(produtoId);
			}
		}

		boolean disponivel = indisponiveis.isEmpty();
		return new DisponibilidadeDTO(disponivel, indisponiveis);
	}

	@Override
	public EstoqueBaixaDTO darBaixa(List<Long> produtosIds, List<Long> produtosQuantidades) {
		if (simularErroNaBaixa) {
			return new EstoqueBaixaDTO(false);
		}

		for (int i = 0; i < produtosIds.size(); i++) {
			Long produtoId = produtosIds.get(i);
			Long quantidade = produtosQuantidades.get(i);
			Long quantidadeAtual = estoque.getOrDefault(produtoId, 0L);

			estoque.put(produtoId, quantidadeAtual - quantidade);
		}

		return new EstoqueBaixaDTO(true);
	}

	public void limpar() {
		estoque.clear();
		simularIndisponibilidade = false;
		simularErroNaBaixa = false;
		produtosIndisponiveis.clear();
	}
}
