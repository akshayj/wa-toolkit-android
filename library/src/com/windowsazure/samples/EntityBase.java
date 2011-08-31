package com.windowsazure.samples;

import com.windowsazure.samples.HttpStatusCode;


public class EntityBase {
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public HttpStatusCode getHttpStatusCode() {
		return httpStatusCode;
	}
	
	public boolean isValid() {
		return (exception == null);
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public void setException(Exception e) {
		exception = e;
	}
	
	public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	
	public EntityBase() {}
	
	private String errorCode;
	private Exception exception;
	private HttpStatusCode httpStatusCode;
}
