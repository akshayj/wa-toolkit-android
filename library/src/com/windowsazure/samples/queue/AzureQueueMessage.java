package com.windowsazure.samples.queue;

import java.util.Date;

public class AzureQueueMessage {

	public AzureQueueMessage() {
		this(null, null, null, null, null, null, null);
	}
	
	public AzureQueueMessage(
			String messageId,
			Date insertionTime,
			Date expirationTime,
			String popReceipt,
			Date timeNextVisible,
			Integer dequeueCount,
			String messageText) {
		this.messageId = messageId;
		this.insertionTime = insertionTime;
		this.expirationTime = expirationTime;
		this.popReceipt = popReceipt;
		this.timeNextVisible = timeNextVisible;
		this.dequeueCount = dequeueCount;
		this.messageText = messageText;
	}
	
	public Integer getDequeueCount() {
		return dequeueCount;
	}
	
	public Date getExpirationTime() {
		return expirationTime;
	}
	
	public Date getInsertionTime() {
		return insertionTime;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public String getMessageText() {
		return messageText;
	}
	
	public String getPopReceipt() {
		return popReceipt;
	}
	
	public Date getTimeNextVisible() {
		return timeNextVisible;
	}
	
	private Integer dequeueCount;
	private Date expirationTime;
	private Date insertionTime;
	private String messageId;
	private String messageText;
	private String popReceipt;
	private Date timeNextVisible;
}
