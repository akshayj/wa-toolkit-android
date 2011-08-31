package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.AzureContainerMetadata;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;

public class AzureContainerMetadataBuilder implements Builder<AzureContainerMetadata> {

	public AzureContainerMetadataBuilder(HttpResult httpResult) {
		this.httpResult = httpResult;
	}
	
	@Override
	public AzureContainerMetadata build()
		throws Exception {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureContainerMetadata metadata = new AzureContainerMetadata();
			metadata.setHttpStatusCode(statusCode);
			return metadata;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		
		MetadataCollection metadata = new MetadataCollection();
		for (String headerName : headers.keySet()) {
			if (headerName.indexOf(AzureHttpHeader.XMS_META_PREFIX) != -1) {
				String value = headers.get(headerName);
				metadata.add(headerName.substring(AzureHttpHeader.XMS_META_PREFIX.length()), value);
			}
		}
		
		String etag = headers.get(ETAG);
		String lastModifiedText = headers.get(LAST_MODIFIED);
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		
		Date lastModified = null;
		try {lastModified = Util.gmtFormatToDate(lastModifiedText);} catch (Exception e) {}
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		AzureContainerMetadata result = new AzureContainerMetadata(metadata, etag, lastModified, requestId, version, date);
		result.setHttpStatusCode(statusCode);
		return result;
	}
	
	private static final String ETAG = "Etag";
	private static final String LAST_MODIFIED = "Last-Modified";
	
	private HttpResult httpResult;
}
