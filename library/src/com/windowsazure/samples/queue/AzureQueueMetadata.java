package com.windowsazure.samples.queue;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.MetadataCollection;

public class AzureQueueMetadata extends EntityBase {

	public AzureQueueMetadata() {
		this(null, new MetadataCollection(), null, null, null);
	}
	
	public AzureQueueMetadata(Integer approximateMessageCount, MetadataCollection metadata, String requestId, String version, Date date) {
		this.ApproximateMessageCount = approximateMessageCount;
		this.metadata = metadata;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
	}
	
	public Integer getApproximateMessageCount() {
		return ApproximateMessageCount;
	}
	
	public Date getDate() {
		return date;
	}
	
	public MetadataCollection getMetatadata() {
		return metadata;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	private Integer ApproximateMessageCount;
	private Date date;
	private MetadataCollection metadata;
	private String queueName;
	private String requestId;
	private String version;
}
