package br.com.rest.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jettison.JettisonFeature;

@ApplicationPath("cervejaria")
public class AppConfig extends Application {

	// @Override
	// public Set<Class<?>> getClasses() {
	// Set<Class<?>> classes = new HashSet<>();
	// classes.add(CervejaResource.class);
	// return classes;
	// }

	/**
	 * Sobrescrevendo o método getProperties, é possivel informar parametros
	 * para a implementacao do JAX-RS. Deste modo, foi informado o pacote que
	 * contem os recursos REST. Com esta configuracao o Jersey mapeara todas as
	 * classes deste pacote automaticamente, dispensando a configuracao atraves
	 * do metodo getClasses.
	 * 
	 * @see javax.ws.rs.core.Application#getProperties()
	 */
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("jersey.config.server.provider.packages", "br.com.rest.resources");
		return properties;
	}

	/**
	 * Para adicionar suporte ao formato JSON no Jersey, é preciso adicionar um
	 * objeto do tipo JettisonFeature no set de singletons da configuracao do
	 * JAX-RS.
	 * 
	 * @see javax.ws.rs.core.Application#getSingletons()
	 */
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new JettisonFeature());
		return singletons;
	}

}
