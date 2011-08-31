package com.windowsazure.samples.internal.xml;


public final class XmlAttribute extends XmlElement {

	public XmlAttribute(String tag, String name, String value) {
		super(tag, name);
		this.value = value;
	}
	
	public String getRepresentation() {
		return getFullName() + "='" + value + "'";
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	private String value;
}
