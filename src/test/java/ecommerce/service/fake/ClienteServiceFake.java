package ecommerce.service.fake;

import java.util.HashMap;
import java.util.Map;

import ecommerce.entity.Cliente;
import ecommerce.service.ClienteService;

public class ClienteServiceFake extends ClienteService {
    private Map<Long, Cliente> clientes;

    public ClienteServiceFake() {
        super(null);
        this.clientes = new HashMap<>();
    }

    public void adicionarCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Cliente buscarPorId(Long clienteId) {
        Cliente cliente = clientes.get(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        return cliente;
    }

    public void limpar() {
        clientes.clear();
    }
}
