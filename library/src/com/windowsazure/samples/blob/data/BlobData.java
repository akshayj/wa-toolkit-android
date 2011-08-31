package com.windowsazure.samples.blob.data;


public abstract class BlobData {

	public String getCacheControl() {
		return cacheControl;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public String getContentLanguage() {
		return contentLanguage;
	}

	public String getContentMd5() {
		return contentMd5;
	}

	public abstract Integer GetContentLength();

	public String getContentType() {
		return contentType;
	}
	
	public abstract void decode(String serverData);
	public abstract String encode();

	protected String cacheControl;
	protected String contentEncoding;
	protected String contentLanguage;
	protected String contentMd5;
	protected String contentType;
}
