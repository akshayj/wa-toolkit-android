package com.windowsazure.samples.blob;


public class IllegalBlobRangeException extends Exception {

	public IllegalBlobRangeException() {
		super("Blob ranges must be on 512 byte boundaries.");
	}
	
	private static final long serialVersionUID = 4432977327033181074L;
}
