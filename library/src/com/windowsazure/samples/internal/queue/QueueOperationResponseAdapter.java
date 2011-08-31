package com.windowsazure.samples.internal.queue;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;
import com.windowsazure.samples.queue.QueueOperationResponse;


public final class QueueOperationResponseAdapter implements Builder<QueueOperationResponse> {

	public QueueOperationResponseAdapter(HttpResult httpResult) {
		this.httpResult = httpResult;
	}
	
	@Override
	public QueueOperationResponse build() {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			QueueOperationResponse response = new QueueOperationResponse();
			response.setHttpStatusCode(statusCode);
			return response;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateString = headers.get(HttpHeader.DATE);
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateString);} catch (Exception e) {}
		
		QueueOperationResponse response = new QueueOperationResponse(requestId, version, date);
		response.setHttpStatusCode(statusCode);
		return response;
	}
	
	private HttpResult httpResult;
}
