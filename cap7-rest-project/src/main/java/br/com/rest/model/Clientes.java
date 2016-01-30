package br.com.rest.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@XmlRootElement
@AllArgsConstructor
public class Clientes implements Serializable {

	private static final long serialVersionUID = -1750752262585754278L;
	private List<Cliente> clientes;

	public Clientes() {

	}

}
