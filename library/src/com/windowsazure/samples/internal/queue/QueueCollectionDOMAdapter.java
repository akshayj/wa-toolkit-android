package com.windowsazure.samples.internal.queue;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;
import com.windowsazure.samples.queue.AzureQueue;
import com.windowsazure.samples.queue.AzureQueueCollection;


public final class QueueCollectionDOMAdapter extends AzureDOMAdapter<AzureQueueCollection> {

	public QueueCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureQueueCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureQueueCollection collection = new AzureQueueCollection();
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
		
		String prefixText = getOptionalInnerText("Prefix");
		String markerText = getOptionalInnerText("Marker");
		String maxResultsText = getOptionalInnerText("MaxResults");
		
		XmlNode queuesNode = getNode("Queues");
		Collection<AzureQueue> queues =
			buildCollection(getNodeCollection(queuesNode, "Queue"), new QueueDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		String nextMarkerText = getInnerText("NextMarker");
		AzureQueueCollection collection = new AzureQueueCollection(
				requestId, version, date,
				prefixText, markerText, Util.isStringNullOrEmpty(maxResultsText) ? null : Integer.parseInt(maxResultsText),
				queues, nextMarkerText);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
