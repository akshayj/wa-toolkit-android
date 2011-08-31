package com.windowsazure.samples.internal.xml;


public abstract class DOMBuilder {

	public String getXmlString(boolean suppressHeader) {
		if (dom == null) {
			buildDOM();
		}
		
		if (dom == null)
			return null;
		
		return dom.getXmlString(suppressHeader);
	}
	
	protected XmlAttribute addAttribute(XmlNode parent, String name, String value) {
		return addAttribute(parent, null, name, value);
	}
	
	protected XmlAttribute addAttribute(XmlNode parent, String tag, String name, String value) {
		XmlAttribute attribute = new XmlAttribute(tag, name, value);
		parent.addAttribute(attribute);
		return attribute;
	}
	
	protected XmlNamespace addDefaultNamespace(XmlNode node, String uri) {
		return addNamespace(node, null, uri);
	}
	
	protected void addHeaderAttribute(String name, String value) {
		assert(dom != null);
		dom.addHeaderAttribute(name, value);
	}
	
	protected XmlNamespace addNamespace(XmlNode node, String tag, String uri) {
		XmlNamespace namespace = new XmlNamespace(tag, uri);
		node.addNamespace(namespace);
		return namespace;
	}
	
	protected XmlNode addNode(XmlNode parent, String name) {
		return addNode(parent, null, name);
	}
	
	protected XmlNode addNode(XmlNode parent, String tag, String name) {
		XmlNode node = new XmlNode(tag, name);
		parent.addChild(node);
		return node;
	}
	
	protected XmlNode addRootNode(String name) {
		return addRootNode(null, name);
	}
	
	protected XmlNode addRootNode(String tag, String name) {
		dom = new XmlDOM();
		return dom.addRootNode(tag, name);
	}
	
	protected XmlNode addTextNode(XmlNode parent, String name, String text) {
		return addTextNode(parent, null, name, text);
	}
	
	protected XmlNode addTextNode(XmlNode parent, String tag, String name, String text) {
		XmlNode node = new XmlNode(tag, name);
		node.setInnerText(text);
		parent.addChild(node);
		return node;
	}
	
	protected XmlDOM getDOM() {
		return dom;
	}
	
	protected void removeHeaderAttribute(String name) {
		assert(dom != null);
		dom.removeHeaderAttribute(name);
	}
	
	protected abstract void buildDOM();
	
	private XmlDOM dom;
}
