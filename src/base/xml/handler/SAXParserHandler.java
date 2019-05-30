package base.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.events.EndElement;

public class SAXParserHandler extends DefaultHandler {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("parse start");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("parse end");
    }
}
