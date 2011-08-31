package com.windowsazure.samples.internal;

import java.util.Date;
import java.util.Set;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.ContainerAccess;
import com.windowsazure.samples.blob.EnumerationFilter;
import com.windowsazure.samples.blob.LeaseAction;
import com.windowsazure.samples.blob.PutPageAction;
import com.windowsazure.samples.blob.SequenceNumberAction;
import com.windowsazure.samples.blob.SnapshotDeleteAction;
import com.windowsazure.samples.blob.condition.Condition;
import com.windowsazure.samples.blob.data.BlobData;
import com.windowsazure.samples.internal.table.IfMatch;
import com.windowsazure.samples.table.Filter;


public final class AzureStrategyContext {
	
	public BlobData getBlobData() {
		return blobData;
	}
	
	public String getBlobName() {
		return blobName;
	}
	
	public int getBlobSequenceNumber() {
		return blobSequenceNumber;
	}
	
	public BlobType getBlobType() {
		return blobType;
	}
	
	public String getBlockId() {
		return blockId;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public ContainerAccess getContainerAccess() {
		return containerAccess;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public Set<EnumerationFilter> getEnumerationFilter() {
		return enumerationFilter;
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	public String getHttpBody() {
		return httpBody;
	}
	
	public IfMatch getIfMatch() {
		return ifMatch;
	}
	
	public LeaseAction getLeaseAction() {
		return leaseAction;
	}
	
	public String getLeaseId() {
		return leaseId;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public int getMaxPageSize() {
		return maxPageSize;
	}
	
	public int getMaxResults() {
		return maxResults;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public MetadataCollection getMetadata() {
		return metadata;
	}
	
	public int getNumberOfMessages() {
		return numberOfMessages;
	}
	
	public AzureOperation getOperation() {
		return operation;
	}
	
	public String getPartitionKey() {
		return partitionKey;
	}
	
	public String getPopReceipt() {
		return popReceipt;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public PutPageAction getPutPageAction() {
		return putPageAction;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	public BlobRange getRange() {
		return range;
	}
	
	public String getRowKey() {
		return rowKey;
	}
	
	public String getSecondaryBlobName() {
		return secondaryBlobName;
	}
	
	public String getSecondaryContainerName() {
		return secondaryContainerName;
	}
	
	public String getSecondaryLeaseId() {
		return secondaryLeaseId;
	}
	
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	
	public SequenceNumberAction getSequenceNumberAction() {
		return sequenceNumberAction;
	}
	
	public String getSnapshot() {
		return snapshot;
	}
	
	public SnapshotDeleteAction getSnapshotDeleteAction() {
		return snapshotDeleteAction;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public int getTimeToLiveInterval() {
		return timeToLiveInterval;
	}
	
	public Integer getTop() {
		return top;
	}
	
	public int getVisibilityTimeout() {
		return visibilityTimeout;
	}
	
	public AzureStrategyContext setBlobData(BlobData blobData) {
		this.blobData = blobData;
		return this;
	}
	
	public AzureStrategyContext setBlobName(String blobName) {
		this.blobName = blobName;
		return this;
	}
	
	public AzureStrategyContext setBlobSequenceNumber(int blobSequenceNumber) {
		this.blobSequenceNumber = blobSequenceNumber;
		return this;
	}
	
	public AzureStrategyContext setBlobType(BlobType blobType) {
		this.blobType = blobType;
		return this;
	}
	
	public AzureStrategyContext setBlockId(String blockId) {
		this.blockId = blockId;
		return this;
	}
	
	public AzureStrategyContext setCondition(Condition condition) {
		this.condition = condition;
		return this;
	}
	
	public AzureStrategyContext setContainerAccess(ContainerAccess containerAccess) {
		this.containerAccess = containerAccess;
		return this;
	}
	
	public AzureStrategyContext setContainerName(String containerName) {
		this.containerName = containerName;
		return this;
	}
	
	public AzureStrategyContext setDate(Date date) {
		this.date = date;
		return this;
	}
	
	public AzureStrategyContext setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}
	
	public AzureStrategyContext setEnumerationFilter(Set<EnumerationFilter> enumerationFilter) {
		this.enumerationFilter = enumerationFilter;
		return this;
	}
	
	public AzureStrategyContext setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}
	
	public AzureStrategyContext setHttpBody(String httpBody) {
		this.httpBody = httpBody;
		return this;
	}
	
	public AzureStrategyContext setIfMatch(IfMatch ifMatch) {
		this.ifMatch = ifMatch;
		return this;
	}
	
	public AzureStrategyContext setLeaseAction(LeaseAction leaseAction) {
		this.leaseAction = leaseAction;
		return this;
	}
	
	public AzureStrategyContext setLeaseId(String leaseId) {
		this.leaseId = leaseId;
		return this;
	}
	
	public AzureStrategyContext setMarker(String marker) {
		this.marker = marker;
		return this;
	}
	
	public AzureStrategyContext setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
		return this;
	}
	
	public AzureStrategyContext setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}
	
	public AzureStrategyContext setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}
	
	public AzureStrategyContext setMetadata(MetadataCollection metadata) {
		this.metadata = metadata;
		return this;
	}
	
	public AzureStrategyContext setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
		return this;
	}
	
	public AzureStrategyContext setOperation(AzureOperation operation) {
		this.operation = operation;
		return this;
	}
	
	public AzureStrategyContext setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
		return this;
	}
	
	public AzureStrategyContext setPopReceipt(String popReceipt) {
		this.popReceipt = popReceipt;
		return this;
	}
	
	public AzureStrategyContext setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public AzureStrategyContext setPutPageAction(PutPageAction action) {
		this.putPageAction = action;
		return this;
	}
	
	public AzureStrategyContext setQueueName(String queueName) {
		this.queueName = queueName;
		return this;
	}
	
	public AzureStrategyContext setRange(BlobRange range) {
		this.range = range;
		return this;
	}
	
	public AzureStrategyContext setRowKey(String rowKey) {
		this.rowKey = rowKey;
		return this;
	}
	
	public AzureStrategyContext setSecondaryBlobName(String secondaryBlobName) {
		this.secondaryBlobName = secondaryBlobName;
		return this;
	}
	
	public AzureStrategyContext setSecondaryContainerName(String secondaryContainerName) {
		this.secondaryContainerName = secondaryContainerName;
		return this;
	}
	
	public AzureStrategyContext setSecondaryLeaseId(String secondaryLeaseId) {
		this.secondaryLeaseId = secondaryLeaseId;
		return this;
	}
	
	public AzureStrategyContext setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		return this;
	}
	
	public AzureStrategyContext setSequenceNumberAction(SequenceNumberAction sequenceNumberAction) {
		this.sequenceNumberAction = sequenceNumberAction;
		return this;
	}
	
	public AzureStrategyContext setSnapshot(String snapshot) {
		this.snapshot = snapshot;
		return this;
	}
	
	public AzureStrategyContext setSnapshotDeleteAction(SnapshotDeleteAction snapshotDeleteAction) {
		this.snapshotDeleteAction = snapshotDeleteAction;
		return this;
	}
	
	public AzureStrategyContext setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public AzureStrategyContext setTimeToLiveInterval(int timeToLiveInterval) {
		this.timeToLiveInterval = timeToLiveInterval;
		return this;
	}
	
	public AzureStrategyContext setTop(Integer top) {
		this.top = top;
		return this;
	}
	
	public AzureStrategyContext setVisibilityTimeout(int visibilityTimeout) {
		this.visibilityTimeout = visibilityTimeout;
		return this;
	}
	
	// Properties in common
	private Date date;
	private String httpBody;
	private String marker;
	private int maxResults;
	private String prefix;
	private AzureOperation operation;
	
	// Blob Operations
	private BlobData blobData;
	private String blobName;
	private int blobSequenceNumber;
	private BlobType blobType;
	private String blockId;
	private Condition condition;
	private String containerName;
	private ContainerAccess containerAccess;
	private String delimiter;
	private Set<EnumerationFilter> enumerationFilter;
	private LeaseAction leaseAction;
	private String leaseId;
	private int maxPageSize;
	private PutPageAction putPageAction;
	private BlobRange range;
	private String secondaryBlobName;
	private String secondaryContainerName;
	private String secondaryLeaseId;
	private Integer sequenceNumber;
	private SequenceNumberAction sequenceNumberAction;
	private String snapshot;
	private SnapshotDeleteAction snapshotDeleteAction;
	
	// Queue Operations
	private String messageId;
	private MetadataCollection metadata;
	private int numberOfMessages;
	private String popReceipt;
	private String queueName;
	private int timeToLiveInterval;
	private int visibilityTimeout;
	
	// Table Operations
	private Filter filter;
	private IfMatch ifMatch;
	private String partitionKey;
	private String rowKey;
	private String tableName;
	private Integer top;
}
