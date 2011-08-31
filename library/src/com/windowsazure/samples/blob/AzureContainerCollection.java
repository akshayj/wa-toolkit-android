package com.windowsazure.samples.blob;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public class AzureContainerCollection extends EntityBase implements Iterable<AzureContainer> {

	public AzureContainerCollection() {
		this(
			null, null, null,
		    null, null, null,
		    new Vector<AzureContainer>(), null);
	}
	
	public AzureContainerCollection(
			String requestId, String version, Date date,
			String prefix, String marker, Integer maxResults,
			Collection<AzureContainer> containers, String nextMarker) {
		
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.prefix = prefix;
		this.marker = marker;
		this.maxResults = maxResults;
		
		this.containers = containers;
		this.nextMarker = nextMarker;
	}
	
	public Collection<AzureContainer> getContainers() {
		return containers;
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
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<AzureContainer> iterator() {
		return containers.iterator();
	}

	private Collection<AzureContainer> containers;
	private Date date;
	private String marker;
	private Integer maxResults;
	private String nextMarker;
	private String prefix;
	private String requestId;
	private String version;
}
