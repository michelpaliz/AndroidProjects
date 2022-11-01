package com.example.recyclerviewpais.Parser;

import android.content.Context;

import com.example.recyclerviewpais.Model.Pais;
import com.example.recyclerviewpais.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class ParserPais {

    private List<Pais> paises;
    private Document document;
    private final InputStream countriesFile;

    //getResources() nos permite acceder a nuestros Recursos
    public ParserPais(Context c){
       this.countriesFile = c.getResources().openRawResource(R.raw.countries);
    }

    //Mediante los datos que tenemos en nuestro XML nuestro Parser se encargara de distribuirlos a nuestra Lista
    public boolean parserPaisesXML() {
        paises = new ArrayList<>();
        Pais pais;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
             DocumentBuilder builder = factory.newDocumentBuilder();
             document = builder.parse(countriesFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        NodeList childrenCountry = document.getElementsByTagName("country");
        for (int i = 0; i < childrenCountry.getLength() ; i++) {
            Node node = childrenCountry.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                pais = new Pais();
                String imagen = "_"+element.getAttributes().getNamedItem("countryCode").getNodeValue().toLowerCase();
                pais.setImagen(imagen);
                String countryName = element.getAttributes().getNamedItem("countryName").getNodeValue();
                String countryCapital = element.getAttributes().getNamedItem("capital").getNodeValue();
                long countryPopulation = Long.parseLong(element.getAttributes().getNamedItem("population").getNodeValue());
                String countryCode = element.getAttributes().getNamedItem("countryCode").getNodeValue();
                pais = new Pais(imagen,countryName,countryCapital,countryPopulation,countryCode);
                paises.add(pais);
            }
        }

        return true;

    }

    public List<Pais> getPaises() {
        return paises;
    }




}
