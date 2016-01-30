package br.com.rest.resources;

import br.com.rest.filters.Intercept;
import br.com.rest.model.Cliente;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matheus on 26/12/15.
 */
@Path("/tests")
@Intercept
public class TestResource {

	private static final Map<String, String> ASYNC_RESPONSES = new HashMap<>();
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(50);

	public TestResource() {
		this.getClass();
	}

	@GET
	@Path("/testUriInfo")
	public Response testUriInfo(@Context UriInfo uriInfo) {
		return Response.ok(uriInfo.getQueryParameters()).build();
	}

	@GET
	@Path("/testDefaltValue")
	public Response testDefaltValue(@QueryParam("param") @DefaultValue("Valor Padrão!") String param) {
		return Response.ok(param).build();
	}

	@POST
	@Path("/testBV")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response testBV(@Valid Cliente cliente) {
		return Response.ok(cliente).build();
	}

	@GET
	@Path("/testHeaders")
	public Response testHeaders(@HeaderParam("Content-Type") String contentType, @Context HttpHeaders headers) {
		return Response.ok(contentType + " / " + headers).build();
	}

	@POST
	@Path("/testMultipart")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response testMultipart(MultiPart multiPart) {
		StringBuilder builder = new StringBuilder();
		List<BodyPart> parts = multiPart.getBodyParts();
		parts.forEach(p -> builder.append(p.getEntity()).append(" - ").append(p.getMediaType()).append("\n"));
		return Response.ok(builder.toString()).build();
	}

	@GET
	@Path("/testAsync")
	public void testAsync(@Suspended AsyncResponse asyncResponse) {
		String id = UUID.randomUUID().toString();
		Link resourceURI = Link.fromUri("http://localhost:8080/rest/tests/getAsyncResult/{id}").build(id);

		EXECUTOR.execute(() -> {
			try {
				ASYNC_RESPONSES.put(id, "Processamento completado com sucesso!");
				TimeUnit.SECONDS.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				asyncResponse.resume(Response.accepted("Processamento Iniciado! Por favor, aguarde 10 segundos e acesse a URL indicada pelo cabeçalho Location")
						.header("Location", resourceURI.getUri())
						.build());
			}
		});
	}

	@GET
	@Path("/getAsyncResult/{id}")
	public Response getAsyncResult(@PathParam("id") String id) {
		String result = ASYNC_RESPONSES.get(id);

		if (result != null) {
			return Response.ok(result).build();
		} else {
			throw new WebApplicationException("Resultado não encontrado!", Response.Status.NOT_FOUND);
		}
	}
}