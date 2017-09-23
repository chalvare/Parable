package com.parable.xml;

import com.parable.WPS.Coordenada;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RssParserSax
{

    private URL rssUrl;

    public RssParserSax(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<DatosXML> parse(Coordenada coord)
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            int i1= (int) coord.getX();
            int i2= (int) coord.getY();
            int i3= (int) coord.getZ();

            SAXParser parser = factory.newSAXParser();
            RssHandler handler = new RssHandler(i1,i2,i3);
            parser.parse(this.getInputStream(), handler);

            return handler.getPosicion();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }






}

