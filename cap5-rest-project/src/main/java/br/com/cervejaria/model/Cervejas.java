package br.com.cervejaria.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Cervejas implements Serializable {

	private static final long serialVersionUID = 6552743658524195771L;
	private List<Cerveja> cervejas;

	public Cervejas() {

	}
}
