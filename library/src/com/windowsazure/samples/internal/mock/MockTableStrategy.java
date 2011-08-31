package com.windowsazure.samples.internal.mock;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.windowsazure.samples.EdmType;
import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.OperationNotSupportedException;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.util.Pair;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.xml.XmlAttribute;
import com.windowsazure.samples.internal.xml.XmlNode;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.table.TableOperationResponse;


public class MockTableStrategy extends MockStrategy  {

	public MockTableStrategy(MockToken token) {
		super(token);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <V extends EntityBase> V performOperation(AzureOperation operation, AzureStrategyContext context)
	    throws Exception {
		
		switch (operation) {
		case CreateTable:			return (V) createTable(context);
		case DeleteTableEntity: 	return (V) deleteTableEntity(context);
		case DeleteTable:   		return (V) deleteTable(context);
		case InsertTableEntity:		return (V) insertTableEntity(context);
		case MergeTableEntity:		return (V) mergeTableEntity(context);
		case QueryTableEntities:	return queryTableEntities(context);
		case QueryTables:   		return (V) queryTables(context);
		case UpdateTableEntity:		return (V) updateTableEntity(context);
		
		default:					throw new OperationNotSupportedException(operation, this);
		}
	}
	
	protected AzureTable createTable(AzureStrategyContext context) {
		String title = extractString("/entry/title");
		Date updated = extractDate("/entry/updated");
		String authorName = extractString("/entry/author/name");
		String tableName = extractString("//TableName");
		
		String id = makeTableId(tableName);
		if (getMockTableById(id) != null) {
			AzureTable table = new AzureTable();
			table.setHttpStatusCode(HttpStatusCode.Conflict);
			return table;
		}
		
		MockTable mt = new MockTable();
		mt.authorName = authorName;
		mt.id = id;
		mt.tableName = tableName;
		mt.title = title;
		mt.updated = updated;
		tables.add(mt);
		
		AzureTable table = getAzureTableById(id);
		table.setHttpStatusCode(HttpStatusCode.Created);
		return table;
	}
	
	protected TableOperationResponse deleteTable(AzureStrategyContext context) {
		TableOperationResponse response = new TableOperationResponse();
		
		String tableName = context.getTableName();
		String id = makeTableId(tableName);
		
		MockTable mt = getMockTableById(id);
		if (mt == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		tables.remove(mt);
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	// Not supported:
	//     If-Match
	protected TableOperationResponse deleteTableEntity(AzureStrategyContext context) {
		String tableName = context.getTableName();
		String tableId = makeTableId(tableName);
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		String entityId = makeTableEntityId(tableName, partitionKey, rowKey);
		
		TableOperationResponse response = new TableOperationResponse();
		
		MockTable mt = getMockTableById(tableId);
		MockTableEntity mte = (mt != null) ? getMockTableEntityById(tableId, entityId) : null;
		if (mte == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		mt.entities.remove(mte);
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	protected AzureTableEntity insertTableEntity(AzureStrategyContext context)
		throws Exception {
		
		String tableName = context.getTableName();
		String tableId = makeTableId(tableName);
		String partitionKey = extractString("//properties/PartitionKey");
		String rowKey = extractString("//properties/RowKey");
		
		String title = extractString("/entry/title");
		Date updated = extractDate("/entry/updated");
		String authorName = extractString("/entry/author/name");
		Collection<Property<?>> properties = extractProperties("//properties");
		String entityId = makeTableEntityId(tableName, partitionKey, rowKey);
		
		MockTable mt = getMockTableById(tableId);
		if (mt == null) {
			AzureTableEntity entity = new AzureTableEntity();
			entity.setHttpStatusCode(HttpStatusCode.NotFound);
			return entity;
		}
		
		MockTableEntity mte = getMockTableEntityById(tableId, entityId);
		if (mte != null) {
			AzureTableEntity entity = new AzureTableEntity();
			entity.setHttpStatusCode(HttpStatusCode.Conflict);
			return entity;
		}
		
		mte = new MockTableEntity();
		mte.authorName = authorName;
		mte.id = entityId;
		mte.properties = new Vector<Property<?>>(properties);
		mte.title = title;
		mte.updated = updated;
		mt.entities.add(mte);
		
		AzureTableEntity entity = new AzureTableEntity(
			Long.toString(++requestId),
			VERSION,
			new Date(),
			entityId,
			title,
			updated,
			authorName,
			tableName,
			partitionKey,
			rowKey,
			buildPropertyCollection(partitionKey, rowKey, updated, properties));
		
		entity.setHttpStatusCode(HttpStatusCode.Created);
		return entity;
	}
	
	// Not supported:
	//     If-Match
	protected TableOperationResponse mergeTableEntity(AzureStrategyContext context)
	    throws Exception {
		
		String tableName = context.getTableName();
		String tableId = makeTableId(tableName);
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		String entityId = makeTableEntityId(tableName, partitionKey, rowKey);
		
		String title = extractString("/entry/title");
		Date updated = extractDate("/entry/updated");
		String authorName = extractString("/entry/author/name");
		Collection<Property<?>> properties = extractProperties("//properties");
		
		TableOperationResponse response = new TableOperationResponse();
		
		MockTable mt = getMockTableById(tableId);
		MockTableEntity mte = (mt != null) ? getMockTableEntityById(tableId, entityId) : null;
		if (mte == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
		}
		
		mte.authorName = authorName;
		mte.title = title;
		mte.updated = updated;
		
		for (Property<?> sourceProperty : properties) {
			String propertyName = sourceProperty.getName();
			for (Property<?> destinationProperty : mte.properties) {
				if (destinationProperty.getName().equals(propertyName)) {
					mte.properties.remove(destinationProperty);
					break;
				}
			}
			mte.properties.add(sourceProperty);
		}
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	// Not supported:
	//    continuation
	//    filter expressions
	@SuppressWarnings("unchecked")
	protected <V extends EntityBase> V queryTableEntities(AzureStrategyContext context) {
		String tableName = context.getTableName();
		String tableId = makeTableId(tableName);
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		Integer top = context.getTop();
		
		MockTable mt = getMockTableById(tableId);
		
		// Return a single entity if the partitionKey and rowKey are specified.
		if (! (Util.isStringNullOrEmpty(partitionKey) || Util.isStringNullOrEmpty(rowKey))) {
			String entityId = makeTableEntityId(tableName, partitionKey, rowKey);
			MockTableEntity mte = (mt != null) ? getMockTableEntityById(tableId, entityId) : null;
			
			if (mte == null) {
				AzureTableEntity entity = new AzureTableEntity();
				entity.setHttpStatusCode(HttpStatusCode.NotFound);
				return (V) entity;
			}
			
			AzureTableEntity entity = new AzureTableEntity(
				Long.toString(++requestId),
				VERSION,
				new Date(),
				entityId,
				mte.title,
				mte.updated,
				mte.authorName,
				tableName,
				partitionKey,
				rowKey,
				buildPropertyCollection(partitionKey, rowKey, mte.updated, mte.properties));
			
			entity.setHttpStatusCode(HttpStatusCode.OK);
			return (V) entity;
		}
		
		// Return a collection of entities.
		
		if (mt == null) {
			AzureTableEntityCollection collection = new AzureTableEntityCollection();
			collection.setHttpStatusCode(HttpStatusCode.NotFound);
			return (V) collection;
		}
		
		++requestId;
		Date now = new Date();
		Date lastUpdated = null;
		
		Vector<AzureTableEntity> entityVector = new Vector<AzureTableEntity>();
		for (MockTableEntity mte : mt.entities) {
			Pair<String, String> keys = getKeysFromMockEntity(mte);
			partitionKey = keys.getFirst();
			rowKey = keys.getSecond();
			
			if (lastUpdated == null)
				lastUpdated = mte.updated;
			if (lastUpdated.before(mte.updated))
				lastUpdated = mte.updated;
			
			AzureTableEntity entity = new AzureTableEntity(
				Long.toString(requestId),
				VERSION,
				now,
				mte.id,
				mte.title,
				mte.updated,
				mte.authorName,
				tableName,
				partitionKey,
				rowKey,
				buildPropertyCollection(partitionKey, rowKey, mte.updated, mte.properties));
			
			entityVector.add(entity);
			if (top != null && --top == 0)
				break;
		}
		
		AzureTableEntityCollection collection = new AzureTableEntityCollection(
			null,
			null,
			Long.toString(requestId),
			VERSION,
			now,
			"Mock Entities of " + tableName,
			URI_BASE + "/" + tableName + "/entities",
			lastUpdated,
			tableName,
			entityVector);
		
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return (V) collection;
	}
	
	// Not Supported:
	//     continuation
	protected AzureTableCollection queryTables(AzureStrategyContext context) {
		++requestId;
		Date now = new Date();
		Date lastUpdated = null;
		
		Vector<AzureTable> tableVector = new Vector<AzureTable>();
		for (MockTable mt : tables) {
			if (lastUpdated == null)
				lastUpdated = mt.updated;
			if (lastUpdated.before(mt.updated))
				lastUpdated = mt.updated;
			
			tableVector.add(new AzureTable(
				Long.toString(requestId),
				VERSION,
				now,
				mt.id,
				mt.title,
				mt.updated,
				mt.authorName,
				mt.tableName));
		}
		
		AzureTableCollection collection = new AzureTableCollection(
			null,
			Long.toString(requestId),
			VERSION,
			now,
			URI_BASE,
			"Mock Tables",
			lastUpdated,
			tableVector);
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
	}
	
	// Not supported:
	//    If-Match
	protected TableOperationResponse updateTableEntity(AzureStrategyContext context)
	    throws Exception {
		
		String tableName = context.getTableName();
		String tableId = makeTableId(tableName);
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		String entityId = makeTableEntityId(tableName, partitionKey, rowKey);
		
		String title = extractString("/entry/title");
		Date updated = extractDate("/entry/updated");
		String authorName = extractString("/entry/author/name");
		Collection<Property<?>> properties = extractProperties("//properties");
		
		TableOperationResponse response = new TableOperationResponse();
		
		MockTable mt = getMockTableById(tableId);
		MockTableEntity mte = (mt != null) ? getMockTableEntityById(tableId, entityId) : null;
		if (mte == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
		}
		
		mte.authorName = authorName;
		mte.properties = properties;
		mte.title = title;
		mte.updated = updated;
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	private PropertyCollection buildPropertyCollection(String partitionKey, String rowKey, Date updated, Collection<Property<?>> properties) {
		PropertyCollection propertyCollection = new PropertyCollection(partitionKey, rowKey, updated);
		for (Property<?> property : properties) {
			propertyCollection.add(property);
		}
		return propertyCollection;
	}
	
	private Collection<Property<?>> extractProperties(String xpath)
		throws Exception {
		
		Vector<Property<?>> propertyVector = new Vector<Property<?>>();
		
		XmlNode propertiesNode = getSingleNode(xpath);
		for (XmlNode node : propertiesNode.getChildren()) {
			String propertyName = node.getLocalName();
			String valueRepresentation = node.getInnerText();
			
			XmlAttribute attribute = node.getAttributeByLocalName(Property.TYPE);
			String typeAttribute = (attribute != null) ? attribute.getValue() : null;
			EdmType edmType = (EdmType) (Util.isStringNullOrEmpty(typeAttribute) ? 
					Property.DEFAULT_TYPE : EdmType.fromRepresentation(typeAttribute));
			propertyVector.add(Property.fromRepresentation(propertyName, edmType, valueRepresentation));
		}
		
		return propertyVector;
	}
	
	private AzureTable getAzureTableById(String id) {
		MockTable mt = getMockTableById(id);
		assert(mt != null);
		
		return new AzureTable(
			Long.toString(++requestId),
			VERSION,
			new Date(),
			mt.id,
			mt.title,
			mt.updated,
			mt.authorName,
			mt.tableName);
	}
	
	private Pair<String, String> getKeysFromMockEntity(MockTableEntity mte) {
		String partitionKey = null;
		String rowKey = null;
		
		for (Property<?> property : mte.properties) {
			String propertyName = property.getName();
			if (propertyName.equals("PartitionKey"))
				partitionKey = (String) property.getValue();
			if (propertyName.equals("RowKey"))
				rowKey = (String) property.getValue();
			if (! (Util.isStringNullOrEmpty(partitionKey) || Util.isStringNullOrEmpty(rowKey)))
				return new Pair<String, String>(partitionKey, rowKey);
		}
		
		return null;
	}
	
	private MockTable getMockTableById(String id) {
		for (MockTable table : tables) {
			if (table.id.equals(id))
				return table;
		}
		
		return null;
	}
	
	private MockTableEntity getMockTableEntityById(String tableId, String entityId) {
		MockTable mt = getMockTableById(tableId);
		assert(mt != null);
		
		for (MockTableEntity mte : mt.entities) {
			if (mte.id.equals(entityId))
				return mte;
		}
		return null;
	}
	
	private String makeTableEntityId(String tableName, String partitionKey, String rowKey) {
		return String.format("%s/%s(PartitionKey='%s',RowKey='%s')", URI_BASE, tableName, partitionKey, rowKey);
	}
	
	private String makeTableId(String tableName) {
		return URI_BASE + "/" + tableName.toLowerCase();
	}
	
	private static final String URI_BASE = "/mock/tables";
	private static final String VERSION = "MockTableStrategy1.0";
	
	private static long requestId = 0;
	private static Vector<MockTable> tables = new Vector<MockTable>();
}
