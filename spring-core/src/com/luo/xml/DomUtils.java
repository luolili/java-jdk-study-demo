package com.luo.xml;

import com.luo.util.Assert;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * methods for working with DOM API
 */
public abstract class DomUtils {


    /**
     * Retrieves all child elements of the given DOM element that match any of the given element names.
     * only look at the direct child level of the given ele
     *
     * @param ele           Element is in rt.jar
     * @param childEleNames the child ele names to look for
     * @return child list
     */
    public static List<Element> getChildElementsByTagName(Element ele, String... childEleNames) {
        //-1 pre-check
        Assert.notNull(ele, "Element must not be null");
        Assert.notNull(childEleNames, "Element names collection must not be null");
        //-2 array to list
        List<String> childElementNameList = Arrays.asList(childEleNames);
        //-3 get child nodes
        NodeList nl = ele.getChildNodes();
        List<Element> childNodes = new ArrayList<>();
        //-4 travers nl to get  every child node
        for (int i = 0; i < nl.getLength(); i++) {
            //get the ith child node
            Node node = nl.item(i);
            //-5 node type check and name match, after they are true, add it
            if (node instanceof Element && nodeNameMatches(node, childElementNameList)) {
                childNodes.add((Element) node);
            }

        }

        return childNodes;


    }


    //retrieve the child list of the given ele and the given  ele name
    public static List<Element> getChildElementsByTagName(Element ele, String childEleName) {
        return getChildElementsByTagName(ele, new String[]{childEleName});
    }

    //return the first element identified by its name
    public static Element getChildElementByTagName(Element ele, String childEleName) {
        Assert.notNull(ele, "Element must not be null");
        Assert.notNull(childEleName, "Element name must not be null");

        NodeList nl = ele.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {

            Node node = nl.item(i);
            if (node instanceof Element && nodeNameMatches(node, childEleName)) {
                return (Element) node;
            }

        }
        return null;
    }


    public static String getTextValue(Element valueEle) {
        Assert.notNull(valueEle, "element is required");
        StringBuilder sb = new StringBuilder();

        NodeList nl = valueEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if ((node instanceof CharacterData && !(node instanceof Comment)) || (node instanceof EntityReference)) {
                sb.append(node.getNodeValue());
            }
        }

        return sb.toString();
    }

    public static String getChildElementValueByTagName(Element ele, String childEleName) {
        Element child = getChildElementByTagName(ele, childEleName);
        return (child != null ? getTextValue(child) : null);
    }


    public static List<Element> getChildElements(Element ele) {
        Assert.notNull(ele, "Element must not be null");
        NodeList nl = ele.getChildNodes();
        List<Element> childEles = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                childEles.add((Element) node);
            }
        }
        return childEles;
    }

    public static boolean nodeNameEquals(Node node, String desiredName) {
        Assert.notNull(node, "Node must not be null");
        Assert.notNull(desiredName, "Desired name must not be null");
        return nodeNameMatches(node, desiredName);
    }
    public static boolean nodeNameMatches(Node node, Collection<?> desiredNames) {
        return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
    }


    public static boolean nodeNameMatches(Node node, String desiredNames) {
        return (desiredNames.equals(node.getNodeName()) || desiredNames.equals(node.getLocalName()));
    }
}
