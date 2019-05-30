package base.xml;

import base.xml.handler.SAXParserHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class XmlReadingBySAX01 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // -1 get the factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //-2 get the parser by factory
        SAXParser parser = factory.newSAXParser();
        // -3 parse xml
        SAXParserHandler handler = new SAXParserHandler();
        parser.parse("D:\\myrepo\\demo\\src\\base\\xml\\books.xml", handler);

    }
}
