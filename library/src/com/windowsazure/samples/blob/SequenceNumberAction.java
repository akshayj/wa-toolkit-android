package com.windowsazure.samples.blob;

public enum SequenceNumberAction {
	INCREMENT,
	MAX,
	UNPDATE;
	
	@Override 
	public String toString() {
		return super.toString().toLowerCase();
	}
}
