package com.windowsazure.samples.internal.queue;

import java.util.Date;

import com.windowsazure.samples.internal.util.Base64;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.queue.AzureQueueMessage;


final class QueueMessageDOMAdapter extends AzureDOMAdapter<AzureQueueMessage> {

	@Override
	public AzureQueueMessage build()
		throws Exception {
		
		String messageId = getInnerText("MessageId");
		String insertionTimeText = getInnerText("InsertionTime");
		String expirationTimeText = getInnerText("ExpirationTime");
		String popReceipt = getOptionalInnerText("PopReceipt");
		String timeNextVisibleText = getOptionalInnerText("TimeNextVisible");
		String dequeueCountText = getOptionalInnerText("DequeueCount");
		String messageText = new String(Base64.decode(getInnerText("MessageText").getBytes()));
		
		Date insertionTime = Util.gmtFormatToDate(insertionTimeText);
		Date expirationTime = Util.gmtFormatToDate(expirationTimeText);
		Date timeNextVisible = Util.isStringNullOrEmpty(timeNextVisibleText) ?
				null : Util.gmtFormatToDate(timeNextVisibleText);
		Integer dequeueCount = Util.isStringNullOrEmpty(dequeueCountText) ? null : Integer.parseInt(dequeueCountText);
		
		return new AzureQueueMessage(messageId, insertionTime, expirationTime, popReceipt, timeNextVisible, dequeueCount, messageText);
	}
	
	protected QueueMessageDOMAdapter(XmlHttpResult result) {
		super(result);
	}
}
