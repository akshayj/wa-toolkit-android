package com.windowsazure.samples.queue;

import com.windowsazure.samples.MetadataCollection;

public interface QueueWriter {
	public QueueOperationResponse createQueue(String queueName);
	public QueueOperationResponse createQueue(String queueName, MetadataCollection metadata);
	public QueueOperationResponse deleteQueue(String queueName);
	public QueueOperationResponse setQueueMetadata(String queueName, MetadataCollection metadata);
	
	// timeToLiveInterval: Optional. Specifies the time-to-live interval for the message, in seconds.
	// The maximum time-to-live allowed is 7 days. If this parameter is null, the default time-to-live is 7 days.
	public QueueOperationResponse putMessage(String queueName, String messageText, Integer timeToLiveInterval);
	
	public QueueOperationResponse deleteMessage(String queueName, String messageId, String popReceipt);
	public QueueOperationResponse clearMessages(String queueName);
}
