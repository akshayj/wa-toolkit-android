package com.windowsazure.samples.queue;

public interface QueueReader {
	public AzureQueueMetadata getQueueMetadata(String queueName);
	public AzureQueueCollection listAllQueues();
	public AzureQueueCollection listQueues(String prefix, String marker, Integer maxResults);
	
	// numberOfMessages: Optional.  A nonzero integer value that specifies the number of messages to
	// retrieve from the queue, up to a maximum of 32.  If null, a single message is retrieved from the queue.
	//
	// visibilityTimeout: Optional.  An integer value that specifies the message's visibility timeout in seconds.
	// The maximum value is 2 hours.  If null, the message visibility timeout is 30 seconds.
	public AzureQueueMessageCollection getMessages(String queueName, Integer numberOfMessages, Integer visibilityTimeout);
	
	// numberOfMessages: Optional.  A nonzero integer value that specifies the number of messages to
	// peek from the queue, up to a maximum of 32.  If null, a single message is peeked from the queue.
	public AzureQueueMessageCollection peekMessages(String queueName, Integer numberOfMessages);
}
