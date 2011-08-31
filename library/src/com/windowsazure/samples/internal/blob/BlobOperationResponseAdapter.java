package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.BlobOperationResponse;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.LeaseStatus;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;


public final class BlobOperationResponseAdapter implements Builder<BlobOperationResponse> {

	public BlobOperationResponseAdapter(HttpResult httpResult) {
		this.httpResult = httpResult;
	}
	
	@Override
	public BlobOperationResponse build() {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			BlobOperationResponse response = new BlobOperationResponse();
			response.setHttpStatusCode(statusCode);
			return response;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		String lastModifiedText = headers.get("Last-Modified");
		String blobTypeText = headers.get("x-ms-blob-type");
		String leaseId = headers.get("x-ms-lease-id");
		String leaseStatusText = headers.get("x-ms-lease-status");
		String leaseTimeText = headers.get("x-ms-lease-time");
		String contentLengthText = headers.get(HttpHeader.CONTENT_LENGTH);
		String contentType = headers.get(HttpHeader.CONTENT_TYPE);
		String etag = headers.get("ETag");
		String contentMd5 = headers.get(HttpHeader.CONTENT_MD5);
		String contentEncoding = headers.get(HttpHeader.CONTENT_ENCODING);
		String contentLanguage = headers.get(HttpHeader.CONTENT_LANGUAGE);
		String cacheControl = headers.get(HttpHeader.CACHE_CONTROL);
		String blobSequenceNumber = headers.get("x-ms-blob-sequence-number");
		String snapshot = headers.get("x-ms-snapshot");
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateString = headers.get(HttpHeader.DATE);
		
		MetadataCollection metadata = new MetadataCollection();
		for (String headerName : headers.keySet()) {
			if (headerName.indexOf(AzureHttpHeader.XMS_META_PREFIX) != -1) {
				String value = headers.get(headerName);
				metadata.add(headerName.substring(AzureHttpHeader.XMS_META_PREFIX.length()), value);
			}
		}
		
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		
		BlobType blobType = BlobType.fromRepresentation(blobTypeText);
		LeaseStatus leaseStatus = LeaseStatus.fromString(leaseStatusText);
		
		Integer leaseTime = null;
		try {leaseTime = Integer.parseInt(leaseTimeText);} catch (Exception e) {}
		
		Integer contentLength = null;
		try {contentLength = Integer.parseInt(contentLengthText);} catch (Exception e) {}
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateString);} catch (Exception e) {}
		
		BlobOperationResponse response = new BlobOperationResponse(
				lastModified,
				metadata,
				blobType,
				leaseId,
				leaseStatus,
				leaseTime,
				contentLength,
				contentType,
				etag,
				contentMd5,
				contentEncoding,
				contentLanguage,
				cacheControl,
				blobSequenceNumber,
				snapshot,
				requestId,
				version,
				date);
		response.setHttpStatusCode(statusCode);
		return response;
	}
	
	private HttpResult httpResult;
}
