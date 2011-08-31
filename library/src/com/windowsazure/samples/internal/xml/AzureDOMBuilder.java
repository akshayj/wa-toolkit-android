package com.windowsazure.samples.internal.xml;


public abstract class AzureDOMBuilder extends DOMBuilder {

	public static final String ATOM_NS = "http://www.w3.org/2005/Atom";
	public static final String DATA_SERVICES_NS = "http://schemas.microsoft.com/ado/2007/08/dataservices";
	public static final String METADATA_NS = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
	
	@Override
	protected XmlNode addRootNode(String tag, String name) {
		XmlNode rootNode = super.addRootNode(tag, name);
		addHeaderAttribute("version", "1.0");
		addHeaderAttribute("encoding", "utf-8");
		addHeaderAttribute("standalone", "yes");
		return rootNode;
	}
}
