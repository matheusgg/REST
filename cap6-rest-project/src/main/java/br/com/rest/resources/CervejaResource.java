package br.com.rest.resources;

import br.com.rest.model.Cerveja;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

@Path("cervejas")
@Consumes(MediaType.APPLICATION_XML)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class CervejaResource {

	private static final List<Cerveja> CERVEJAS = new ArrayList<>();
	private static final Integer TAMANHO_PAGINA = 5;
	private static final Map<String, String> MIME_TYPES = new HashMap<>();

	static {
		Integer id = null;
		String nome = null;
		Link link = null;

		for (int i = 0; i < 30; i++) {
			id = i + 1;
			nome = "Cerveja " + (i + 1);

			// Link para HATEOAS
			link = Link.fromPath("cervejas/{nome}").rel(nome).title(nome).build(nome);

			CERVEJAS.add(new Cerveja(id, nome, link));
		}

		MIME_TYPES.put("image/jpg", ".jpg");
		MIME_TYPES.put("image/jpeg", ".jpeg");
		MIME_TYPES.put("image/png", ".png");
	}

	@GET
	@Path("greetings")
	public String greetings() {
		return "Olá Visitante";
	}

	@GET
	public List<Cerveja> listaCervejas(@QueryParam("pagina") int pagina) {
		if (pagina < 0) {
			pagina = 0;
		}

		int indiceInicial = pagina * TAMANHO_PAGINA;
		int indiceFinal = indiceInicial + TAMANHO_PAGINA;

		if (indiceFinal > CERVEJAS.size()) {
			indiceFinal = CERVEJAS.size();
		}

		if (indiceInicial >= indiceFinal) {
			indiceInicial = indiceFinal - TAMANHO_PAGINA;
		}

		return CERVEJAS.subList(indiceInicial, indiceFinal);
	}

	@GET
	@Path("{nome}")
	public Cerveja recuperaCervejaPeloNome(@PathParam("nome") String nome) {
		for (Cerveja cerveja : CERVEJAS) {
			if (cerveja.getNome().equals(nome)) {
				return cerveja;
			}
		}

		/*
		 * A especificacao JAX-RS possui uma excecao criada especialmente para
		 * representar erros na execucao do servico. A WebApplicationException
		 * possui um construtor que recebe o codigo de estado, sendo possivel
		 * assim, informar para o cliente o tipo de erro que aconteceu.
		 */
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@POST
	public Response criaCerveja(Cerveja cerveja) {
		for (Cerveja cervejaCadastrada : CERVEJAS) {
			if (cervejaCadastrada.getNome().equals(cerveja.getNome())) {
				throw new WebApplicationException(Response.Status.CONFLICT);
			}
		}

		Integer id = new Random().nextInt(100) + 10;
		Link link = Link.fromPath("cervejas/{nome}").rel(cerveja.getNome()).title(cerveja.getNome()).build(cerveja.getNome());
		cerveja.setId(id);
		cerveja.setLink(link);

		CERVEJAS.add(cerveja);

		/*
		 * O JAX-RS possui uma classe auxiliar para criar URIs com base em um
		 * template de URL, assim como na anotacao Path. A classe UriBuilder
		 * possui metodos utilitarios para criar URIs customizadas baseadas em
		 * parametros.
		 */
		URI location = UriBuilder.fromPath("cervejas/{nome}").build(cerveja.getNome());

		/*
		 * Um objeto do tipo Response esta sendo retornado para atender a um dos
		 * principios fundamentais dos servicos RESTful, ou seja, a correta
		 * utilizacao dos codigos de estado. Com a classe Response, é possivel
		 * construir um retorno com o codigo de estado que representa a entidade
		 * criada (201) e a localizacao do novo recurso.
		 */
		return Response.created(location).entity(cerveja).build();
	}

	@PUT
	@Path("{nome}")
	public void atualizaCerveja(@PathParam("nome") String nome, Cerveja cerveja) {
		int indiceCervejaAntiga = -1;

		for (int i = 0; i < CERVEJAS.size(); i++) {
			if (CERVEJAS.get(i).getNome().equals(nome)) {
				indiceCervejaAntiga = i;
				break;
			}
		}

		if (indiceCervejaAntiga < 0) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		Link link = Link.fromPath("cervejas/{nome}").rel(cerveja.getNome()).title(cerveja.getNome()).build(cerveja.getNome());
		cerveja.setLink(link);

		CERVEJAS.set(indiceCervejaAntiga, cerveja);
	}

	@DELETE
	@Path("{nome}")
	public void apagaCerveja(@PathParam("nome") String nome) {
		int indiceCervejaParaRemocao = -1;

		for (int i = 0; i < CERVEJAS.size(); i++) {
			if (CERVEJAS.get(i).getNome().equals(nome)) {
				indiceCervejaParaRemocao = i;
				break;
			}
		}

		if (indiceCervejaParaRemocao < 0) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		CERVEJAS.remove(indiceCervejaParaRemocao);
	}

	/**
	 * A aplicacao da anotacao Consumes no metodo sobrescreve a anotacao
	 * definida a novel de classe.
	 *
	 * @return
	 */
	@GET
	@Produces("image/*")
	@Consumes(MediaType.WILDCARD)
	public Response recuperaImagemCerveja() {
		try (InputStream imageStream = Cerveja.class.getResourceAsStream("/images/heineken.jpg")) {
			byte[] bytes = new byte[imageStream.available()];
			imageStream.read(bytes);

			return Response.ok(bytes).type("image/jpg").build();

		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * A anotacao Context faz com que a implementacao do JAX-RS realize a
	 * injecao de objetos, uma vez que eles sejam conhecidos pelo JAX-RS e seja
	 * permitido injeta-los. O JAX-RS permite a injecao de varios
	 * tipos de objetos, por exemplo, HttpServletRequest, HttpServletResponse,
	 * SecurityContext, etc. Este metodo recebera uma imagem em formato binario
	 * atraves do parametro dadosImagens e salvara essa imagem no disco.
	 *
	 * @param nome
	 * @param request
	 * @param dadosImagem
	 * @return
	 */
	@POST
	@Path("{nome}")
	@Consumes("image/*")
	public Response carregaImagemCerveja(@PathParam("nome") String nome, @Context HttpServletRequest request, byte[] dadosImagem) {
		String type = request.getContentType();

		if (type == null) {
			type = "image/jpg";
		}

		StringBuilder filePath = new StringBuilder(System.getProperty("user.home"));
		filePath.append(File.separator).append("Downloads").append(File.separator).append(nome).append(MIME_TYPES.get(type));

		try (FileOutputStream os = new FileOutputStream(filePath.toString())) {

			os.write(dadosImagem);
			return Response.ok().type(type).build();

		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
