package com.tibco.businessworks6.sonar.plugin;

import java.util.NoSuchElementException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper {

	public static Element firstChildElement(Element element,
			String childNameSpace, String childName)
			throws NoSuchElementException {
		NodeList childCandidateList;
		if (childNameSpace == null || childNameSpace.isEmpty()) {
			childCandidateList = element.getElementsByTagName(childName);
		} else {
			/*childCandidateList = element.getElementsByTagNameNS(childNameSpace,
					childName);*/
			childCandidateList = element.getElementsByTagNameNS(childNameSpace,childName);
		}
		if (childCandidateList.getLength() > 0) {
			Element firstChild = (Element) childCandidateList.item(0);
			return firstChild;
		} else {
			throw new NoSuchElementException(
					"No child candidate found in this element");
		}
	}

	public static Element firstChildElement(Element element, String childName)
			throws NoSuchElementException {
		return firstChildElement(element, null, childName);
	}

	public static Node evaluateXpathNode(Node rootNode,
			String xPathQuery) throws XPathExpressionException {
		XPath xPath =  XPathFactory.newInstance().newXPath();
		return (Node) xPath.compile(xPathQuery).evaluate(rootNode, XPathConstants.NODE);
	}

}
