package com.windowsazure.samples.blob;

import java.util.Date;

import com.windowsazure.samples.MetadataCollection;


public class AzureContainer {

	public AzureContainer() {
		this(null, null, null, null, new MetadataCollection());
	}
	
	public AzureContainer(String containerName, String url, Date lastModified, String etag, MetadataCollection metadata) {
		this.containerName = containerName;
		this.url = url;
		this.lastModified = lastModified;
		this.etag = etag;
		this.metadata = metadata;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public String getEtag() {
		return etag;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public MetadataCollection getMetadata() {
		return metadata;
	}
	
	public String getUrl() {
		return url;
	}
	
	private String containerName;
	private String etag;
	private Date lastModified;
	private MetadataCollection metadata;
	private String url;
}
