package com.windowsazure.samples.queue;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public final class AzureQueueMessageCollection extends EntityBase implements Iterable<AzureQueueMessage> {

	public AzureQueueMessageCollection() {
		this(null, null, null, new Vector<AzureQueueMessage>());
	}
	
	public AzureQueueMessageCollection(String requestId, String version, Date date, Collection<AzureQueueMessage> messages) {
		this.messages = messages;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Collection<AzureQueueMessage> getMessages() {
		return messages;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<AzureQueueMessage> iterator() {
		return messages.iterator();
	}
	
	private Date date;
	private Collection<AzureQueueMessage> messages;
	private String requestId;
	private String version;
}
