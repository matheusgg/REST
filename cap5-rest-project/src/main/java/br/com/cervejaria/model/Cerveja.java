package br.com.cervejaria.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 5368024384640151336L;
	private String nome;
	private TipoCerveja tipo;

	public Cerveja() {

	}

}
