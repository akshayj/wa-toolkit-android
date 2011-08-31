package com.windowsazure.samples.blob;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.condition.CopyBlobCondition;
import com.windowsazure.samples.blob.condition.DeleteBlobCondition;
import com.windowsazure.samples.blob.condition.DeleteContainerCondition;
import com.windowsazure.samples.blob.condition.LeaseBlobCondition;
import com.windowsazure.samples.blob.condition.PutBlobCondition;
import com.windowsazure.samples.blob.condition.PutBlockListCondition;
import com.windowsazure.samples.blob.condition.PutPageCondition;
import com.windowsazure.samples.blob.condition.SetBlobMetadataCondition;
import com.windowsazure.samples.blob.condition.SetBlobPropertiesCondition;
import com.windowsazure.samples.blob.condition.SetContainerMetadataCondition;
import com.windowsazure.samples.blob.condition.SnapshotBlobCondition;
import com.windowsazure.samples.blob.data.BlobData;


public interface BlobWriter {
	public BlobOperationResponse createContainer(String containerName, MetadataCollection metadata, ContainerAccess containerAccess);
	public BlobOperationResponse setContainerMetadata(String containerName, MetadataCollection metadata, SetContainerMetadataCondition condition);
	public BlobOperationResponse setContainerACL(String containerName, ContainerAccess containerAccess, ACLCollection acl);
	public BlobOperationResponse deleteContainer(String containerName, DeleteContainerCondition condition);
	
	// The following two methods implement the Put Blob operation.
	public BlobOperationResponse putBlockBlob(String containerName, String blobName, String leaseId, MetadataCollection metadata, BlobData blobData, PutBlobCondition condition);
	public BlobOperationResponse initializePageBlob(String containerName, String blobName, String leaseId, MetadataCollection metadata, int maxSize, Integer blobSequenceNumber, PutBlobCondition condition);

	public BlobOperationResponse setBlockBlobProperties(
			String containerName,
			String blobName,
			String leaseId,
			SetBlobPropertiesCondition condition,
			String cacheControl,
			String contentType,
			String contentMd5,
			String contentEncoding,
			String contentLanguage);
	
	public BlobOperationResponse setPageBlobProperties(
			String containerName,
			String blobName,
			String leaseId,
			SetBlobPropertiesCondition condition,
			String cacheControl,
			String contentType,
			String contentMd5,
			String contentEncoding,
			String contentLanguage,
			Integer contentLength,
			SequenceNumberAction sequenceNumberAction,
			Integer sequenceNumber);
	
	public BlobOperationResponse setBlobMetadata(
			String containerName,
			String blobName,
			String leaseId,
			SetBlobMetadataCondition condition,
			MetadataCollection metadata);
	
	public BlobOperationResponse deleteBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition);
	public BlobOperationResponse deleteSnapshotBlob(
			String containerName,
			String blobName,
			String leaseId,
			DeleteBlobCondition condition,
			String snapshot,
			SnapshotDeleteAction action);
	
	public BlobOperationResponse leaseBlob(
			String containerName,
			String blobName,
			String leaseId,
			LeaseAction action,
			LeaseBlobCondition condition);
	
	public BlobOperationResponse snapshotBlob(
			String containerName,
			String blobName,
			String leaseId,
			SnapshotBlobCondition condition,
			MetadataCollection metadata);
	
	public BlobOperationResponse copyBlob(String sourceContainerName, String sourceBlobName, String sourceLeaseId, String sourceSnapshot, 
			                              String destinationContainerName, String destinationBlobName, String destinationLeaseId,
			                              CopyBlobCondition condition, MetadataCollection metadata);
	
	public BlobOperationResponse putBlock(String containerName, String blobName, String leaseId, Block block, BlobData blobData);
	
	public BlobOperationResponse putBlockList(String containerName, String blobName, String leaseId, BlockList blockList);
	public BlobOperationResponse putBlockList(
			String containerName,
			String blobName,
			String leaseId,
			PutBlockListCondition condition,
			BlockList blockList,
			MetadataCollection metadata,
			String cacheControl,
			String contentType,
			String contentEncoding,
			String contentLanguage,
			String contentMd5);
	
	public BlobOperationResponse putPage(
			String containerName,
			String blobName,
			BlobRange range,
			PutPageAction action,
			String leaseId,
			PutPageCondition condition,
			BlobData blobData);
}
