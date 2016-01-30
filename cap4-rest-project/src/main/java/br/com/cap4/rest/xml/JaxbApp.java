package br.com.cap4.rest.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.cap4.rest.xml.model.Endereco;
import br.com.cap4.rest.xml.model.Pessoa;

public class JaxbApp {

	public static void main(String[] args) throws Exception {
		Endereco endereco = new Endereco();
		endereco.setCep("04419-140");
		endereco.setLogradouro("Tito Pacheco");

		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Matheus Gomes Goes");
		pessoa.setIdade(22);
		pessoa.getEnderecos().add(endereco);

		JAXBContext context = JAXBContext.newInstance("br.com.cap4.rest.xml.model");
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(pessoa, System.out);

		// JAXB.marshal(pessoa, System.out);
		System.out.println("\n=========================================================================");

		Unmarshaller unmarshaller = context.createUnmarshaller();
		JAXBElement<?> element = (JAXBElement<?>) unmarshaller.unmarshal(new File(
				"/Users/Matheus/Documents/workspace/cap4-rest-project/src/main/resources/Teste.xml"));
		System.out.println(element.getValue());
	}

}
