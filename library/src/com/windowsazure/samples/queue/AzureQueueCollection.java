package com.windowsazure.samples.queue;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public final class AzureQueueCollection extends EntityBase implements Iterable<AzureQueue> {

	public AzureQueueCollection() {
		this(
			null, null, null,
		    null, null, null,
		    new Vector<AzureQueue>(), null);
	}
	
	public AzureQueueCollection(
			String requestId, String version, Date date,
			String prefix, String marker, Integer maxResults,
			Collection<AzureQueue> queues, String nextMarker) {
		
		this.prefix = prefix;
		this.marker = marker;
		this.maxResults = maxResults;
		this.queues = queues;
		this.nextMarker = nextMarker;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public Integer getMaxResults() {
		return maxResults;
	}
	
	public String getNextMarker() {
		return nextMarker;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public Collection<AzureQueue> getQueues() {
		return queues;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<AzureQueue> iterator() {
		return queues.iterator();
	}
	
	private Date date;
	private String marker;
	private Integer maxResults;
	private String nextMarker;
	private String prefix;
	private Collection<AzureQueue> queues;
	private String requestId;
	private String version;
}
