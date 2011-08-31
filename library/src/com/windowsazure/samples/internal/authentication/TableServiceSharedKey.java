package com.windowsazure.samples.internal.authentication;


public final class TableServiceSharedKey extends SharedKey {

	public TableServiceSharedKey setCanoncalizedResource(String canonocalizedResource) {
		this.canoncalizedResource = canonocalizedResource;
		return this;
	}
	
	public TableServiceSharedKey setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
		return this;
	}
	
	public TableServiceSharedKey setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	public TableServiceSharedKey setDateString(String dateString) {
		this.dateString = dateString;
		return this;
	}
	
	public TableServiceSharedKey setVerb(String verb) {
		this.verb = verb;
		return this;
	}
	
	@Override
	protected String getStringToSign() {
		String stringToSign =
			verb + "\n" +
			contentMd5 + "\n" +
			contentType + "\n" +
			dateString + "\n" +
			canoncalizedResource;
		return stringToSign;
	}
	
	private String canoncalizedResource = "";
	private String contentMd5 = "";
	private String contentType = "";
	private String dateString = "";
	private String verb = "";
}
