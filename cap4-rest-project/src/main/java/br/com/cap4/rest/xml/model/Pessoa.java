//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.11.02 às 02:50:37 PM BRST 
//

package br.com.cap4.rest.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java de Pessoa complex type.
 * 
 * <p>
 * O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro
 * desta classe.
 * 
 * <pre>
 * &lt;complexType name="Pessoa">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idade" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="enderecos" type="{http://www.cap4-rest-project.com.br/pessoa}Endereco" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pessoa", propOrder = { "nome", "idade", "enderecos" })
public class Pessoa {

	@XmlElement(required = true)
	protected String nome;
	protected int idade;
	protected List<Endereco> enderecos;

	/**
	 * Obtém o valor da propriedade nome.
	 * 
	 * @return
	 * 		possible object is
	 *         {@link String }
	 * 
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Define o valor da propriedade nome.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 * 
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * Obtém o valor da propriedade idade.
	 * 
	 */
	public int getIdade() {
		return idade;
	}

	/**
	 * Define o valor da propriedade idade.
	 * 
	 */
	public void setIdade(int value) {
		this.idade = value;
	}

	/**
	 * Gets the value of the enderecos property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the enderecos
	 * property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getEnderecos().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Endereco }
	 * 
	 * 
	 */
	public List<Endereco> getEnderecos() {
		if (enderecos == null) {
			enderecos = new ArrayList<Endereco>();
		}
		return this.enderecos;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pessoa [nome=" + nome + ", idade=" + idade + ", enderecos=" + enderecos + "]";
	}

}
