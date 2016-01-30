package br.com.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Matheus on 26/12/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Cliente implements Serializable {

	private static final long serialVersionUID = 8624498020952749043L;

	@NotNull
	private Integer id;
	private String nome;
}
