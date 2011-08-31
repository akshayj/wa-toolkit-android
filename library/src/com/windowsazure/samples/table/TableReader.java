package com.windowsazure.samples.table;


public interface TableReader {
	public AzureTableEntity getEntity(String tableName, String partitionKey, String rowKey);
	public AzureTableEntityCollection queryAllEntities(String tableName);
	public AzureTableEntityCollection queryEntities(String tableName, Filter filter, Integer top);
	public AzureTableCollection queryTables();
}
