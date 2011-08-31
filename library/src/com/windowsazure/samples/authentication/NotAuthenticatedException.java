package com.windowsazure.samples.authentication;

public final class NotAuthenticatedException extends Exception {

	public NotAuthenticatedException() {
		super("Not Authenticated");
	}
	
	private static final long serialVersionUID = -3003542060680274345L;
}
