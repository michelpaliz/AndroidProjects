package com.example.selectcountry.Parser;
import android.content.Context;

import com.example.selectcountry.Model.Pais;
import com.example.selectcountry.R;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


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
        Pais pais = new Pais();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
             DocumentBuilder builder = factory.newDocumentBuilder();
             document = builder.parse(countriesFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

//        NodeList childrenCountries = document.getChildNodes();
//        Element countries = (Element) childrenCountries.item(0);

        //No es necesario especificar el root
//        Element root = document.getDocumentElement();
        NodeList childrenCountry = document.getElementsByTagName("country");

        for (int i = 0; i < childrenCountry.getLength() ; i++) {
            Node node = childrenCountry.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                pais = new Pais();
                pais.setImagen("_"+element.getAttribute("countryCode").toLowerCase());
                pais.setNombre(element.getAttribute("countryName"));
//                pais.setPoblacion((long)Integer.parseInt(element.getAttribute("population")));
                pais.setPoblacion(String.valueOf(Long.parseLong(element.getAttribute("population"))));
                pais.setCapital(element.getAttribute("capital"));
                pais.setIsoAlpha(element.getAttribute("isoAlpha3"));
                paises.add(pais);
            }
        }

        return true;

    }

    public List<Pais> getPaises() {
        return paises;
    }
}
