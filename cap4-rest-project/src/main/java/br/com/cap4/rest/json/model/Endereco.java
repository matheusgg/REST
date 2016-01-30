package br.com.cap4.rest.json.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Endereco {

	private String cep;
	private String logradouro;

	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep
	 *            the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	/**
	 * @return the logradouro
	 */
	public String getLogradouro() {
		return logradouro;
	}

	/**
	 * @param logradouro
	 *            the logradouro to set
	 */
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Endereco [cep=" + cep + ", logradouro=" + logradouro + "]";
	}

}
