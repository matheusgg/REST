/**
 * Por padrao, o JAXB nao possui suporte nativo a classe Link do JAX-RS. Porem a
 * especificacao JAX-RS ja prove uma classe adaptadora responsavel por oferecer
 * suporte aos Links HATEOAS. Desta forma, é preciso informar para o JAXB este
 * adaptador atraves da anotacao XmlJavaTypeAdapter, que pode ser aplicado em
 * PACKAGE,FIELD,METHOD,TYPE,PARAMETER.
 */
@XmlJavaTypeAdapter(JaxbAdapter.class)
package br.com.rest.model;

import javax.ws.rs.core.Link.JaxbAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;