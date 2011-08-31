package com.windowsazure.samples.internal.xml;

import java.util.Collection;
import java.util.Vector;

import com.windowsazure.samples.internal.util.Util;


public final class XPath {

	public static Collection<XmlNode> getNodes(XmlNode parent, String xpath) {
		assert(! Util.isStringNullOrEmpty(xpath));
		Vector<XmlNode> nodes = new Vector<XmlNode>();
		
		if (xpath.equals("/")) {
			nodes.add(parent);
			return nodes;
		}
		
		assert(xpath.length() >= 2);
		if (xpath.indexOf("//") == 0) {
			addNonAnchoredNodes(xpath, parent, nodes);
			return nodes;
		}
		
		String[] parts = xpath.substring(1).split("/");
		addAnchoredNodes(parts, 0, parent, nodes);
		return nodes;
	}
	
	public static XmlNode getSingleNode(XmlNode parent, String xpath) {
		Collection<XmlNode> nodes = getNodes(parent, xpath);
		return (nodes.size() > 0) ? nodes.iterator().next() : null;
	}
	
	private static void addAnchoredNodes(String[] parts, int index, XmlNode current, Vector<XmlNode> nodes) {
		if (! current.getLocalName().equals(parts[index]))
			return;
		
		if (index >= parts.length - 1) {
			nodes.add(current);
			return;
		}
		
		++index;
		for (XmlNode child : current.getChildren()) {
			addAnchoredNodes(parts, index, child, nodes);
		}
	}
	
	private static void addNonAnchoredNodes(String xpath, XmlNode parent, Vector<XmlNode> nodes) {
		String[] parts = xpath.substring(2).split("/");
		for (XmlNode descendent : parent.getDescendents()) {
			addAnchoredNodes(parts, 0, descendent, nodes);
		}
	}
	
	private XPath() {}
}
