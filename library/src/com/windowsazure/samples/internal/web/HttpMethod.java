package com.windowsazure.samples.internal.web;

public enum HttpMethod {
	DELETE,
	GET,
	HEAD,
	MERGE,
	POST,
	PUT;
	
	public boolean hasContent() {
		switch (this) {
		case MERGE:
		case POST:
		case PUT:
			return true;
		default:
			return false;
		}
	}
}
