package com.windowsazure.samples.blob;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.internal.blob.AzureBlobSetter;
import com.windowsazure.samples.internal.util.Util;


public class AzureBlob extends EntityBase {

	public AzureBlob(AzureBlobSetter setter) {
		if (setter != null) {
			this.blobName = setter.getBlobName();
			this.blobSequenceNumber = setter.getBlobSequenceNumber();
			this.blobType = setter.getBlobType();
			this.cacheControl = setter.getCacheControl();
			this.contentEncoding = setter.getContentEncoding();
			this.contentLanguage = setter.getContentLanguage();
			this.contentLength = setter.getContentLength();
			this.contentMd5 = setter.getContentMd5();
			this.contentType = setter.getContentType();
			this.date = setter.getDate();
			this.etag = setter.getEtag();
			this.lastModified = setter.getLastModified();
			this.leaseStatus = setter.getLeaseStatus();
			this.metadata = setter.getMetadata();
			this.range = setter.getRange();
			this.requestId = setter.getRequestId();
			this.sequenceNumber = setter.getSequenceNumber();
			this.serverData = setter.getServerData();
			this.snapshot = setter.getSnapshot();
			this.url = setter.getUrl();
			this.version = setter.getVersion();
		}
	}
	
	public String getBlobName() {
		return blobName;
	}
	public Integer getBlobSequenceNumber() {
		return blobSequenceNumber;
	}
	
	public BlobType getBlobType() {
		return blobType;
	}
	
	public String getCacheControl() {
		return cacheControl;
	}
	
	public String getContentEncoding() {
		return contentEncoding;
	}
	
	public String getContentLanguage() {
		return contentLanguage;
	}
	
	public Integer getContentLength() {
		return contentLength;
	}
	
	public String getContentMd5() {
		return contentMd5;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getEtag() {
		return etag;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public LeaseStatus getLeaseStatus() {
		return leaseStatus;
	}
	
	public MetadataCollection getMetadata() {
		return metadata;
	}
	
	public BlobRange getRange() {
		return range;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getServerData() {
		return serverData;
	}
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	
	public String getSnapshot() {
		return snapshot;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean hasData() {
		return ! Util.isStringNullOrEmpty(serverData);
	}
	
	private String blobName;
	private Integer blobSequenceNumber;
	private BlobType blobType;
	private String cacheControl;
	private String contentEncoding;
	private String contentLanguage;
	private Integer contentLength;
	private String contentMd5;
	private String contentType;
	private Date date;
	private String etag;
	private Date lastModified;
	private LeaseStatus leaseStatus;
	private MetadataCollection metadata;
	private BlobRange range;
	private String requestId;
	private String sequenceNumber;
	private String serverData;
	private String snapshot;
	private String url;
	private String version;
}
