package br.com.rest.filters;

import br.com.rest.model.GoogleUser;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * The type Rest oauth 2 authorization filter.
 */
@WebFilter("/rest/*")
public class RestOauth2AuthorizationFilter implements Filter {

	/**
	 * Client ID gerado pelo Google durante o registro da aplicacao
	 */
	private static final String CLIENT_ID = "32881336158-pl3h62enl8i8rd2ovbasp4ohhnof3giu.apps.googleusercontent.com";

	/**
	 * Client Secret gerado pelo Google durante o registro da aplicacao
	 */
	private static final String CLIENT_SECRET = "Sirvhsn6QQhP3RZG_vFc8YN4";

	/**
	 * URL de callback registrada no registro da apliacao no Google
	 */
	private static final String CALLBACK = "https://localhost:8443/cap8-rest-project/rest";

	/**
	 * URL do Google para recuperar informacoes do usuario
	 */
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

	/**
	 * URLS que representam os contextos que se deseja acessar
	 */
	private static final List<String> SCOPES = Arrays.asList("profile", "email");

	/**
	 * The constant JSON_FACTORY.
	 */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * The constant HTTP_TRANSPORT.
	 */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/**
	 * Classe utilitaria da API do Google para realizar a configiracao da URL de requisicao do formulario de login
	 */
	private GoogleAuthorizationCodeFlow flow;

	/**
	 * Token de verificacao
	 */
	private String stateToken;

	/**
	 * Prepara a classe responsavel pela montagem da URL de requisicao do formulario de login e do token de verificacao.
	 *
	 * @param filterConfig the filter config
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES).setAccessType("offline").build();
		this.stateToken = "google;" + new SecureRandom().nextInt();
	}

	/**
	 * Do filter.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param chain    the chain
	 * @throws IOException      the io exception
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		try {
			String code = req.getParameter("code");
			String state = req.getParameter("state");

			if (code != null && state != null) {
				session.setAttribute("code", code);
				session.setAttribute("state", state);

			} else {
				code = (String) session.getAttribute("code");
				state = (String) session.getAttribute("state");
			}

			/*
			 * Caso seja a primeira visita do usuario, a aplicacao deve direciona-lo para o formulario de login do Google
			 */
			if (code == null || state == null) {
				this.makeRedirectToGoogleLogin(req, resp, session);

			} else {
				// Neste ponto o usuario ja se logou e o formulario de login o direcionou de volta para a aplicacao
				Credential credential = (Credential) session.getAttribute("credential");

				if (credential == null) {
					// Uma nova requisicao é preparada para o Google a fim de recuperar o Access Token de acordo com o code retornado pelo Google
					GoogleTokenResponse tokenResponse = this.flow.newTokenRequest(code).setRedirectUri(CALLBACK).execute();

					// Armazena o Access Token na memoria e utiliza essa credencial para realizar as demais requisicoes para o servidor do Google
					credential = this.flow.createAndStoreCredential(tokenResponse, null);

					// Recupera as informacoes do usuario que acabou de se logar
					GoogleUser googleUser = this.getGoogleUserIdentity(credential);

					session.setAttribute("googleUser", googleUser);
					session.setAttribute("credential", credential);
				}

				// Caso seja a primeira requisicao depois do login, o usuario é direcionado para a URL de requisicao original
				String originalRequestUrl = (String) session.getAttribute("originalRequestUrl");

				if (originalRequestUrl != null) {
					session.removeAttribute("originalRequestUrl");
					resp.sendRedirect(originalRequestUrl);

				} else {
					chain.doFilter(req, resp);
				}
			}

		} catch (Exception e) {
			session.removeAttribute("code");
			session.removeAttribute("state");
			session.removeAttribute("googleUser");
			session.removeAttribute("originalRequestUrl");
			session.removeAttribute("credential");

			req.getServletContext().log(e.getMessage(), e);
		}
	}

	/**
	 * Make redirect to google login.
	 *
	 * @param req     the req
	 * @param resp    the resp
	 * @param session the session
	 * @throws IOException the io exception
	 */
	private void makeRedirectToGoogleLogin(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
		// Salva a URL de requisicao original
		String requestUrl = req.getRequestURL().toString();
		session.setAttribute("originalRequestUrl", requestUrl);

		StringBuilder callback = new StringBuilder(CALLBACK);
		String queyString = req.getQueryString();

		if (queyString != null) {
			callback.append("&").append(queyString);
		}

		// Prepara a URL de requisicao do formulario de login do Google
		GoogleAuthorizationCodeRequestUrl googleRequestUrl = this.flow.newAuthorizationUrl();
		String googleUrl = googleRequestUrl.setRedirectUri(callback.toString()).setState(this.stateToken).build();

		// Realiza o redirecionamento
		resp.sendRedirect(googleUrl);
	}

	private GoogleUser getGoogleUserIdentity(Credential credential) throws IOException, JAXBException, JSONException, XMLStreamException {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);

		// Realiza uma nova requisicao para o Google a sim de recuperar informacoes sobre o usuario logado
		// Esta requisicao utiliza a credencial e o Access Token obtido anteriormente atraves do code
		GenericUrl genericUrl = new GenericUrl(USER_INFO_URL);
		HttpRequest httpRequest = requestFactory.buildGetRequest(genericUrl);
		httpRequest.getHeaders().setContentType("application/json");

		// Recupera as informacoes do usuario logado no formato JSON
		StringBuilder userIdentity = new StringBuilder("{'googleUser': ");
		userIdentity.append(httpRequest.execute().parseAsString()).append("}");

		JAXBContext jaxbContext = JAXBContext.newInstance(GoogleUser.class);
		MappedNamespaceConvention con = new MappedNamespaceConvention();
		XMLStreamReader reader = new MappedXMLStreamReader(new JSONObject(userIdentity.toString()), con);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		return (GoogleUser) unmarshaller.unmarshal(reader);
	}

	/**
	 * Destroy.
	 */
	@Override
	public void destroy() {

	}
}
