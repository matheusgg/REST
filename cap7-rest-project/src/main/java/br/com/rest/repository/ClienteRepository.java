package br.com.rest.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Link;

import br.com.rest.model.Cliente;

@RequestScoped
public class ClienteRepository implements Serializable {

	private static final long serialVersionUID = 5550117080462349445L;
	private static final List<Cliente> CLIENTES = new ArrayList<>();
	public static final String HOST = "http://localhost:8080/cap7-rest-project/rest/clientes/";

	static {
		Integer id = null;
		String nome = null;
		Link link = null;

		for (int i = 0; i < 10; i++) {
			id = i + 1;
			nome = "Cliente " + UUID.randomUUID().toString().substring(0, 8);
			link = Link.fromUri(HOST + "{id}").rel(nome).title(nome).build(id);
			CLIENTES.add(new Cliente(id, nome, link));
		}
	}

	public List<Cliente> listAll() {
		return CLIENTES;
	}

	public Cliente findById(Integer id) {
		for (Cliente cliente : CLIENTES) {
			if (cliente.getId().equals(id)) {
				return cliente;
			}
		}

		return null;
	}

	public Cliente findByNome(String nome) {
		for (Cliente cliente : CLIENTES) {
			if (cliente.getNome().equals(nome)) {
				return cliente;
			}
		}

		return null;
	}

	public void remove(Integer id) {
		CLIENTES.removeIf(c -> c.getId().equals(id));
	}

	public void save(Cliente cliente) {
		CLIENTES.add(cliente);
	}
}
