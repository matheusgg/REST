package br.com.rest.model;

import java.io.Serializable;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlRootElement
public class Cerveja implements Serializable {

	private static final long serialVersionUID = -468778937421378478L;
	private Integer id;
	private String nome;

	/**
	 * O JAX-RS possui suporte para HATEOAS atraves da classe Link.
	 */
	private Link link;

	public Cerveja() {

	}
}
