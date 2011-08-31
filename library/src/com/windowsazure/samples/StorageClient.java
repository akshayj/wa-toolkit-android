package com.windowsazure.samples;

import java.util.Date;
import java.util.Set;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobCollection;
import com.windowsazure.samples.blob.AzureBlobManager;
import com.windowsazure.samples.blob.AzureContainerCollection;
import com.windowsazure.samples.blob.AzureContainerMetadata;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobReader;
import com.windowsazure.samples.blob.BlobWriter;
import com.windowsazure.samples.blob.Block;
import com.windowsazure.samples.blob.BlockList;
import com.windowsazure.samples.blob.ContainerAccess;
import com.windowsazure.samples.blob.EnumerationFilter;
import com.windowsazure.samples.blob.LeaseAction;
import com.windowsazure.samples.blob.PageRangeCollection;
import com.windowsazure.samples.blob.BlobOperationResponse;
import com.windowsazure.samples.blob.PutPageAction;
import com.windowsazure.samples.blob.SequenceNumberAction;
import com.windowsazure.samples.blob.SnapshotDeleteAction;
import com.windowsazure.samples.blob.condition.CopyBlobCondition;
import com.windowsazure.samples.blob.condition.DeleteBlobCondition;
import com.windowsazure.samples.blob.condition.DeleteContainerCondition;
import com.windowsazure.samples.blob.condition.GetBlobCondition;
import com.windowsazure.samples.blob.condition.GetBlobMetadataCondition;
import com.windowsazure.samples.blob.condition.GetBlobPropertiesCondition;
import com.windowsazure.samples.blob.condition.GetPageRegionsCondition;
import com.windowsazure.samples.blob.condition.LeaseBlobCondition;
import com.windowsazure.samples.blob.condition.PutBlobCondition;
import com.windowsazure.samples.blob.condition.PutBlockListCondition;
import com.windowsazure.samples.blob.condition.PutPageCondition;
import com.windowsazure.samples.blob.condition.SetBlobMetadataCondition;
import com.windowsazure.samples.blob.condition.SetBlobPropertiesCondition;
import com.windowsazure.samples.blob.condition.SetContainerMetadataCondition;
import com.windowsazure.samples.blob.condition.SnapshotBlobCondition;
import com.windowsazure.samples.blob.data.BlobData;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.table.IfMatch;
import com.windowsazure.samples.mock.MockBlobManager;
import com.windowsazure.samples.mock.MockQueueManager;
import com.windowsazure.samples.mock.MockTableManager;
import com.windowsazure.samples.queue.AzureQueueCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.queue.AzureQueueMetadata;
import com.windowsazure.samples.queue.QueueOperationResponse;
import com.windowsazure.samples.queue.QueueReader;
import com.windowsazure.samples.queue.QueueWriter;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.table.AzureTableManager;
import com.windowsazure.samples.table.Filter;
import com.windowsazure.samples.table.TableOperationResponse;
import com.windowsazure.samples.table.TableReader;
import com.windowsazure.samples.table.TableWriter;


public class StorageClient implements
	BlobReader, BlobWriter,
	QueueReader, QueueWriter,
	TableReader, TableWriter {
	
	public StorageClient(AuthenticationToken token) {
		this.token = token;
	}
	
	// Blob Operations
	
	@Override
	public BlobOperationResponse copyBlob(String sourceContainerName, String sourceBlobName, String sourceLeaseId, String sourceSnapshot, String destinationContainerName, String destinationBlobName, String destinationLeaseId, CopyBlobCondition condition, MetadataCollection metadata) {
		return getBlobWriter().copyBlob(sourceContainerName, sourceBlobName, sourceLeaseId, sourceSnapshot, destinationContainerName, destinationBlobName, destinationLeaseId, condition, metadata);
	}
	
	@Override
	public BlobOperationResponse createContainer(String containerName, MetadataCollection metadata, ContainerAccess containerAccess) {
		return getBlobWriter().createContainer(containerName, metadata, containerAccess);
	}
	
	@Override
	public BlobOperationResponse deleteBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition) {
		return getBlobWriter().deleteBlob(containerName, blobName, leaseId, condition);
	}
	
	@Override
	public BlobOperationResponse deleteContainer(String containerName, DeleteContainerCondition condition) {
		return getBlobWriter().deleteContainer(containerName, condition);
	}
	
	@Override
	public BlobOperationResponse deleteSnapshotBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition, String snapshot, SnapshotDeleteAction action) {
		return getBlobWriter().deleteSnapshotBlob(containerName, blobName, leaseId, condition, snapshot, action);
	}
	
	@Override
	public AzureBlob getBlob(String containerName, String blobName, String snapshot, BlobRange range, String leaseId, GetBlobCondition condition) {
		return getBlobReader().getBlob(containerName, blobName, snapshot, range, leaseId, condition);
	}
	
	@Override
	public BlobOperationResponse getBlobMetadata(String containerName, String blobName, String snapshot, String leaseId, GetBlobMetadataCondition condition) {
		return getBlobReader().getBlobMetadata(containerName, blobName, snapshot, leaseId, condition);
	}
	
	@Override
	public BlobOperationResponse getBlobProperties(String containerName, String blobName, String snapshot, String leaseId, GetBlobPropertiesCondition condition) {
		return getBlobReader().getBlobProperties(containerName, blobName, snapshot, leaseId, condition);
	}
	
	@Override
	public AzureBlob getBlockBlob(String containerName, String blobName) {
		return getBlobReader().getBlockBlob(containerName, blobName);
	}
	
	@Override
	public BlockList getBlockList(String containerName, String blobName, String leaseId, String snapshot) {
		return getBlobReader().getBlockList(containerName, blobName, leaseId, snapshot);
	}
	
	@Override
	public ACLCollection getContainerACL(String containerName) {
		return getBlobReader().getContainerACL(containerName);
	}
	
	@Override
	public AzureContainerMetadata getContainerMetadata(String containerName) {
		return getBlobReader().getContainerMetadata(containerName);
	}
	
	@Override
	public AzureContainerMetadata getContainerProperties(String containerName) {
		return getBlobReader().getContainerProperties(containerName);
	}
	
	@Override
	public AzureBlob getPageBlob(String containerName, String blobName, BlobRange range) {
		return getBlobReader().getPageBlob(containerName, blobName, range);
	}
	
	@Override
	public PageRangeCollection getPageRegions(String containerName, String blobName, String snapshot, BlobRange range, String leaseId, GetPageRegionsCondition condition) {
		return getBlobReader().getPageRegions(containerName, blobName, snapshot, range, leaseId, condition);
	}
	
	@Override
	public BlobOperationResponse initializePageBlob(String containerName, String blobName, String leaseId, MetadataCollection metadata, int maxSize, Integer blobSequenceNumber, PutBlobCondition condition) {
		return getBlobWriter().initializePageBlob(containerName, blobName, leaseId, metadata, maxSize, blobSequenceNumber, condition);
	}
	
	@Override
	public AzureBlobCollection listAllBlobs(String containerName) {
		return getBlobReader().listAllBlobs(containerName);
	}
	
	@Override
	public BlobOperationResponse leaseBlob(String containerName, String blobName, String leaseId, LeaseAction action, LeaseBlobCondition condition) {
		return getBlobWriter().leaseBlob(containerName, blobName, leaseId, action, condition);
	}

	@Override
	public AzureBlobCollection listBlobs(
			String containerName,
			String prefix,
			String delimiter,
			String marker,
			Integer maxResults,
			Set<EnumerationFilter> includes) {
		return getBlobReader().listBlobs(containerName, prefix, delimiter, marker, maxResults, includes);
	}

	@Override
	public AzureContainerCollection listAllContainers() {
		return getBlobReader().listAllContainers();
	}

	@Override
	public AzureContainerCollection listContainers(String prefix, String marker, Integer maxResults) {
		return getBlobReader().listContainers(prefix, marker, maxResults);
	}
	
	@Override
	public BlobOperationResponse putBlock(String containerName, String blobName, String leaseId, Block block, BlobData blobData) {
		return getBlobWriter().putBlock(containerName, blobName, leaseId, block, blobData);
	}
	
	@Override
	public BlobOperationResponse putBlockBlob(String containerName, String blobName, String leaseId, MetadataCollection metadata, BlobData blobData, PutBlobCondition condition) {
		return getBlobWriter().putBlockBlob(containerName, blobName, leaseId, metadata, blobData, condition);
	}
	
	@Override
	public BlobOperationResponse putBlockList(String containerName, String blobName, String leaseId, BlockList blockList) {
		return getBlobWriter().putBlockList(containerName, blobName, leaseId, blockList);
	}

	@Override
	public BlobOperationResponse putBlockList(String containerName, String blobName, String leaseId, PutBlockListCondition condition, BlockList blockList, MetadataCollection metadata, String cacheControl, String contentType, String contentEncoding, String contentLanguage, String contentMd5) {
		return getBlobWriter().putBlockList(containerName, blobName, leaseId, condition, blockList, metadata, cacheControl, contentType, contentEncoding, contentLanguage, contentMd5);
	}
	
	@Override
	public BlobOperationResponse putPage(String containerName, String blobName, BlobRange range, PutPageAction action, String leaseId, PutPageCondition condition, BlobData blobData) {
		return getBlobWriter().putPage(containerName, blobName, range, action, leaseId, condition, blobData);
	}
	
	@Override
	public BlobOperationResponse setBlobMetadata(String containerName, String blobName, String leaseId, SetBlobMetadataCondition condition,  MetadataCollection metadata) {
		return getBlobWriter().setBlobMetadata(containerName, blobName, leaseId, condition, metadata);
	}
	
	@Override
	public BlobOperationResponse setBlockBlobProperties(String containerName, String blobName, String leaseId, SetBlobPropertiesCondition condition, String cacheControl, String contentType, String contentMd5, String contentEncoding, String contentLanguage) {
		return getBlobWriter().setBlockBlobProperties(containerName, blobName, leaseId, condition, cacheControl, contentType, contentMd5, contentEncoding, contentLanguage);
	}
	
	@Override
	public BlobOperationResponse setPageBlobProperties(String containerName, String blobName, String leaseId, SetBlobPropertiesCondition condition, String cacheControl, String contentType, String contentMd5, String contentEncoding, String contentLanguage, Integer contentLength, SequenceNumberAction sequenceNumberAction, Integer sequenceNumber) {
		return getBlobWriter().setPageBlobProperties(containerName, blobName, leaseId, condition, cacheControl, contentType, contentMd5, contentEncoding, contentLanguage, contentLength, sequenceNumberAction, sequenceNumber);
	}
	
	@Override
	public BlobOperationResponse setContainerACL(String containerName, ContainerAccess containerAccess, ACLCollection acl) {
		return getBlobWriter().setContainerACL(containerName, containerAccess, acl);
	}
	
	@Override
	public BlobOperationResponse setContainerMetadata(String containerName, MetadataCollection metadata, SetContainerMetadataCondition condition) {
		return getBlobWriter().setContainerMetadata(containerName, metadata, condition);
	}
	
	@Override
	public BlobOperationResponse snapshotBlob(String containerName, String blobName, String leaseId, SnapshotBlobCondition condition, MetadataCollection metadata) {
		return getBlobWriter().snapshotBlob(containerName, blobName, leaseId, condition, metadata);
	}
	
	// Queue Operations
	
	@Override
	public QueueOperationResponse clearMessages(String queueName) {
		return getQueueWriter().clearMessages(queueName);
	}
	
	@Override
	public QueueOperationResponse createQueue(String queueName) {
		return getQueueWriter().createQueue(queueName);
	}
	
	@Override
	public QueueOperationResponse createQueue(String queueName, MetadataCollection metadata) {
		return getQueueWriter().createQueue(queueName, metadata);
	}
	
	@Override
	public QueueOperationResponse deleteMessage(String queueName, String messageId, String popReceipt) {
		return getQueueWriter().deleteMessage(queueName, messageId, popReceipt);
	}
	
	@Override
	public QueueOperationResponse deleteQueue(String queueName) {
		return getQueueWriter().deleteQueue(queueName);
	}
	
	@Override
	public AzureQueueMessageCollection getMessages(String queueName, Integer numberOfMessages, Integer visibilityTimeout) {
		return getQueueReader().getMessages(queueName, numberOfMessages, visibilityTimeout);
	}
	
	@Override
	public AzureQueueMetadata getQueueMetadata(String queueName) {
		return getQueueReader().getQueueMetadata(queueName);
	}
	
	@Override
	public AzureQueueCollection listAllQueues() {
		return getQueueReader().listAllQueues();
	}
	
	@Override
	public AzureQueueCollection listQueues(String prefix, String marker, Integer maxResults) {
		return getQueueReader().listQueues(prefix, marker, maxResults);
	}
	
	@Override
	public AzureQueueMessageCollection peekMessages(String queueName, Integer numberOfMessages) {
		return getQueueReader().peekMessages(queueName, numberOfMessages);
	}
	
	@Override
	public QueueOperationResponse putMessage(String queueName, String messageText, Integer timeToLiveInterval) {
		return getQueueWriter().putMessage(queueName, messageText, timeToLiveInterval);
	}
	
	@Override
	public QueueOperationResponse setQueueMetadata(String queueName, MetadataCollection metadata) {
		return getQueueWriter().setQueueMetadata(queueName, metadata);
	}
	
	// Table Operations
	
	@Override
	public AzureTable createTable(String title, Date updated, String authorName, String tableName) {
		return getTableWriter().createTable(title, updated, authorName, tableName);
	}

	@Override
	public TableOperationResponse deleteEntity(String tableName, String partitionKey, String rowKey, IfMatch match) {
		return getTableWriter().deleteEntity(tableName, partitionKey, rowKey, match);
	}

	@Override
	public TableOperationResponse deleteTable(String tableName) {
		return getTableWriter().deleteTable(tableName);
	}
	
	@Override
	public AzureTableEntity getEntity(String tableName, String partitionKey, String rowKey) {
		return getTableReader().getEntity(tableName, partitionKey, rowKey);
	}

	@Override
	public AzureTableEntity insertEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties) {
		return getTableWriter().insertEntity(title, updated, authorName, tableName, properties);
	}

	@Override
	public TableOperationResponse mergeEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties, IfMatch match) {
		return getTableWriter().mergeEntity(title, updated, authorName, tableName, properties, match);
	}
	
	@Override
	public AzureTableEntityCollection queryAllEntities(String tableName) {
		return getTableReader().queryAllEntities(tableName);
	}
	
	@Override
	public AzureTableEntityCollection queryEntities(String tableName, Filter filter, Integer top) {
		return getTableReader().queryEntities(tableName, filter, top);
	}
	
	@Override
	public AzureTableCollection queryTables() {
		return getTableReader().queryTables();
	}

	@Override
	public TableOperationResponse updateEntity(String title, Date updated, String authorName, String tableName, PropertyCollection properties, IfMatch match) {
		return getTableWriter().updateEntity(title, updated, authorName, tableName, properties, match);
	}
	
	private BlobReader getBlobReader() {
		try
		{
			if (blobReader == null) {
				Object manager = (token instanceof MockToken) ? new MockBlobManager(token) : new AzureBlobManager(token);
				blobReader = (BlobReader) manager;
				blobWriter = (BlobWriter) manager;
			}
		}
		catch (Exception e) {}
		return blobReader;
	}
	
	private BlobWriter getBlobWriter() {
		try
		{
			if (blobWriter == null) {
				Object manager = (token instanceof MockToken) ? new MockBlobManager(token) : new AzureBlobManager(token);
				blobReader = (BlobReader) manager;
				blobWriter = (BlobWriter) manager;
			}
		}
		catch (Exception e) {}
		return blobWriter;
	}
	
	private QueueReader getQueueReader() {
		try
		{
			if (queueReader == null) {
				Object manager = (token instanceof MockToken) ? new MockQueueManager() : new AzureQueueManager(token);
				queueReader = (QueueReader) manager;
				queueWriter = (QueueWriter) manager;
			}
		}
		catch (Exception e) {}
		return queueReader;
	}
	
	private QueueWriter getQueueWriter() {
		try
		{
			if (queueWriter == null) {
				Object manager = (token instanceof MockToken) ? new MockQueueManager() : new AzureQueueManager(token);
				queueReader = (QueueReader) manager;
				queueWriter = (QueueWriter) manager;
			}
		}
		catch (Exception e) {}
		return queueWriter;
	}
	
	private TableReader getTableReader() {
		try
		{
			if (tableReader == null) {
				Object manager = (token instanceof MockToken) ? new MockTableManager() : new AzureTableManager(token);
				tableReader = (TableReader) manager;
				tableWriter = (TableWriter) manager;
			}
		}
		catch (Exception e) {}
		return tableReader;
	}
	
	private TableWriter getTableWriter() {
		try
		{
			if (tableWriter == null) {
				Object manager = (token instanceof MockToken) ? new MockTableManager() : new AzureTableManager(token);
				tableReader = (TableReader) manager;
				tableWriter = (TableWriter) manager;
			}
		}
		catch (Exception e) {}
		return tableWriter;
	}
	
	private BlobReader blobReader;
	private BlobWriter blobWriter;
	private QueueReader queueReader;
	private QueueWriter queueWriter;
	private TableReader tableReader;
	private TableWriter tableWriter;
	private AuthenticationToken token;
}
