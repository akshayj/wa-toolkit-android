package com.windowsazure.samples.table;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;


public final class AzureTableEntityCollection extends EntityBase implements Iterable<AzureTableEntity> {

	public AzureTableEntityCollection() {
		this(
			null, null, null, null, null,
		    null, null, null,
		    null, new Vector<AzureTableEntity>());
	}
	
	public AzureTableEntityCollection(
			String nextPartitionKey, String nextRowKey, String requestId, String version, Date date,
			String title, String id, Date updated,
			String tableName, Collection<AzureTableEntity> entities) {
		
		this.nextPartitionKey = nextPartitionKey;
		this.nextRowKey = nextRowKey;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.title = title;
		this.id = id;
		this.updated = updated;
		
		this.tableName = tableName;
		this.entities = entities;
	}
	
	public Collection<AzureTableEntity> getEntities() {
		return entities;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNextPartitionKey() {
		return nextPartitionKey;
	}
	
	public String getNextRowKey() {
		return nextRowKey;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getTableName() {
		return tableName;
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
	public Iterator<AzureTableEntity> iterator() {
		return entities.iterator();
	}
	
	private Date date;
	private Collection<AzureTableEntity> entities;
	private String id;
	private String nextPartitionKey;
	private String nextRowKey;
	private String requestId;
	private String tableName;
	private String title;
	private Date updated;
	private String version;
}
