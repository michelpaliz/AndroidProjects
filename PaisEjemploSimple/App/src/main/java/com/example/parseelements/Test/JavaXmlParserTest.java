package com.example.parseelements.Test;

import com.example.parseelements.Pais;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import android.content.res.Resources;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class JavaXmlParserTest {
    private Document document;
    private ArrayList<Pais> paises;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("countries.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(new FileInputStream("/home/michael/Documents/GitHub/android/app/src/main/java/com/example/parseelements/Test/countries.xml"));
    }
    @Test
    public void testReadElement(){
        NodeList children = document.getChildNodes();
        Element element = (Element) children.item(0);
        Assert.assertEquals("countries", element.getNodeName());
    }
    @Test
    public void testCountry(){
        paises = new ArrayList<>();
        Pais pais = new Pais();
//        NodeList childrenCountries = document.getChildNodes();
//        Element countries = (Element) childrenCountries.item(0);
        NodeList childrenCountry = document.getElementsByTagName("country");
        for (int i = 0; i < childrenCountry.getLength() ; i++) {
            Node node = childrenCountry.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                pais = new Pais();
                pais.setImagen(element.getAttribute("countryCode").toLowerCase(Locale.ROOT));
                pais.setNombre(element.getAttribute("countryName"));
                pais.setPoblacion((long)Integer.parseInt(element.getAttribute("population")));
                pais.setCapital(element.getAttribute("capital"));
                pais.setIsoAlpha(element.getAttribute("isoAlpha3"));

                paises.add(pais);
            }
        }
        System.out.println(paises);

    }
    @Test
    public void testAttribute(){
        NodeList childrenCountries = document.getChildNodes();
        Element countries = (Element) childrenCountries.item(0);
        NodeList childrenCountry = countries.getElementsByTagName("country");
        Element country = (Element) childrenCountry.item(0);
        Assert.assertEquals("Andorra",country.getAttribute("countryName"));
    }


}
