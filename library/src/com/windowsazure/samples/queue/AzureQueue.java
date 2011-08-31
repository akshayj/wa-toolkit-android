package com.windowsazure.samples.queue;

import com.windowsazure.samples.MetadataCollection;


public final class AzureQueue {
	
	public AzureQueue(String queueName, String url, MetadataCollection metadata) {
		this.queueName = queueName;
		this.url = url;
		this.metadata = metadata;
	}
	
	public MetadataCollection getMetadata() {
		return metadata;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	public String getUrl() {
		return url;
	}
	
	private MetadataCollection metadata;
	private String queueName;
	private String url;
}
