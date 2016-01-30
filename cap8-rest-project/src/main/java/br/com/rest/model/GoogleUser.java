package br.com.rest.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Matheus on 20/12/15.
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GoogleUser {

	private String sub;
	private String email;
	private String name;
	private String gender;

}
