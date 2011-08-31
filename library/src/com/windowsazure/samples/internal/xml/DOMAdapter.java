package com.windowsazure.samples.internal.xml;

import java.util.Collection;
import java.util.Vector;

import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;


public abstract class DOMAdapter<T> implements Builder<T> {

	protected static final String ALL_NODES = "*";
	protected static final String ANY_NODE = "*";
	
	protected DOMAdapter(XmlNode node) {
		dom = new XmlDOM(node);
	}
	
	protected DOMAdapter(String xmlString) {
		dom = new XmlDOM();
		if (! Util.isStringNullOrEmpty(xmlString))
		    dom.fromString(xmlString);
	}
	
	protected <V> Collection<V> buildCollection(Collection<XmlNode> nodeCollection, DOMAdapter<V> adapter)
		throws Exception {
		
		Vector<V> collection = new Vector<V>();
		for (XmlNode node : nodeCollection) {
			collection.add(adapter.build(node));
		}
		return collection;
	}
	
	protected String getInnerText(String localName)
		throws NodeNotFoundException {
		
		if (dom == null)
			throw new NodeNotFoundException();
		
		XmlNode node = getRootNode();
		if (node == null)
			throw new NodeNotFoundException();
		
		return getInnerText(node, localName);
	}
	
	protected String getInnerText(XmlNode parent, String localName)
		throws NodeNotFoundException {
		
		XmlNode node = parent.getChildByLocalName(localName);
		if (node == null)
			throw new NodeNotFoundException();
		
		return (node != null) ? node.getInnerText() : null;
	}
	
	protected XmlNode getNode(String localName)
		throws NodeNotFoundException {
		
		return getNode(getRootNode(), localName);
	}
	
	protected XmlNode getNode(XmlNode parent, String localName)
		throws NodeNotFoundException {
		
		XmlNode node = parent.getChildByLocalName(localName);
		if (node == null)
			throw new NodeNotFoundException();
		
		return node;
	}
	
	protected Collection<XmlNode> getNodeCollection(String localName) {
		return getNodeCollection(getRootNode(), localName);
	}
	
	protected Collection<XmlNode> getNodeCollection(XmlNode parent, String localName) {
		Vector<XmlNode> nodeVector = new Vector<XmlNode>();
		for (XmlNode node : parent.getChildren()) {
			if (localName.equals(ANY_NODE) || node.getLocalName().equals(localName))
				nodeVector.add(node);
		}
		return nodeVector;
	}
	
	protected String getOptionalInnerText(String localName) {
		return getOptionalInnerText(getRootNode(), localName);
	}
	
	protected String getOptionalInnerText(XmlNode parent, String localName) {
		XmlNode node = parent.getChildByLocalName(localName);
		return (node != null) ? node.getInnerText() : null;
	}
	
	protected XmlNode getOptionalNode(String localName) {
		return getOptionalNode(getRootNode(), localName);
	}
	
	protected XmlNode getOptionalNode(XmlNode parent, String localName) {
		return parent.getChildByLocalName(localName);
	}
	
	protected XmlNode getRootNode() {
		return dom.getRootNode();
	}
	
	protected XmlDOM dom;
	
	private T build(XmlNode node)
		throws Exception {
		
		dom = new XmlDOM(node);
		return build();
	}
}
