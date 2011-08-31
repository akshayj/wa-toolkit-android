package com.windowsazure.samples.table;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.PropertyCollection;


public final class AzureTableEntity extends EntityBase {

	public AzureTableEntity() {
		this(
			null, null, null,
		    null, null, null, null,
		    null, null, null,
		    new PropertyCollection());
	}
	
	public AzureTableEntity(
			String requestId, String version, Date date,
			String id, String title, Date updated, String authorName,
			String tableName, String partitionKey, String rowKey,
			PropertyCollection properties) {
		
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.id = id;
		this.title = title;
		this.updated = updated;
		this.authorName = authorName;
		
		this.tableName = tableName;
		this.partitionKey = partitionKey;
		this.rowKey = rowKey;
		
		this.properties = properties;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPartitionKey() {
		return partitionKey;
	}
	
	public PropertyCollection getProperties() {
		return properties;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getRowKey() {
		return rowKey;
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
	
	private String authorName;
	private Date date;
	private String id;
	private String partitionKey;
	private PropertyCollection properties;
	private String requestId;
	private String rowKey;
	private String tableName;
	private String title;
	private Date updated;
	private String version;
}
