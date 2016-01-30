package br.com.rest.config;

import br.com.rest.filters.ApplicationFilter;
import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matheus on 26/12/15.
 */
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ServerProperties.PROVIDER_PACKAGES, "br.com.rest.resources");
		properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		return properties;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new JettisonFeature());
		singletons.add(new MultiPartFeature());
		singletons.add(new ApplicationFilter());
		return singletons;
	}
}
