package com.windowsazure.samples.internal;

import com.windowsazure.samples.internal.web.HttpMethod;


public enum AzureOperation {
	
	// Blob Operation
	ListContainers(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	CreateContainer(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetContainerProperties(HttpMethod.GET, AzureOperationReturnType.EMPTY),
	GetContainerMetadata(HttpMethod.GET, AzureOperationReturnType.EMPTY),
	SetContainerMetadata(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetContainerACL(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	SetContainerACL(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	DeleteContainer(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	ListBlobs(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	PutBlob(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetBlob(HttpMethod.GET, AzureOperationReturnType.BLOB),
	GetBlobProperties(HttpMethod.HEAD, AzureOperationReturnType.EMPTY),
	SetBlobProperties(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetBlobMetadata(HttpMethod.GET, AzureOperationReturnType.EMPTY),
	SetBlobMetadata(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	DeleteBlob(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	LeaseBlob(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	SnapshotBlob(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	CopyBlob(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	PutBlock(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	PutBlockList(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetBlockList(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	PutPage(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	GetPageRegions(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	
	// Queue Operations
	ListQueues(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	CreateQueue(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	DeleteQueue(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	GetQueueMetadata(HttpMethod.GET, AzureOperationReturnType.EMPTY),
	SetQueueMetadata(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	PutMessage(HttpMethod.POST, AzureOperationReturnType.EMPTY),
	GetMessages(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	PeekMessages(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	DeleteMessage(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	ClearMessages(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	
	// Table Operations
	QueryTables(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	CreateTable(HttpMethod.POST, AzureOperationReturnType.XML_STRING),
	DeleteTable(HttpMethod.DELETE, AzureOperationReturnType.EMPTY),
	QueryTableEntities(HttpMethod.GET, AzureOperationReturnType.XML_STRING),
	InsertTableEntity(HttpMethod.POST, AzureOperationReturnType.XML_STRING),
	UpdateTableEntity(HttpMethod.PUT, AzureOperationReturnType.EMPTY),
	MergeTableEntity(HttpMethod.MERGE, AzureOperationReturnType.EMPTY),
	DeleteTableEntity(HttpMethod.DELETE, AzureOperationReturnType.EMPTY);
	
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	
	public AzureOperationReturnType getReturnType() {
		return returnType;
	}
	
	private AzureOperation(HttpMethod httpMethod, AzureOperationReturnType returnType) {
		this.httpMethod = httpMethod;
		this.returnType = returnType;
	}
	
	private HttpMethod httpMethod;
	private AzureOperationReturnType returnType;
}
