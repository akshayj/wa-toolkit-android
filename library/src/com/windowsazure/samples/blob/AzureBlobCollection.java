package com.windowsazure.samples.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.internal.blob.AzureBlobCollectionSetter;


public class AzureBlobCollection extends EntityBase implements Iterable<AzureBlob> {

	public AzureBlobCollection(AzureBlobCollectionSetter setter) {
		if (setter != null) {
			this.blobPrefix = setter.getBlobPrefix();
			this.blobs = setter.getBlobs();
			this.date = setter.getDate();
			this.delimiter = setter.getDelimiter();
			this.marker = setter.getMarker();
			this.maxResults = setter.getMaxResults();
			this.nextMarker = setter.getNextMarker();
			this.prefix = setter.getPrefix();
			this.requestId = setter.getRequestId();
			this.version = setter.getVersion();
		}
	}
	
	public String getBlobPrefix() {
		return blobPrefix;
	}
	
	public Collection<AzureBlob> getBlobs() {
		return blobs;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public Integer getMaxResults() {
		return maxResults;
	}
	
	public String getNextMarker() {
		return nextMarker;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<AzureBlob> iterator() {
		return blobs.iterator();
	}
	
	private String blobPrefix;
	private Vector<AzureBlob> blobs;
	private Date date;
	private String delimiter;
	private String marker;
	private Integer maxResults;
	private String nextMarker;
	private String prefix;
	private String requestId;
	private String version;
}
