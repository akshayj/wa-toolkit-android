package com.windowsazure.samples.blob.data;

import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.internal.util.Base64;


public class OctetBlobData extends BlobData {

	public static OctetBlobData fromBlob(AzureBlob blob) {
		OctetBlobData blobData = new OctetBlobData();
		blobData.cacheControl = blob.getCacheControl();
		blobData.contentEncoding = blob.getContentEncoding();
		blobData.contentLanguage = blob.getContentLanguage();
		blobData.contentMd5 = blob.getContentMd5();
		blobData.contentType = blob.getContentType();
		blobData.decode(blob.getServerData());
		return blobData;
	}
	
	public OctetBlobData() {
		this(new byte[0]);
	}
	
	public OctetBlobData(String text) {
		this(text.getBytes());
	}
	
	public OctetBlobData(byte[] data) {
		this.contentEncoding = "base64";
		this.contentType = "application/octet-stream";
		this.data = data;
	}
	
	public String getAsString() {
		return new String(data);
	}
	
	public byte[] getBytes() {
		return data;
	}
	
	@Override
	public Integer GetContentLength() {
		return Base64.encode(data).length;
	}
	
	@Override
	public String toString() {
		return new String(data);
	}
	
	@Override
	public void decode(String serverData) {
		this.data = Base64.decode(serverData.getBytes());
	}

	@Override
	public String encode() {
		return new String(Base64.encode(data));
	}

	private byte[] data;
}
