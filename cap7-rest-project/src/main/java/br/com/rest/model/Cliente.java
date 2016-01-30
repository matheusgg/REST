package br.com.rest.model;

import java.io.Serializable;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlRootElement
public class Cliente implements Serializable {

	private static final long serialVersionUID = 8624498020952749043L;
	private Integer id;
	private String nome;
	private Link link;

	public Cliente() {

	}

}
