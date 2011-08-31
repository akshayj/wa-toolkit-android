package com.windowsazure.samples.internal.xml;

import java.util.Collection;
import java.util.Vector;

import com.windowsazure.samples.internal.xml.tokenizer.NodeTokenizer;


public final class XmlDOM {

	public static String EmptyXml = "<empty />";
	
	public XmlDOM() {}
	
	public XmlDOM(XmlNode node) {
		this.node = node;
	}
		
	public void addHeaderAttribute(String name, String value) {
		headerAttributes.add(new XmlAttribute(null, name, value));
	}
	
	public XmlNode addRootNode(String tag, String name) {
		node = new XmlNode(tag, name);
		return node;
	}
	
	public void fromString(String xml) {
		node = new NodeTokenizer(xml).getRootNode();
	}
	
	public Collection<XmlNode> getNodes(String xpath) {
		return XPath.getNodes(node, xpath);
	}
	
	public XmlNode getRootNode() {
		return node;
	}
	
	public XmlNode getSingleNode(String xpath) {
		return XPath.getSingleNode(node, xpath);
	}
	
	public String getXmlString(boolean suppressHeader) {
		if (node == null)
			return null;
		
		return suppressHeader ? node.getRepresentation() : getHeader() + node.getRepresentation();
	}
	
	public void removeHeaderAttribute(String name) {
		for (XmlAttribute attribute : headerAttributes) {
			if (attribute.getLocalName().equals(name)) {
				headerAttributes.remove(attribute);
				return;
			}
		}
	}
	
	private String getHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml");
		
		for (XmlAttribute attribute : headerAttributes) {
			sb.append(" " + attribute.getRepresentation());
		}
		
		sb.append("?>");
		return sb.toString();
	}
	
	private Vector<XmlAttribute> headerAttributes = new Vector<XmlAttribute>();
	private XmlNode node;
}
