package br.com.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Matheus on 20/12/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Cliente {

	private Integer id;
	private String nome;
	private Link link;

}
