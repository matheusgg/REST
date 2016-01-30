//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.11.02 às 02:50:37 PM BRST 
//

package br.com.cap4.rest.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java de Endereco complex type.
 * 
 * <p>
 * O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro
 * desta classe.
 * 
 * <pre>
 * &lt;complexType name="Endereco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cep" type="{http://www.cap4-rest-project.com.br/pessoa}CEP"/>
 *         &lt;element name="logradouro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Endereco", propOrder = { "cep", "logradouro" })
public class Endereco {

	@XmlElement(required = true)
	protected String cep;
	@XmlElement(required = true)
	protected String logradouro;

	/**
	 * Obtém o valor da propriedade cep.
	 * 
	 * @return
	 * 		possible object is
	 *         {@link String }
	 * 
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * Define o valor da propriedade cep.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 * 
	 */
	public void setCep(String value) {
		this.cep = value;
	}

	/**
	 * Obtém o valor da propriedade logradouro.
	 * 
	 * @return
	 * 		possible object is
	 *         {@link String }
	 * 
	 */
	public String getLogradouro() {
		return logradouro;
	}

	/**
	 * Define o valor da propriedade logradouro.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 * 
	 */
	public void setLogradouro(String value) {
		this.logradouro = value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Endereco [cep=" + cep + ", logradouro=" + logradouro + "]";
	}

}
