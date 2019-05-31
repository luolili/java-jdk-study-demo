package base.xml;

import base.xml.entity.Book;
import base.xml.handler.SAXParserHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class XmlReadingBySAX01 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // -1 get the factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //-2 get the parser by factory
        SAXParser parser = factory.newSAXParser();
        // -3 parse xml
        SAXParserHandler handler = new SAXParserHandler();


        parser.parse("D:\\myrepo\\demo\\src\\base\\xml\\books.xml", handler);
        List<Book> books = handler.getBooks();
        int size = books.size();
        System.out.println("book num:" + size);


        for (Book book : books) {
            System.out.println(book);

        }

    }
}
