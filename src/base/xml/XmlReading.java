package base.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlReading {

    public static void main(String[] args) throws ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        try {
            Document doc = builder.parse("D:\\myrepo\\demo\\src\\base\\xml\\books.xml");
            NodeList books = doc.getElementsByTagName("book");
            int bookLen = books.getLength();


            for (int i = 0; i < bookLen; i++) {
                Node book = books.item(i);
                //get all attrs of a node
                NamedNodeMap attributes = book.getAttributes();
                int attrLen = attributes.getLength();
                for (int j = 0; j < attrLen; j++) {
                    Node attr = attributes.item(j);
                    String attrNodeName = attr.getNodeName();
                    String attrNodeValue = attr.getNodeValue();
                    System.out.println("name:" + attrNodeName);
                    System.out.println("name:" + attrNodeValue);

                }


            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
