package com.windowsazure.samples.internal.xml;

import java.util.Collection;
import java.util.Vector;

import com.windowsazure.samples.internal.util.Util;


public final class XmlNode extends XmlElement {

	public XmlNode(String tag, String name) {
		super(tag, name);
	}
	
	public void addAttribute(XmlAttribute attribute) {
		attributes.add(attribute);
	}
	
	public void addChild(XmlNode node) {
		children.add(node);
	}
	
	public void addNamespace(XmlNamespace namespace) {
		namespaces.add(namespace);
	}
	
	public XmlAttribute getAttributeByLocalName(String localName) {
		for (XmlAttribute attribute : attributes) {
			if (attribute.getLocalName().equals(localName))
				return attribute;
		}
		return null;
	}
	
	public XmlNode getChildByLocalName(String localName) {
		for (XmlNode node : children) {
			if (node.getLocalName().equals(localName))
				return node;
		}
		return null;
	}
	
	public Collection<XmlNode> getChildren() {
		return children;
	}
	
	public Collection<XmlNode> getDescendents() {
		Vector<XmlNode> nodes = new Vector<XmlNode>();
		for (XmlNode child : children) {
			addDescendents(child, nodes);
		}
		return nodes;
	}
	
	public String getInnerText() {
		return innerText;
	}
	
	public Collection<XmlNode> getNodes(String xpath) {
		return XPath.getNodes(this, xpath);
	}
	
	public String getRepresentation() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<" + getFullName());
		
		for (XmlNamespace namespace : namespaces) {
			sb.append(" " + namespace.getRepresentation());
		}
		
		for (XmlAttribute attribute : attributes) {
			sb.append(" " + attribute.getRepresentation());
		}
		
		if (isEmpty()) {
			sb.append(" />");
			return sb.toString();
		}
		
		sb.append('>');
		
		if (hasChildren()) {
			for (XmlNode node : children) {
				sb.append(node.getRepresentation());
			}
		}
		else
			sb.append(innerText);
		
		sb.append("</" + getFullName() + ">");
		return sb.toString();
	}
	
	public XmlNode getSingleNode(String xpath) {
		return XPath.getSingleNode(this, xpath);
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public boolean isEmpty() {
		return (children.size() == 0 && Util.isStringNullOrEmpty(innerText));
	}
	
	public void setInnerText(String text) {
		innerText = text;
	}
	
	private void addDescendents(XmlNode node, Vector<XmlNode> descendents) {
		descendents.add(node);
		for (XmlNode child : node.getChildren()) {
			addDescendents(child, descendents);
		}
	}
	
	private Vector<XmlAttribute> attributes = new Vector<XmlAttribute>();
	private Vector<XmlNode> children = new Vector<XmlNode>();
	private String innerText;
	private Vector<XmlNamespace> namespaces = new Vector<XmlNamespace>();
}
