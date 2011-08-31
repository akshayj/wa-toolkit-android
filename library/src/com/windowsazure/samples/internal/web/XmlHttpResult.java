package com.windowsazure.samples.internal.web;

import com.windowsazure.samples.HttpStatusCode;


public class XmlHttpResult extends HttpResult {
	
	public XmlHttpResult(HttpMethod method, HttpStatusCode statusCode, HttpHeader headers, String body) {
		super(method, statusCode, headers, body);
	}
	
	public XmlHttpResult getEmptyResult() {
		return new XmlHttpResult(getMethod(), getStatusCode(), getHeaders(), null);
	}
	
	public String getXmlString() {
		return xmlString;
	}
	
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	
	private String xmlString;
}
