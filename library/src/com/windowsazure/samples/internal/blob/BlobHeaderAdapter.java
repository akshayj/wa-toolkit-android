package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.LeaseStatus;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;


public class BlobHeaderAdapter implements Builder<AzureBlob> {
	
	public BlobHeaderAdapter(HttpResult httpResult) {
		this.httpResult = httpResult;
	}

	@Override
	public AzureBlob build() {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! httpResult.getStatusCode().isSuccess()) {
			AzureBlob result = new AzureBlob(null);
			result.setHttpStatusCode(statusCode);
			return result;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		String lastModifiedText = headers.get("Last-Modified");
		String contentLengthText = headers.get(HttpHeader.CONTENT_LENGTH);
		String contentType = headers.get(HttpHeader.CONTENT_TYPE);
		String contentRange = headers.get("Content-Range");
		String etag = headers.get("ETag");
		String contentMd5 = headers.get(HttpHeader.CONTENT_MD5);
		String contentEncoding = headers.get(HttpHeader.CONTENT_ENCODING);
		String contentLanguage = headers.get(HttpHeader.CONTENT_LANGUAGE);
		String cacheControl = headers.get(HttpHeader.CACHE_CONTROL);
		String blobSequenceNumberText = headers.get("x-ms-blob-sequence-number");
		String blobTypeText = headers.get("x-ms-blob-type");
		String leaseStatusText = headers.get("x-ms-lease-status");
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		
		Integer contentLength = null;
		try {contentLength = Integer.parseInt(contentLengthText);} catch(Exception e) {}
		
		BlobRange blobRange = null;
		try {blobRange = BlobRange.fromString(contentRange);} catch (Exception e) {}
		
		Integer blobSequenceNumber = null;
		try {blobSequenceNumber = Integer.parseInt(blobSequenceNumberText);} catch (Exception e) {}
		
		BlobType blobType = BlobType.fromRepresentation(blobTypeText);
		LeaseStatus leaseStatus = LeaseStatus.fromString(leaseStatusText);
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		MetadataCollection metadata = new MetadataCollection();
		for (String headerName : headers.keySet()) {
			if (headerName.indexOf(AzureHttpHeader.XMS_META_PREFIX) != -1) {
				String value = headers.get(headerName);
				metadata.add(headerName.substring(AzureHttpHeader.XMS_META_PREFIX.length()), value);
			}
		}
		
		AzureBlobSetter setter = new AzureBlobSetter()
			.setBlobSequenceNumber(blobSequenceNumber)
			.setBlobType(blobType)
			.setCacheControl(cacheControl)
			.setContentEncoding(contentEncoding)
			.setContentLanguage(contentLanguage)
			.setContentLength(contentLength)
			.setContentMd5(contentMd5)
			.setContentType(contentType)
			.setDate(date)
			.setEtag(etag)
			.setLastModified(lastModified)
			.setLeaseStatus(leaseStatus)
			.setMetadata(metadata)
			.setRange(blobRange)
			.setRequestId(requestId)
			.setServerData(httpResult.getBody())
			.setVersion(version);
		
		AzureBlob blob =  new AzureBlob(setter);
		blob.setHttpStatusCode(statusCode);
		return blob;
	}

	private HttpResult httpResult;
}
