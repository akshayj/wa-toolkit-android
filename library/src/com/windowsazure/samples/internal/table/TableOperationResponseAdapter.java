package com.windowsazure.samples.internal.table;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpResult;
import com.windowsazure.samples.table.TableOperationResponse;


public final class TableOperationResponseAdapter implements Builder<TableOperationResponse> {

	public TableOperationResponseAdapter(HttpResult httpResult) {
		this.httpResult = httpResult;
	}
	
	@Override
	public TableOperationResponse build() {
		
		HttpStatusCode statusCode = httpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			TableOperationResponse response = new TableOperationResponse();
			response.setHttpStatusCode(statusCode);
			return response;
		}
		
		HttpHeader headers = httpResult.getHeaders();
		String etag = headers.get("ETag");
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateString = headers.get(HttpHeader.DATE);
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateString);} catch (Exception e) {}
		
		TableOperationResponse response = new TableOperationResponse(etag, requestId, version, date);
		response.setHttpStatusCode(statusCode);
		return response;
	}
	
	private HttpResult httpResult;
}
