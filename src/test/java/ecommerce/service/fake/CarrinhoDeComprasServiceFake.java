package ecommerce.service.fake;

import java.util.HashMap;
import java.util.Map;

import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.service.CarrinhoDeComprasService;

public class CarrinhoDeComprasServiceFake extends CarrinhoDeComprasService {
    private Map<Long, CarrinhoDeCompras> carrinhos;

    public CarrinhoDeComprasServiceFake() {
        super(null);
        this.carrinhos = new HashMap<>();
    }

    public void adicionarCarrinho(CarrinhoDeCompras carrinho) {
        carrinhos.put(carrinho.getId(), carrinho);
    }

    @Override
    public CarrinhoDeCompras buscarPorCarrinhoIdEClienteId(Long carrinhoId, Cliente cliente) {
        CarrinhoDeCompras carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null || !carrinho.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Carrinho não encontrado.");
        }
        return carrinho;
    }

    public void limpar() {
        carrinhos.clear();
    }
}
