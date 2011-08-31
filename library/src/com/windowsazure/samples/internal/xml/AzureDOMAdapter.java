package com.windowsazure.samples.internal.xml;

import com.windowsazure.samples.internal.web.XmlHttpResult;


public abstract class AzureDOMAdapter<T> extends DOMAdapter<T> {
	
	public AzureDOMAdapter(XmlHttpResult xmlHttpResult) {
		super(xmlHttpResult.getXmlString());
		this.xmlHttpResult = xmlHttpResult;
	}
	
	public abstract T build() throws Exception;
	
	protected AzureDOMAdapter(XmlNode node) {
		super(node);
	}
	
	protected String extractTableNameFromEntityId(String idText) {
		int beginIndex = idText.lastIndexOf('/') + 1;
		int endIndex = idText.indexOf('(', beginIndex);
		if (endIndex == -1)
			endIndex = idText.length();
		return idText.substring(beginIndex, endIndex);
	}
	
	protected String extractTableNameFromTableId(String idText) {
		int beginIndex = idText.indexOf('\'') + 1;
		int endIndex = idText.indexOf('\'', beginIndex);
		return idText.substring(beginIndex, endIndex);
	}
	
	protected String getErrorCode()
		throws NodeNotFoundException {
		
		XmlNode errorNode = getRootNode();
		if (errorNode.getLocalName().compareTo("error") != 0)
			return null;
		
		return getInnerText(errorNode, "code");
	}
	
	protected XmlHttpResult xmlHttpResult;
}
