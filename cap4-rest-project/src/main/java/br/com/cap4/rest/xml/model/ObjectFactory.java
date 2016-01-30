//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2015.11.02 às 02:50:37 PM BRST 
//


package br.com.cap4.rest.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.com.cap4_rest_project.pessoa package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Pessoa_QNAME = new QName("http://www.cap4-rest-project.com.br/pessoa", "pessoa");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.com.cap4_rest_project.pessoa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Pessoa }
     * 
     */
    public Pessoa createPessoa() {
        return new Pessoa();
    }

    /**
     * Create an instance of {@link Endereco }
     * 
     */
    public Endereco createEndereco() {
        return new Endereco();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pessoa }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cap4-rest-project.com.br/pessoa", name = "pessoa")
    public JAXBElement<Pessoa> createPessoa(Pessoa value) {
        return new JAXBElement<Pessoa>(_Pessoa_QNAME, Pessoa.class, null, value);
    }

}
