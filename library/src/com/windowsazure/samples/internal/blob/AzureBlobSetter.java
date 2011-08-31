package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.LeaseStatus;


public class AzureBlobSetter {

	public void clear() {
		blobName = null;
		blobSequenceNumber = null;
		blobType = null;
		cacheControl = null;
		contentEncoding = null;
		contentLanguage = null;
		contentLength = null;
		contentMd5 = null;
		contentType = null;
		date = null;
		etag = null;
		lastModified = null;
		leaseStatus = null;
		metadata = null;
		range = null;
		requestId = null;
		sequenceNumber = null;
		serverData = null;
		snapshot = null;
		url = null;
		version = null;
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
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	
	public String getServerData() {
		return serverData;
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
	
	public AzureBlobSetter setBlobName(String blobName) {
		this.blobName = blobName;
		return this;
	}
	
	public AzureBlobSetter setBlobSequenceNumber(Integer blobSequenceNumber) {
		this.blobSequenceNumber = blobSequenceNumber;
		return this;
	}
	
	public AzureBlobSetter setBlobType(BlobType blobType) {
		this.blobType = blobType;
		return this;
	}
	
	public AzureBlobSetter setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
		return this;
	}
	
	public AzureBlobSetter setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
		return this;
	}
	
	public AzureBlobSetter setContentLanguage(String contentLanguage) {
		this.contentLanguage = contentLanguage;
		return this;
	}
	
	public AzureBlobSetter setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
		return this;
	}
	
	public AzureBlobSetter setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
		return this;
	}
	
	public AzureBlobSetter setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	public AzureBlobSetter setDate(Date date) {
		this.date = date;
		return this;
	}
	
	public AzureBlobSetter setEtag(String etag) {
		this.etag = etag;
		return this;
	}
	
	public AzureBlobSetter setLastModified(Date lastModified) {
		this.lastModified = lastModified;
		return this;
	}
	
	public AzureBlobSetter setLeaseStatus(LeaseStatus leaseStatus) {
		this.leaseStatus = leaseStatus;
		return this;
	}
	
	public AzureBlobSetter setMetadata(MetadataCollection metadata) {
		this.metadata = metadata;
		return this;
	}
	
	public AzureBlobSetter setRange(BlobRange range) {
		this.range = range;
		return this;
	}
	
	public AzureBlobSetter setRequestId(String requestId) {
		this.requestId = requestId;
		return this;
	}
	
	public AzureBlobSetter setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		return this;
	}
	
	public AzureBlobSetter setServerData(String serverData) {
		this.serverData = serverData;
		return this;
	}
	
	public AzureBlobSetter setSnapshot(String snapshot) {
		this.snapshot = snapshot;
		return this;
	}
	
	public AzureBlobSetter setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public AzureBlobSetter setVersion(String version) {
		this.version = version;
		return this;
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
