package com.windowsazure.samples.blob;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.MetadataCollection;


public class BlobOperationResponse extends EntityBase {

	public BlobOperationResponse() {}
	
	public BlobOperationResponse(
			Date lastModified,
			MetadataCollection metadata,
			BlobType blobType,
			String leaseId,
			LeaseStatus leaseStatus,
			Integer leaseTime,
			Integer contentLength,
			String contentType,
			String etag,
			String contentMd5,
			String contentEncoding,
			String contentLanguage,
			String cacheControl,
			String blobSequenceNumber,
			String snapshot,
			String requestId,
			String version,
			Date date) {
		this.lastModified = lastModified;
		this.metadata = metadata;
		this.blobType = blobType;
		this.leaseId = leaseId;
		this.leaseStatus = leaseStatus;
		this.leaseTime = leaseTime;
		this.contentLength = contentLength;
		this.contentType = contentType;
		this.etag = etag;
		this.contentMd5 = contentMd5;
		this.contentEncoding = contentEncoding;
		this.contentLanguage = contentLanguage;
		this.cacheControl = cacheControl;
		this.blobSequenceNumber = blobSequenceNumber;
		this.snapshot = snapshot;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
	}
	
	public String getBlobSequenceNumber() {
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
	
	public String getLeaseId() {
		return leaseId;
	}
	
	public LeaseStatus getLeaseStatus() {
		return leaseStatus;
	}
	
	public Integer getLeaseTime() {
		return leaseTime;
	}
	
	public MetadataCollection getMetadata() {
		return metadata;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getSnapshot() {
		return snapshot;
	}
	
	public String getVersion() {
		return version;
	}
	
	private String blobSequenceNumber;
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
	private String leaseId;
	private LeaseStatus leaseStatus;
	private Integer leaseTime;
	private MetadataCollection metadata;
	private String requestId;
	private String snapshot;
	private String version;
}
