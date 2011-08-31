package com.windowsazure.samples.internal;

import com.windowsazure.samples.blob.data.BlobData;


public class BlobDataContext extends BlobData {

	public BlobDataContext(
			String cacheControl,
			String contentEncoding,
			String contentLanguage,
			Integer contentLength,
			String contentMd5,
			String contentType) {
		this.cacheControl = cacheControl;
		this.contentEncoding = contentEncoding;
		this.contentLanguage = contentLanguage;
		this.contentLength = contentLength;
		this.contentMd5 = contentMd5;
		this.contentType = contentType;
	}
	
	@Override
	public Integer GetContentLength() {
		return contentLength;
	}

	@Override
	public void decode(String serverData) {
		// TODO Auto-generated method stub
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	private Integer contentLength;
}
