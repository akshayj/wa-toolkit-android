package com.windowsazure.samples.internal;

import com.windowsazure.samples.authentication.AuthenticationToken;


public class OperationNotSupportedException extends Exception {
	
	public OperationNotSupportedException() {
		super("This operation is not supported.");
	}
	
	public OperationNotSupportedException(AzureOperation operation, AzureStrategy<? extends AuthenticationToken> strategy) {
		super("Operation" + operation + " is not supported by strategy " + strategy.getClass().toString());
	}
	
	private static final long serialVersionUID = -2010591407052857763L;
}
