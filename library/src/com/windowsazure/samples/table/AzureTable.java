package com.windowsazure.samples.table;

import java.util.Date;

import com.windowsazure.samples.EntityBase;

public final class AzureTable extends EntityBase {
	
	public AzureTable() {}
	
	public AzureTable(
			String requestId, String version, Date date,
			String id, String title, Date updated,
			String authorName, String tableName) {
		
		this.requestId = requestId;
		this.version = version;
		this.date = date;
		
		this.id = id;
		this.title = title;
		this.updated = updated;
		
		this.authorName = authorName;
		this.tableName = tableName;
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
	
	private String authorName;
	private Date date;
	private String id;
	private String requestId;
	private String tableName;
	private String title;
	private Date updated;
	private String version;
}
