package br.com.cervejaria.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import br.com.cervejaria.model.Cerveja;
import br.com.cervejaria.model.Cervejas;
import br.com.cervejaria.model.TipoCerveja;

@WebServlet(value = "/cervejas/*", loadOnStartup = 1)
public class CervejaServlet extends HttpServlet {

	private static final long serialVersionUID = 3769101772289490289L;

	private static final List<Cerveja> CERVEJAS = new ArrayList<>();
	private JAXBContext jaxbContext = null;

	static {
		for (int i = 0; i < 10; i++) {
			CERVEJAS.add(new Cerveja("Cerveja " + (i + 1), TipoCerveja.PILSEN));
		}
	}

	@Override
	public void init() throws ServletException {
		try {
			this.jaxbContext = JAXBContext.newInstance(Cerveja.class, Cervejas.class);
		} catch (Exception e) {
			throw new UnavailableException(e.getMessage());
		}

	}

	/**
	 * Cria um recurso, ou seja, uma nova cerveja de acordo com o nome e o tipo
	 * informado narequisicao.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String uri = req.getRequestURI();
			String[] parts = uri.split("/");
			uri = parts[parts.length - 1];

			if ("cervejas".equals(uri)) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Informe o nome da nova cerveja!");

			} else {
				String tipo = req.getParameter("tipo");

				if (tipo == null) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Informe o tipo da nova cerveja!");
					return;
				}

				String nome = URLDecoder.decode(uri, "UTF-8");
				Cerveja cerveja = this.recuperaCervejaPeloNome(nome);

				if (cerveja != null) {
					resp.sendError(HttpServletResponse.SC_CONFLICT, "Já existe uma cerveja com o nome informado!");
					resp.setHeader("Location", req.getRequestURI());
					return;
				}

				TipoCerveja tipoCerveja = TipoCerveja.valueOf(tipo);
				cerveja = new Cerveja(nome, tipoCerveja);
				CERVEJAS.add(cerveja);

				resp.setContentType("application/json;charset=UTF-8");
				resp.setStatus(HttpServletResponse.SC_CREATED);
				resp.setHeader("Location", req.getRequestURI());
				this.writeJson(cerveja, resp);
			}

		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Recupera uma, ou varias, cervejas de acordo com a URI requisitada. Caso
	 * um nome de uma cerveja seja especificada, ele sera retornada se existir,
	 * caso contrario, todas serao devolvidas.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String contentType = this.getContentType(req);

			if (contentType == null) {
				throw new IllegalArgumentException("O formato solicitado não é suportado!");
			}

			resp.setContentType(contentType + ";charset=UTF-8");

			Object cerveja = this.recuperaCervejaRequisitada(req);

			if (cerveja == null) {
				throw new RuntimeException("A cerveja informada não foi encontrada!");
			}

			if ("application/xml".equals(contentType)) {
				this.writeXML(cerveja, resp);

			} else {
				this.writeJson(cerveja, resp);
			}

		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());

		} catch (RuntimeException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());

		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Recupera a cerveja informada de acordo com a URI da requisicao.
	 */
	private Object recuperaCervejaRequisitada(HttpServletRequest req) throws UnsupportedEncodingException {
		String uri = req.getRequestURI();
		String[] parts = uri.split("/");
		uri = parts[parts.length - 1];

		Object obj = null;

		if ("cervejas".equals(uri)) {
			obj = new Cervejas(CERVEJAS);

		} else {
			uri = URLDecoder.decode(uri, "UTF-8");
			obj = this.recuperaCervejaPeloNome(uri);
		}

		return obj;
	}

	/**
	 * Recupera a cerveja pelo nome informado.
	 */
	private Cerveja recuperaCervejaPeloNome(String nome) {
		for (Cerveja cerveja : CERVEJAS) {
			if (nome.equals(cerveja.getNome())) {
				return cerveja;
			}
		}

		return null;
	}

	/**
	 * Escreve o objeto no stream de saida da resposta em formato xml.
	 */
	private void writeXML(Object obj, HttpServletResponse resp) throws JAXBException, IOException {
		Marshaller marshaller = this.jaxbContext.createMarshaller();
		marshaller.marshal(obj, resp.getOutputStream());
	}

	/**
	 * Escreve o objeto no stream de saida da resposta em formato json.
	 */
	private void writeJson(Object obj, HttpServletResponse resp) throws IOException, JAXBException {
		MappedNamespaceConvention con = new MappedNamespaceConvention();
		XMLStreamWriter writer = new MappedXMLStreamWriter(con, resp.getWriter());

		Marshaller marshaller = this.jaxbContext.createMarshaller();
		marshaller.marshal(obj, writer);
	}

	/**
	 * Recupera o tipo de conteudo de acordo com o cabecalho Accept considerando
	 * os pesos dos tipos informados.
	 */
	private String getContentType(HttpServletRequest req) {
		String contentType = null;
		String acceptHeader = req.getHeader("Accept");
		double xmlWeight = 0.0;
		double jsonWeight = 0.0;
		boolean anyType = false;

		if (acceptHeader != null) {
			String[] headers = acceptHeader.split(",");

			for (String header : headers) {

				if (header.contains("application/xml")) {
					if (header.contains(";q=")) {
						xmlWeight = Double.parseDouble(header.split(";q=")[1]);

					} else {
						xmlWeight = 1;
					}
				}

				if (header.contains("application/json")) {
					if (header.contains(";q=")) {
						jsonWeight = Double.parseDouble(header.split(";q=")[1]);

					} else {
						jsonWeight = 1;
					}
				}

				if ("*/*".equals(header)) {
					anyType = true;
				}
			}
		}

		if (xmlWeight > 0 && jsonWeight > 0) {
			if (xmlWeight > jsonWeight) {
				contentType = "application/xml";

			} else {
				contentType = "application/json";
			}

		} else if (xmlWeight > 0) {
			contentType = "application/xml";

		} else if (jsonWeight > 0) {
			contentType = "application/json";
		}

		if (contentType == null && anyType) {
			contentType = "application/json";
		}

		return contentType;
	}
}
