package com.windowsazure.samples;

import com.windowsazure.samples.Metadata;


public final class Metadata {

	public static Metadata buildInvalid(String invalidName) {
		return new Metadata(false, invalidName, null);
	}
	
	public static Metadata buildValid(String validName, String value) {
		return new Metadata(true, validName, value);
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	private Metadata(boolean isValid, String name, String value) {
		this.isValid = isValid;
		this.name = name;
		this.value = value;
	}
	
	private String name;
	private boolean isValid;
	private String value;
}
