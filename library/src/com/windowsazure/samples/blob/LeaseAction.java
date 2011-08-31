package com.windowsazure.samples.blob;

public enum LeaseAction {
	ACQUIRE,
	BREAK,
	RELEASE,
	RENEW;
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
