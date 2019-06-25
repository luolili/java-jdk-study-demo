package com.luo.xml;

import com.luo.util.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * methods for working with DOM API
 */
public abstract class DomUtils {


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
                childNodes.add(node);
            }

        }

        return childNodes;


    }

    public static boolean nodeNameMatches(Node node, Collection<?> desiredNames) {
        return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
    }
}
