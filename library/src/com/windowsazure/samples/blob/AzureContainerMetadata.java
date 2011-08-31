package com.windowsazure.samples.blob;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.MetadataCollection;

public class AzureContainerMetadata extends EntityBase {

	public AzureContainerMetadata() {
		this(new MetadataCollection(), null, null, null, null, null);
	}
	
	public AzureContainerMetadata(MetadataCollection metadata, String etag, Date lastModified, String requestId, String version, Date date) {
		this.metadata = metadata;
		this.etag = etag;
		this.lastModified = lastModified;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getEtag() {
		return etag;
	}
	
	public Date getLastModfied() {
		return lastModified;
	}
	
	public MetadataCollection getMetatadata() {
		return metadata;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	private String containerName;
	private Date date;
	private String etag;
	private Date lastModified;
	private MetadataCollection metadata;
	private String requestId;
	private String version;
}
