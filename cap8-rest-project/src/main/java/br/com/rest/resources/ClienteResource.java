package br.com.rest.resources;

import br.com.rest.model.Cliente;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matheus on 20/12/15.
 */
@Path("clientes")
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ClienteResource {

    private final static List<Cliente> CLIENTES = new ArrayList<>();

    static {
        Random random = new Random();
        String path = "http://localhost:8082/cap8-rest-project/rest/clientes/{id}";

        for (int i = 0; i < 10; i++) {
            Integer id = random.nextInt(9999);
            String nome = "Cliente " + id;
            Link link = Link.fromPath(path).rel(nome).title(nome).build(id);
            CLIENTES.add(new Cliente(id, nome, link));
        }
    }

    @GET
    public List<Cliente> findAll() {
        return CLIENTES;
    }

}
