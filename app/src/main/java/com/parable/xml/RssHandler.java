package com.parable.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ReplacedKevin on 27/11/15.
 */
public class RssHandler extends DefaultHandler {

    private List<DatosXML> posicion;
    private DatosXML posicionActual;
    private StringBuilder sbTexto;
    private int coordX;
    private int coordY;
    private int coordZ;
    private boolean acabado;
    private boolean finalizar;

    public RssHandler(int coordX, int coordY, int coordZ)
    {
        this.coordX=coordX;
        this.coordY=coordY;
        this.coordZ=coordZ;
        this.acabado=false;
        this.finalizar=false;

    }

    public List<DatosXML> getPosicion(){
        return posicion;
    }

    @Override
    public void startDocument() throws SAXException {

        //super.startDocument();

        posicion = new ArrayList<DatosXML>();
        sbTexto = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        sbTexto.setLength(0);
        //super.startElement(uri, localName, name, attributes);



        if (localName.equalsIgnoreCase("Planta")) {


            //noticiaActual.setIniz(Integer.parseInt(attributes.getValue("iniz")));

            //if(coordZ==noticiaActual.getIniz())
            if(coordZ== Integer.parseInt(attributes.getValue("iniz")))
            {
                posicionActual = new DatosXML();
                posicionActual.setIniz(Integer.parseInt(attributes.getValue("iniz")));

                acabado=true;
                finalizar=true;

            }
            else
            {
                finalizar=false;
                acabado=false;
            }

        }

        if(finalizar) {



            if(localName.equalsIgnoreCase("Aula")){

                posicionActual.setInix(Integer.parseInt(attributes.getValue("inix")));
                posicionActual.setFinx(Integer.parseInt(attributes.getValue("finx")));
                posicionActual.setIniy(Integer.parseInt(attributes.getValue("iniy")));
                posicionActual.setFiny(Integer.parseInt(attributes.getValue("finy")));


                if (coordX >= posicionActual.getInix() && coordX <= posicionActual.getFinx() && coordY >= posicionActual.getIniy() && coordY <= posicionActual.getFiny()) {
                    //Log.e("Entro2", "Prueba2");

                    acabado = true;

                } else {
                    acabado = false;
                }



            }
            if (localName.equalsIgnoreCase("Pasillo")) {


                posicionActual.setInix(Integer.parseInt(attributes.getValue("inix")));
                posicionActual.setFinx(Integer.parseInt(attributes.getValue("finx")));
                posicionActual.setIniy(Integer.parseInt(attributes.getValue("iniy")));
                posicionActual.setFiny(Integer.parseInt(attributes.getValue("finy")));
                //posicionActual.setPoi(Integer.parseInt(attributes.getValue("poi")));

                if (coordX >= posicionActual.getInix() && coordX <= posicionActual.getFinx() && coordY >= posicionActual.getIniy() && coordY <= posicionActual.getFiny()) {
                    //Log.e("Entro2", "Prueba2");

                    acabado = true;

                } else {
                    acabado = false;
                }

            }

            if (localName.equalsIgnoreCase("Seccion")) {


                posicionActual.setInix(Integer.parseInt(attributes.getValue("inix")));
                posicionActual.setFinx(Integer.parseInt(attributes.getValue("finx")));
                posicionActual.setIniy(Integer.parseInt(attributes.getValue("iniy")));
                posicionActual.setFiny(Integer.parseInt(attributes.getValue("finy")));


                if (coordX >= posicionActual.getInix() && coordX <= posicionActual.getFinx() && coordY >= posicionActual.getIniy() && coordY <= posicionActual.getFiny()) {

                    acabado = true;

                } else {
                    acabado = false;
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

       // super.characters(ch, start, length);
        sbTexto.append(ch, start, length);
    }



    public void endElement(String uri, String localName, String name) throws SAXException {

        //super.endElement(uri, localName, name);
        //Log.e("Entroend", "Pruebaend");
        if(acabado) {

            if (localName.equalsIgnoreCase("NombreAula")) {
                posicionActual.setNombreAula(sbTexto.toString());

            } else if (localName.equalsIgnoreCase("NombrePlanta")) {
                posicionActual.setNombrePlanta(sbTexto.toString());

            }else if (localName.equalsIgnoreCase("NombrePasillo")) {
                posicionActual.setNombrePasillo(sbTexto.toString());

            }else if (localName.equalsIgnoreCase("NombreSeccion")) {
                posicionActual.setNombreSeccion(sbTexto.toString());

            }else if (localName.equalsIgnoreCase("Poi")) {
                posicionActual.setPoi(sbTexto.toString());

            }else if (localName.equalsIgnoreCase("Planta")) {

                posicion.add(posicionActual);
            }else if (localName.equalsIgnoreCase("Pasillo")) {

                posicion.add(posicionActual);
            }else if (localName.equalsIgnoreCase("Seccion")) {

                posicion.add(posicionActual);
            }

            sbTexto.setLength(0);

        }






    }


    @Override
    public void endDocument() throws SAXException {
        //super.endDocument();
    }



}
