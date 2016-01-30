package br.com.cap4.rest.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import br.com.cap4.rest.json.model.Cliente;
import br.com.cap4.rest.json.model.Endereco;

public class JettisonpApp {

	public static void main(String[] args) throws Exception {
		Endereco endereco = new Endereco();
		endereco.setCep("04419-140");
		endereco.setLogradouro("Tito Pacheco");

		Cliente cliente = new Cliente();
		cliente.setNome("Matheus Gomes Goes");
		cliente.setIdade(22);
		cliente.getEnderecos().add(endereco);

		JAXBContext context = JAXBContext.newInstance(Cliente.class, Endereco.class);

		MappedNamespaceConvention con = new MappedNamespaceConvention();
		XMLStreamWriter writer = new MappedXMLStreamWriter(con, new OutputStreamWriter(System.out));

		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(cliente, writer);

		System.out.println("\n===============================================================================");

		File jsonFile = new File("/Users/Matheus/Documents/workspace/cap4-rest-project/src/main/resources/Cliente.json");

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)))) {
			String json = bufferedReader.readLine();
			XMLStreamReader reader = new MappedXMLStreamReader(new JSONObject(json), con);

			Unmarshaller unmarshaller = context.createUnmarshaller();
			Cliente cliente2 = (Cliente) unmarshaller.unmarshal(reader);
			System.out.println(cliente2);
		}
	}

}
