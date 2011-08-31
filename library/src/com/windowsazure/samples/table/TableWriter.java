package com.windowsazure.samples.table;

import java.util.Date;

import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.table.IfMatch;

public interface TableWriter {
	public AzureTable createTable(String title, Date updated, String authorName, String tableName);
	public TableOperationResponse deleteEntity(String tableName, String partitionKey, String rowKey, IfMatch match);
	public TableOperationResponse deleteTable(String tableName);
	public AzureTableEntity insertEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties);
	public TableOperationResponse mergeEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties, IfMatch match);
	public TableOperationResponse updateEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties, IfMatch match);
}
