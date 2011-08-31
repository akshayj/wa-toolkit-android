package com.windowsazure.samples.internal.queue;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;


public final class QueueMessageCollectionDOMAdapter extends AzureDOMAdapter<AzureQueueMessageCollection> {

	public QueueMessageCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureQueueMessageCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureQueueMessageCollection collection = new AzureQueueMessageCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateString = headers.get(HttpHeader.DATE);
		Date date = null;
		try {date = Util.gmtFormatToDate(dateString);} catch (Exception e) {}
		
		Collection<AzureQueueMessage> messages =
			buildCollection(getNodeCollection("QueueMessage"), new QueueMessageDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		AzureQueueMessageCollection collection = new AzureQueueMessageCollection(requestId, version, date, messages);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
