package br.com.rest.resources;

import java.net.URI;
import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.com.rest.model.Cliente;
import br.com.rest.model.Clientes;
import br.com.rest.repository.ClienteRepository;

@Path("clientes")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ClienteResource {

	@Inject
	private ClienteRepository repository;

	@GET
	public Clientes listaClientes() {
		return new Clientes(this.repository.listAll());
	}

	@GET
	@Path("{id}")
	public Cliente recuperaPorId(@PathParam("id") Integer id) {
		return this.repository.findById(id);
	}

	@DELETE
	@Path("{id}")
	public void removeCliente(@PathParam("id") Integer id) {
		this.repository.remove(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response cadastraCliente(Cliente cliente) {
		String nome = cliente.getNome();
		Cliente existingEntity = this.repository.findByNome(nome);

		if (existingEntity != null) {
			throw new WebApplicationException("Cliente já cadastrado!", Response.Status.CONFLICT);
		}

		Integer id = new Random().nextInt(1000);
		Link link = Link.fromUri(ClienteRepository.HOST + "{id}").rel(nome).title(nome).build(id);

		cliente.setId(id);
		cliente.setLink(link);

		this.repository.save(cliente);
		URI location = UriBuilder.fromPath(ClienteRepository.HOST + "{id}").build(id);

		return Response.ok(cliente).location(location).build();
	}

}
