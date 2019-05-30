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

public class XmlReadingByDOM02 {

    public static void main(String[] args) throws ParserConfigurationException {

        // -1 get the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        // -2 get the builder by the factory
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        // -3 use the builder to parse xml
        try {
            Document doc = builder.parse("D:\\myrepo\\demo\\src\\base\\xml\\books.xml");
            NodeList books = doc.getElementsByTagName("book");
            // -4 get node list by doc's get  elementsByTagName
            int bookLen = books.getLength();

            for (int i = 0; i < bookLen; i++) {
                Node book = books.item(i);
                //get book's children
                NodeList childNodes = book.getChildNodes();
                System.out.println(childNodes.getLength());//13
                int len = 0;
                for (int k = 0; k < (len = childNodes.getLength()); k++) {
                    Node child = childNodes.item(k);
                    String nodeName = child.getNodeName();
                    if (child.getNodeType() == Node.ELEMENT_NODE) {

                        System.out.println(nodeName);
                        //node's value is his first child node's value
                        System.out.println(child.getFirstChild().getNodeValue());
                        System.out.println("-----");
                        System.out.println(child.getFirstChild().getTextContent());
                    }


                }


            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
