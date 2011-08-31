package com.windowsazure.samples.blob.data;

import com.windowsazure.samples.blob.AzureBlob;


public class TextBlobData extends BlobData {

	public static TextBlobData fromBlob(AzureBlob blob) {
		TextBlobData blobData = new TextBlobData();
		blobData.cacheControl = blob.getCacheControl();
		blobData.contentEncoding = blob.getContentEncoding();
		blobData.contentLanguage = blob.getContentLanguage();
		blobData.contentMd5 = blob.getContentMd5();
		blobData.contentType = blob.getContentType();
		blobData.decode(blob.getServerData());
		return blobData;
	}
	
	public TextBlobData() {
		this("");
	}
	
	public TextBlobData(String text) {
		this.contentType = "text/plain";
		this.text = text;
	}
	
	@Override
	public Integer GetContentLength() {
		return text.length();
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public void decode(String serverData) {
		this.text = serverData;
	}

	@Override
	public String encode() {
		return text;
	}

	private String text;
}
