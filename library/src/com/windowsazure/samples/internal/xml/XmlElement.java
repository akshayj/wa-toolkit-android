package com.windowsazure.samples.internal.xml;

import com.windowsazure.samples.internal.util.Util;


public abstract class XmlElement {

	public String getFullName() {
		return Util.isStringNullOrEmpty(tag) ? name : tag + ":" + name;
	}
	
	public String getLocalName() {
		return name;
	}
	
	protected XmlElement(String tag, String name) {
		this.tag = tag;
		this.name = name;
	}
	
	protected String getTag() {
		return tag;
	}
	
	private String name;
	private String tag;
}
