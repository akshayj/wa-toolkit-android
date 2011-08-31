package com.windowsazure.samples.internal.xml;

import com.windowsazure.samples.internal.util.Util;


public class XmlNamespace extends XmlElement {

	public XmlNamespace(String tag, String uri) {
		super(tag, null);
		this.uri = uri;
	}
	
	public String getRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("xmlns");
		
		if (! isDefault()) {
			sb.append(":" + getTag());
		}
		
		sb.append("='" + uri + "'");
		return sb.toString();
	}
	
	public String getUri() {
		return uri;
	}
	
	public boolean isDefault() {
		return Util.isStringNullOrEmpty(getTag());
	}
	
	private String uri;
}
