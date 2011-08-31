package com.windowsazure.samples.test;

import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.internal.table.IfMatch;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.table.AzureTableManager;
import com.windowsazure.samples.table.Filter;
import com.windowsazure.samples.table.TableReader;
import com.windowsazure.samples.table.TableWriter;

import android.test.AndroidTestCase;

public class TableTests extends AndroidTestCase {

	public void testCreateAndDeleteTable() {
		try
		{
			String tableName = generateTableName("TestCreateAndDeleteTable");
			AzureTable table = getWriter().createTable("Test Table", new Date(), AUTHOR, tableName);
			Assert.assertEquals(tableName, table.getTableName());
			
			table = getTable(tableName);
			Assert.assertNotNull(table);
			
			getWriter().deleteTable(tableName);
			table = getTable(tableName);
			Assert.assertNull(table);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteAllTestTables() {
		try
		{
			AzureTableCollection collection = getReader().queryTables();
			for (AzureTable table : collection) {
				if (table.getTableName().toLowerCase().indexOf("test") == 0)
					getWriter().deleteTable(table.getTableName());
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteEntity() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestDeleteEntity");
			String partitionKey = "pk1";
			String rowKey = "rk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			AzureTable table = getTable(tableName);
			Assert.assertNotNull(table);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(1, collection.getEntities().size());
			
			EntityBase entity = getWriter().deleteEntity(tableName, partitionKey, rowKey, IfMatch.WILD);
			Assert.assertEquals(HttpStatusCode.NoContent, entity.getHttpStatusCode());
			
			collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(0, collection.getEntities().size());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testFilter() {
		try
		{
			if (TARGET == TestTarget.MOCK)
				return;
			
			Date now = new Date();
			String tableName = generateTableName("TestFilter");
			String partitionKey = "pk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			AzureTable table = getTable(tableName);
			Assert.assertNotNull(table);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, "rk1");
			getWriter().insertEntity("test entity 1", now, AUTHOR, tableName, properties);
			properties = new PropertyCollection(partitionKey, "rk2");
			getWriter().insertEntity("test entity 2", now, AUTHOR, tableName, properties);
			properties = new PropertyCollection(partitionKey, "rk3");
			getWriter().insertEntity("test entity 3", now, AUTHOR, tableName, properties);
			
			Filter filter = Filter.Equal("PartitionKey", partitionKey);
			AzureTableEntityCollection collection = getReader().queryEntities(tableName, filter, null);
			Assert.assertEquals(3, collection.getEntities().size());
			
			filter = Filter.Equal("RowKey", "rk1");
			collection = getReader().queryEntities(tableName, filter, null);
			Assert.assertEquals(1, collection.getEntities().size());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testFilterExpressions() {
		try
		{
			Filter expression = Filter.And(Filter.Equal("PartitionKey", "MyPartitionKey"), Filter.Equal("RowKey", "MyRowKey1"));
			String representation = expression.getRepresentation();
			Assert.assertEquals("(PartitionKey%20eq%20'MyPartitionKey')%20and%20(RowKey%20eq%20'MyRowKey1')", representation);
			
			expression = Filter.Not(Filter.Or(Filter.Equal("FirstName", "Moe"), Filter.Equal("FirstName", "Larry")));
			representation = expression.getRepresentation();
			Assert.assertEquals("not((FirstName%20eq%20'Moe')%20or%20(FirstName%20eq%20'Larry'))", representation);
			
			expression = Filter.GreaterThan("Age", 30);
			representation = expression.getRepresentation();
			Assert.assertEquals("Age%20gt%2030", representation);
			
			expression = Filter.LessOrEqual("AmountDue", 100.25);
			representation = expression.getRepresentation();
			Assert.assertEquals("AmountDue%20le%20100.25", representation);
			
			expression = Filter.Equal("IsActive", true);
			representation = expression.getRepresentation();
			Assert.assertEquals("IsActive%20eq%20true", representation);
			
			Date testDate = new Date(2008 - 1900, 6, 10, 0, 0, 0);
			expression = Filter.Equal("CustomerSince", testDate);
			representation = expression.getRepresentation();
			Assert.assertEquals("CustomerSince%20eq%20datetime'2008-07-10T00:00:00+0000'", representation);
			
			UUID testGuid = UUID.fromString("a455c695-df98-5678-aaaa-81d3367e5a34");
			expression = Filter.Equal("GuidValue", testGuid);
			representation = expression.getRepresentation();
			Assert.assertEquals("GuidValue%20eq%20guid'a455c695-df98-5678-aaaa-81d3367e5a34'", representation);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testGetEntity() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestGetEntity");
			String partitionKey = "pk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, "rk1", now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			properties = new PropertyCollection(partitionKey, "rk2", now);
			properties.add(Property.newProperty("D", 4));
			properties.add(Property.newProperty("E", 5));
			properties.add(Property.newProperty("F", 6));
			getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(2, collection.getEntities().size());
			
			AzureTableEntity entity = getReader().getEntity(tableName, partitionKey, "rk2");
			Assert.assertEquals(HttpStatusCode.OK, entity.getHttpStatusCode());
			Assert.assertEquals(tableName, entity.getTableName());
			Assert.assertEquals(partitionKey, entity.getPartitionKey());
			Assert.assertEquals("rk2", entity.getRowKey());
			
			properties = entity.getProperties();
			Property<Integer> property = properties.getProperty("E");
			Assert.assertEquals((Integer) 5, property.getValue());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testInsertEntity() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestInsertEntity");
			String partitionKey = "pk1";
			String rowKey = "rk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			AzureTableEntity entity = getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			Assert.assertEquals(tableName, entity.getTableName());
			Assert.assertEquals(partitionKey, entity.getPartitionKey());
			Assert.assertEquals(rowKey, entity.getRowKey());
			
			properties = entity.getProperties();
			Assert.assertEquals(6, properties.getCount());
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(1, collection.getEntities().size());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testMergeEntity() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestMergeEntity");
			String partitionKey = "pk1";
			String rowKey = "rk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			AzureTableEntity entity = getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			Assert.assertEquals(tableName, entity.getTableName());
			Assert.assertEquals(partitionKey, entity.getPartitionKey());
			Assert.assertEquals(rowKey, entity.getRowKey());
			
			properties = entity.getProperties();
			Assert.assertEquals(6, properties.getCount());
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(1, collection.getEntities().size());
			
			properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("D", 4));
			properties.add(Property.newProperty("E", 5));
			properties.add(Property.newProperty("F", 6));
			EntityBase result = getWriter().mergeEntity("test entity", now, AUTHOR, tableName, properties, IfMatch.WILD);
			Assert.assertEquals(HttpStatusCode.NoContent, result.getHttpStatusCode());
			
			entity = getReader().getEntity(tableName, partitionKey, rowKey);
			Assert.assertEquals(9, entity.getProperties().getCount());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testQueryAllEntities() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestQueryAllEntities");
			String partitionKey = "pk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, "rk1", now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			properties = new PropertyCollection(partitionKey, "rk2", now);
			properties.add(Property.newProperty("D", 4));
			properties.add(Property.newProperty("E", 5));
			properties.add(Property.newProperty("F", 6));
			getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(2, collection.getEntities().size());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testQueryTables() {
		try
		{
			AzureTableCollection tables = getReader().queryTables();
			int tableCount = tables.getTables().size();
			if (tableCount == 0)
				testCreateAndDeleteTable();
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testTop() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestTop");
			String partitionKey = "pk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			AzureTable table = getTable(tableName);
			Assert.assertNotNull(table);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, "rk1");
			getWriter().insertEntity("test entity 1", now, AUTHOR, tableName, properties);
			properties = new PropertyCollection(partitionKey, "rk2");
			getWriter().insertEntity("test entity 2", now, AUTHOR, tableName, properties);
			properties = new PropertyCollection(partitionKey, "rk3");
			getWriter().insertEntity("test entity 3", now, AUTHOR, tableName, properties);
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(3, collection.getEntities().size());
			
			collection = getReader().queryEntities(tableName, null, 2);
			Assert.assertEquals(2, collection.getEntities().size());
			
			collection = getReader().queryEntities(tableName, null, 1);
			Assert.assertEquals(1, collection.getEntities().size());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testUpdateEntity() {
		try
		{
			Date now = new Date();
			String tableName = generateTableName("TestUpdateEntity");
			String partitionKey = "pk1";
			String rowKey = "rk1";
			
			getWriter().createTable("test table", now, AUTHOR, tableName);
			
			PropertyCollection properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("A", 1));
			properties.add(Property.newProperty("B", 2));
			properties.add(Property.newProperty("C", 3));
			AzureTableEntity entity = getWriter().insertEntity("test entity", now, AUTHOR, tableName, properties);
			
			Assert.assertEquals(tableName, entity.getTableName());
			Assert.assertEquals(partitionKey, entity.getPartitionKey());
			Assert.assertEquals(rowKey, entity.getRowKey());
			
			properties = entity.getProperties();
			Assert.assertEquals(6, properties.getCount());
			
			AzureTableEntityCollection collection = getReader().queryAllEntities(tableName);
			Assert.assertEquals(1, collection.getEntities().size());
			
			properties = new PropertyCollection(partitionKey, rowKey, now);
			properties.add(Property.newProperty("D", 4));
			properties.add(Property.newProperty("E", 5));
			properties.add(Property.newProperty("F", 6));
			EntityBase result = getWriter().updateEntity("test entity", now, AUTHOR, tableName, properties, IfMatch.WILD);
			Assert.assertEquals(HttpStatusCode.NoContent, result.getHttpStatusCode());
			
			entity = getReader().getEntity(tableName, partitionKey, rowKey);
			properties = entity.getProperties();
			Assert.assertEquals(6, properties.getCount());
			Property<Integer> property = properties.getProperty("E");
			Assert.assertEquals((Integer) 5, property.getValue());
			
			getWriter().deleteTable(tableName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	private String generateTableName(String prefix)
		throws Exception {
		
		AzureTableCollection tables = getReader().queryTables();
		int tableCount = tables.getTables().size();
		return prefix + tableCount;
	}
	
	private AuthenticationToken getAuthenticationToken()
		throws NotAuthenticatedException {
		
		if (token == null) {
			switch (TARGET) {
			
			case DIRECT:
				token = AuthenticationTests.buildDirectConnectToken();
				break;
				
			case PROXY:
				token = AuthenticationTests.buildProxyToken();
				break;
			
			case MOCK:
				token = AuthenticationTests.buildMockToken();
				break;
			}
		}
		return token;
	}
	
	private TableReader getReader()
		throws Exception {
		
		if (tableReader == null)
			tableReader = new AzureTableManager(getAuthenticationToken());
		return tableReader;
	}
	
	private AzureTable getTable(String tableName)
		throws Exception {
		
		AzureTableCollection tables = getReader().queryTables();
		for (AzureTable table : tables.getTables()) {
			if (table.getTableName().equals(tableName))
				return table;
		}
		return null;
	}
	
	private TableWriter getWriter()
		throws Exception {
	
		if (tableWriter == null)
			tableWriter = new AzureTableManager(getAuthenticationToken());
		return tableWriter;
	}
	
	private static final String AUTHOR = "JUnit";
	private static final TestTarget TARGET = TestTarget.DIRECT;
	
	private TableReader tableReader;
	private TableWriter tableWriter;
	private AuthenticationToken token;
}
