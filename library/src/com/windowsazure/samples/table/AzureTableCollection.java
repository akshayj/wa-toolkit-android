package com.windowsazure.samples.table;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;

public final class AzureTableCollection extends EntityBase implements Iterable<AzureTable> {

	public AzureTableCollection() {
		this(
		    null, null, null, null,
		    null, null, null, new Vector<AzureTable>());
	}
	
	public AzureTableCollection(
			String continuation, String requestId, String version, Date date,
			String id, String title, Date updated, Collection<AzureTable> tables) {
		
		this.continuation = continuation;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.id = id;
		this.title = title;
		this.updated = updated;
		this.tables = tables;
	}
	
	public String getContinuation() {
		return continuation;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getId() {
		return id;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public Collection<AzureTable> getTables() {
		return tables;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getUpdated() {
		return updated;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public Iterator<AzureTable> iterator() {
		return tables.iterator();
	}
	
	private String continuation;
	private Date date;
	private String id;
	private String requestId;
	private Collection<AzureTable> tables;
	private String title;
	private Date updated;
	private String version;
}
