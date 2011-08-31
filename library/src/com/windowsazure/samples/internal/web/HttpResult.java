package com.windowsazure.samples.internal.web;

import com.windowsazure.samples.HttpStatusCode;


public class HttpResult {
	
	public HttpResult(HttpMethod method, HttpStatusCode statusCode, HttpHeader headers, String body) {
		this.method = method;
		this.statusCode = statusCode;
		this.headers = headers;
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}
	
	public HttpHeader getHeaders() {
		return headers;
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	
	private String body;
	private HttpHeader headers;
	private HttpMethod method;
	private HttpStatusCode statusCode;
}
