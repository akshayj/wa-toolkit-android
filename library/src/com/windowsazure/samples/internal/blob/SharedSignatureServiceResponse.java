package com.windowsazure.samples.internal.blob;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.HttpUri;


public class SharedSignatureServiceResponse extends EntityBase {

	public SharedSignatureServiceResponse() {
		this(null, new CloudBlobCollection());
	}
	
	public SharedSignatureServiceResponse(String anyUrl, CloudBlobCollection blobCollection) {
		this.anyUrl = anyUrl;
		this.blobCollection = blobCollection;
	}
	
	public HttpUri getAnyUri() {
		String path = anyUrl.substring(anyUrl.indexOf("/usercontainer"));
		return new HttpUri(path);
	}
	
	public CloudBlobCollection getBlobCollection() {
		return blobCollection;
	}
	
	public String getBlobHost() {
		String url = anyUrl;
		if (Util.isStringNullOrEmpty(url))
			url = blobCollection.iterator().next().getUrl();
		int startIndex = 2 + url.indexOf("//");
		String host = url.substring(startIndex);
		int endIndex = host.indexOf('/');
		host = host.substring(0, endIndex);
		return host;
	}
	
	private String anyUrl;
	private CloudBlobCollection blobCollection;
}
