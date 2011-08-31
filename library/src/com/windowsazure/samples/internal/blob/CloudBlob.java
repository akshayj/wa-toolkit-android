package com.windowsazure.samples.internal.blob;


public class CloudBlob {

	public CloudBlob(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	private String name;
	private String url;
}
