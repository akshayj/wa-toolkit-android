package com.windowsazure.samples.table;

import java.util.Date;

import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.internal.AzureManager;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.UnableToBuildStrategyException;
import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.mock.MockTableStrategy;
import com.windowsazure.samples.internal.table.CreateTableDOMBuilder;
import com.windowsazure.samples.internal.table.DirectConnectTableStrategy;
import com.windowsazure.samples.internal.table.IfMatch;
import com.windowsazure.samples.internal.table.ProxyTableStrategy;
import com.windowsazure.samples.internal.table.TableDOMAdapter;
import com.windowsazure.samples.internal.table.TableCollectionDOMAdapter;
import com.windowsazure.samples.internal.table.TableEntityDOMAdapter;
import com.windowsazure.samples.internal.table.TableEntityDOMBuilder;
import com.windowsazure.samples.internal.table.TableEntityCollectionDOMAdapter;
import com.windowsazure.samples.internal.table.TableOperationResponseAdapter;


public class AzureTableManager extends AzureManager implements TableReader, TableWriter {

	public AzureTableManager(AuthenticationToken token)
		throws Exception {
		
		super(token);
	}
	
	@Override
	public AzureTable createTable(String title, Date updated, String authorName, String tableName) {
		try
		{
			if (updated == null)
				updated = new Date();
			
			String httpBody = new CreateTableDOMBuilder(title, updated, authorName, tableName).getXmlString(false);
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.CreateTable)
				.setDate(updated)
				.setTableName(tableName)
				.setHttpBody(httpBody);
			
			return strategy.execute(context, TableDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTable table = new AzureTable();
			table.setException(e);
			return table;
		}
	}
	
	@Override
	public TableOperationResponse deleteEntity(String tableName, String partitionKey, String rowKey, IfMatch match) {
		try
		{
			if (match == null)
				match = IfMatch.WILD;
			
			AzureStrategyContext context = new AzureStrategyContext()
			    .setOperation(AzureOperation.DeleteTableEntity)
			    .setDate(new Date())
			    .setIfMatch(match)
			    .setTableName(tableName)
			    .setPartitionKey(partitionKey)
			    .setRowKey(rowKey);
			
			return strategy.execute(context, TableOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			TableOperationResponse response = new TableOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public TableOperationResponse deleteTable(String tableName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.DeleteTable)
				.setDate(new Date())
				.setTableName(tableName);

			return strategy.execute(context, TableOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			TableOperationResponse response = new TableOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public AzureTableEntity getEntity(String tableName, String partitionKey, String rowKey) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
			    .setOperation(AzureOperation.QueryTableEntities)
			    .setDate(new Date())
				.setTableName(tableName)
				.setPartitionKey(partitionKey)
				.setRowKey(rowKey);
			
			return strategy.execute(context, TableEntityDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTableEntity entity = new AzureTableEntity();
			entity.setException(e);
			return entity;
		}
	}
	
	@Override
	public AzureTableEntity insertEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties) {
		try
		{
			String httpBody = new TableEntityDOMBuilder(title, updated, authorName, properties).getXmlString(false);
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.InsertTableEntity)
				.setDate(updated)
				.setTableName(tableName)
				.setHttpBody(httpBody);
				
			return strategy.execute(context, TableEntityDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTableEntity entity = new AzureTableEntity();
			entity.setException(e);
			return entity;
		}
	}
	
	@Override
	public TableOperationResponse mergeEntity(
			String title, Date updated, String authorName, String tableName,
			PropertyCollection properties, IfMatch match) {
		try
		{
			if (match == null)
				match = IfMatch.WILD;
				
			String partitionKey = properties.getPartitionKey();
			String rowKey = properties.getRowKey();
			String httpBody = new TableEntityDOMBuilder(title, updated, authorName, properties).getXmlString(false);
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.MergeTableEntity)
				.setDate(updated)
				.setIfMatch(match)
				.setTableName(tableName)
				.setPartitionKey(partitionKey)
				.setRowKey(rowKey)
				.setHttpBody(httpBody);
			
			return strategy.execute(context, TableOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			TableOperationResponse response = new TableOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public AzureTableEntityCollection queryAllEntities(String tableName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.QueryTableEntities)
				.setDate(new Date())
				.setTableName(tableName);
				
			return strategy.execute(context, TableEntityCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTableEntityCollection collection = new AzureTableEntityCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	public AzureTableEntityCollection queryEntities(String tableName, Filter filter, Integer top) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.QueryTableEntities)
				.setDate(new Date())
				.setFilter(filter)
				.setTableName(tableName)
				.setTop(top);
				
			return strategy.execute(context, TableEntityCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTableEntityCollection collection = new AzureTableEntityCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public AzureTableCollection queryTables() {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.QueryTables)
				.setDate(new Date());
				
			return strategy.execute(context, TableCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureTableCollection collection = new AzureTableCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public TableOperationResponse updateEntity(
			String title, Date updated, String authorName, String tableName,
			PropertyCollection properties, IfMatch match) {
		try
		{
			if (match == null)
				match = IfMatch.WILD;
			
			String partitionKey = properties.getPartitionKey();
			String rowKey = properties.getRowKey();
			String httpBody = new TableEntityDOMBuilder(title, updated, authorName, properties).getXmlString(false);
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.UpdateTableEntity)
				.setDate(updated)
				.setIfMatch(match)
				.setTableName(tableName)
				.setPartitionKey(partitionKey)
				.setRowKey(rowKey)
				.setHttpBody(httpBody);
					
			return strategy.execute(context, TableOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			TableOperationResponse response = new TableOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	protected AzureStrategy<? extends AuthenticationToken> buildStrategy(AuthenticationToken token)
		throws UnableToBuildStrategyException {
		
		if (token instanceof DirectConnectToken)
			return new DirectConnectTableStrategy((DirectConnectToken) token);
		
		if (token instanceof ProxyToken)
			return new ProxyTableStrategy((ProxyToken) token);
		
		if (token instanceof MockToken)
			return new MockTableStrategy((MockToken) token);
		
		throw new UnableToBuildStrategyException();
	}
}
