package com.windowsazure.samples.blob;

public enum PutPageAction {
	CLEAR,
	UPDATE;
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
