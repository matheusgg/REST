package br.com.rest.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jettison.JettisonFeature;

/**
 * O Jersey gera automaticamente um XML descritor do servico REST, ou seja, o
 * WADL. Para acessa-lo, basta realizar uma requisicao a url base do servico
 * seguida por application.wadl. Ex.:
 * http://localhost:8080/cap7-rest-project/rest/application.wadl.
 * <br>
 * Esse XML é semelhante ao WSDL utilizado pelos servicos SOAP, e tem como
 * objetivo descrever as operacoes, os metodos HTTP suportados, assim como, as
 * representacoes de recursos disponiveis.
 * 
 * @author Matheus
 *
 */
@ApplicationPath("rest")
public class ApplicationConfig extends Application {

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("jersey.config.server.provider.packages", "br.com.rest.resources");
		return properties;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new JettisonFeature());
		return singletons;
	}

}
