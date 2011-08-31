package com.windowsazure.samples.internal.blob;

import java.util.Date;
import java.util.Vector;

import com.windowsazure.samples.blob.AzureBlob;


public class AzureBlobCollectionSetter {
	
	public void clear() {
		blobPrefix = null;
		blobs = null;
		date = null;
		delimiter = null;
		marker = null;
		maxResults = null;
		nextMarker = null;
		prefix = null;
		requestId = null;
		version = null;
	}
	
	public String getBlobPrefix() {
		return blobPrefix;
	}
	
	public Vector<AzureBlob> getBlobs() {
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
	
	public AzureBlobCollectionSetter setBlobPrefix(String blobPrefix) {
		this.blobPrefix = blobPrefix;
		return this;
	}
	
	public AzureBlobCollectionSetter setBlobs(Vector<AzureBlob> blobs) {
		this.blobs = blobs;
		return this;
	}
	
	public AzureBlobCollectionSetter setDate(Date date) {
		this.date = date;
		return this;
	}
	
	public AzureBlobCollectionSetter setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}
	
	public AzureBlobCollectionSetter setMarker(String marker) {
		this.marker = marker;
		return this;
	}
	
	public AzureBlobCollectionSetter setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
		return this;
	}
	
	public AzureBlobCollectionSetter setNextMarker(String nextMarker) {
		this.nextMarker = nextMarker;
		return this;
	}
	
	public AzureBlobCollectionSetter setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public AzureBlobCollectionSetter setRequestId(String requestId) {
		this.requestId = requestId;
		return this;
	}
	
	public AzureBlobCollectionSetter setVersion(String version) {
		this.version = version;
		return this;
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
