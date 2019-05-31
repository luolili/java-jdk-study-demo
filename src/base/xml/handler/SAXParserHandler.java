package base.xml.handler;

import base.xml.entity.Book;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.events.EndElement;
import java.util.ArrayList;
import java.util.List;

public class SAXParserHandler extends DefaultHandler {

    private String value = null;
    // xml to entity Book
    private Book book = null;
    private List<Book> books = new ArrayList();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        //qName means node name
        if ("book".equals(qName)) {

            book = new Book();
            //traverse attr
            String id = attributes.getValue("id");

            System.out.println(id);
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                System.out.println(attributes.getQName(i));
                if ("id".equals(attributes.getQName(i))) {
                    book.setId(attributes.getValue(i));
                }
            }

        } else if (!"book".equals(qName) && !"catalog".equals(qName)) {
            System.out.print("node name:" + qName + "------");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        //it means a node has end
        if ("book".equals(qName)) {
            books.add(book);
            book = null;

            System.out.println("end a node");
        } else if ("author".equals(qName)) {
            book.setAuthor(value);

        } else if ("title".equals(qName)) {
            book.setTitle(value);
        } else if ("genre".equals(qName)) {
            book.setGenre(value);
        } else if ("price".equals(qName)) {
            book.setPrice(value);
        } else if ("publish_date".equals(qName)) {
            book.setPublishDate(value);
        } else if ("description".equals(qName)) {
            book.setDescription(value);
        }
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

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        //get node value(content)
        value = new String(ch, start, length);
        if (!"".equals(value.trim())) {
            System.out.println(value);
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
