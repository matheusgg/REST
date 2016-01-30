package br.com.rest.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jettison.JettisonFeature;

import br.com.rest.model.Cliente;
import br.com.rest.model.Clientes;
import br.com.rest.repository.ClienteRepository;

public class ServiceClient {

	/**
	 * API fluente para criacao de clientes rest. Para adicionar suporte ao
	 * formato JSON utilizando o Jersey é preciso registrar um JettisonFeature,
	 * que por sua vez adicionará um MessageBodyReader e um MessageBodyWriter
	 * responsaveis por ler e escrever objetos JSON.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JettisonFeature jsonFeature = new JettisonFeature();

		/*
		 * Recuperando os clientes
		 */
		Client client = ClientBuilder.newClient();

		/*
		 * O método target é responsavel por receber a URI base do servico. Já o
		 * método path é responsavel por configurar a URI relativa do recurso.
		 */
		Clientes clientes = client.register(jsonFeature).target(ClienteRepository.HOST).request(MediaType.APPLICATION_JSON).get(Clientes.class);

		List<Cliente> listaClientes = clientes.getClientes();
		listaClientes.forEach(c -> {
			Client clientTemp = ClientBuilder.newClient();
			/*
			 * O metodo invocation recebe um Link que substitui as chamadas aos
			 * metodos target e path.
			 */
			Cliente cliente = clientTemp.register(jsonFeature).invocation(c.getLink()).accept(MediaType.APPLICATION_JSON).get(Cliente.class);
			System.out.println(cliente);
		});

		/*
		 * Cadastrando um novo cliente
		 */
		Cliente cliente = new Cliente();
		cliente.setNome("Matheus Gomes Góes");
		cliente.setLink(Link.fromPath("clientes").build());

		client = ClientBuilder.newClient();
		Response response = client.register(jsonFeature).target(ClienteRepository.HOST).request(MediaType.APPLICATION_JSON).post(Entity.json(cliente));
		Link location = Link.fromUri(response.getLocation()).build();
		System.out.println(response.getStatus());

		client = ClientBuilder.newClient();
		cliente = client.register(jsonFeature).invocation(location).accept(MediaType.APPLICATION_JSON).get(Cliente.class);
		System.out.println(cliente);
	}

}
