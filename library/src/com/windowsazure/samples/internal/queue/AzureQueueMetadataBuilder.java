package com.windowsazure.samples.internal.queue;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;
import com.windowsazure.samples.queue.AzureQueueMetadata;

public class AzureQueueMetadataBuilder implements Builder<AzureQueueMetadata> {

	public AzureQueueMetadataBuilder(HttpResult httpResult) {
		this.httpResult = httpResult;
	}
	
	@Override
	public AzureQueueMetadata build()
		throws Exception {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureQueueMetadata metadata = new AzureQueueMetadata();
			metadata.setHttpStatusCode(statusCode);
			return metadata;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		
		String approximateMessageCountText = headers.get(X_MS_APPROXIMATE_MESSAGE_COUNT);
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		
		int approximateMessageCount = Integer.parseInt(approximateMessageCountText);
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		MetadataCollection metadata = new MetadataCollection();
		for (String headerName : headers.keySet()) {
			if (headerName.indexOf(AzureHttpHeader.XMS_META_PREFIX) != -1) {
				String value = headers.get(headerName);
				metadata.add(headerName.substring(AzureHttpHeader.XMS_META_PREFIX.length()), value);
			}
		}
		
		AzureQueueMetadata result = new AzureQueueMetadata(approximateMessageCount, metadata, requestId, version, date);
		result.setHttpStatusCode(statusCode);
		return result;
	}
	
	private static final String X_MS_APPROXIMATE_MESSAGE_COUNT = "x-ms-approximate-messages-count";
	
	private HttpResult httpResult;
}
