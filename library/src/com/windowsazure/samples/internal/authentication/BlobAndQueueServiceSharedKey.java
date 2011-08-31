package com.windowsazure.samples.internal.authentication;


public final class BlobAndQueueServiceSharedKey extends SharedKey {

	public BlobAndQueueServiceSharedKey setCanonicalizedHeaders(String canonicalizedHeaders) {
		this.canonicalizedHeaders = canonicalizedHeaders;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setCanonicalizedResource(String canonicalizedResource) {
		this.canonicalizedResource = canonicalizedResource;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setContentLength(String contentLength) {
		this.contentLength = contentLength;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setDateString(String dateString) {
		this.dateString = dateString;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setIfMatch(String etag) {
		this.ifMatch = etag;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setIfModifiedSince(String dateString) {
		this.ifModifiedSince = dateString;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setIfNoneMatch(String etag) {
		this.ifNoneMatch = etag;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setIfUnmodifiedSince(String dateString) {
		this.ifUnmodifiedSince = dateString;
		return this;
	}
	
	public BlobAndQueueServiceSharedKey setVerb(String verb) {
		this.verb = verb;
		return this;
	}
	
	@Override
	protected String getStringToSign() {
		String stringToSign =
			verb + "\n" +
			contentEncoding + "\n" +
			contentLanguage + "\n" + 
			contentLength + "\n" + 
			contentMd5 + "\n" +
			contentType + "\n" +
			dateString + "\n" +
			ifModifiedSince + "\n" +
			ifMatch + "\n" +
			ifNoneMatch + "\n" +
			ifUnmodifiedSince + "\n" +
			range + "\n" +
			canonicalizedHeaders +
			canonicalizedResource;
		return stringToSign;
	}

	private String canonicalizedHeaders = "";
	private String canonicalizedResource = "";
	private String contentEncoding = "";
	private String contentLanguage = "";
	private String contentLength = "";
	private String contentMd5 = "";
	private String contentType = "";
	private String dateString = "";
	private String ifMatch = "";
	private String ifModifiedSince = "";
	private String ifNoneMatch = "";
	private String ifUnmodifiedSince = "";
	private String range = "";
	private String verb = "";
}
