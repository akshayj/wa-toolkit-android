package com.windowsazure.samples.blob;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import com.windowsazure.samples.blob.EnumerationFilter;
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
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.internal.AzureManager;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.BlobDataContext;
import com.windowsazure.samples.internal.UnableToBuildStrategyException;
import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.blob.ACLCollectionDOMAdapter;
import com.windowsazure.samples.internal.blob.ACLDOMBuilder;
import com.windowsazure.samples.internal.blob.AzureContainerMetadataBuilder;
import com.windowsazure.samples.internal.blob.BlobCollectionDOMAdapter;
import com.windowsazure.samples.internal.blob.BlobOperationResponseAdapter;
import com.windowsazure.samples.internal.blob.BlockListDOMAdapter;
import com.windowsazure.samples.internal.blob.ContainerCollectionDOMAdapter;
import com.windowsazure.samples.internal.blob.DirectConnectBlobStrategy;
import com.windowsazure.samples.internal.blob.PageRangeCollectionDOMAdapter;
import com.windowsazure.samples.internal.blob.ProxyBlobStrategy;
import com.windowsazure.samples.internal.blob.PutBlockListDOMBuilder;


public final class AzureBlobManager extends AzureManager implements BlobReader, BlobWriter {

	public AzureBlobManager(AuthenticationToken token)
		throws Exception {
		
		super(token);
	}
	
	@Override
	public BlobOperationResponse copyBlob(
			String sourceContainerName,
			String sourceBlobName,
			String sourceLeaseId,
			String sourceSnapshot,
			String destinationContainerName,
			String destinationBlobName,
			String destinationLeaseId,
			CopyBlobCondition condition,
			MetadataCollection metadata) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.CopyBlob)
				.setBlobName(sourceBlobName)
				.setCondition(condition)
				.setContainerName(sourceContainerName)
				.setDate(new Date())
				.setLeaseId(sourceLeaseId)
				.setMetadata(metadata)
				.setSecondaryBlobName(destinationBlobName)
				.setSecondaryContainerName(destinationContainerName)
				.setSecondaryLeaseId(destinationLeaseId)
				.setSnapshot(sourceSnapshot);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse createContainer(String containerName, MetadataCollection metadata, ContainerAccess containerAccess) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.CreateContainer)
				.setContainerAccess(containerAccess)
				.setContainerName(containerName)
				.setDate(new Date())
				.setMetadata(metadata);
			
			return strategy.execute(context, BlobOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse deleteBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition) {
		return deleteSnapshotBlob(containerName, blobName, leaseId, condition, null, null);
	}
	
	@Override
	public BlobOperationResponse deleteContainer(String containerName, DeleteContainerCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.DeleteContainer)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date());
			
			return strategy.execute(context, BlobOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse deleteSnapshotBlob(
			String containerName,
			String blobName,
			String leaseId,
			DeleteBlobCondition condition,
			String snapshot,
			SnapshotDeleteAction action) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.DeleteBlob)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setSnapshot(snapshot)
				.setSnapshotDeleteAction(action);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public AzureBlob getBlob(
			String containerName,
			String blobName,
			String snapshot,
			BlobRange range,
			String leaseId,
			GetBlobCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetBlob)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setRange(range)
				.setSnapshot(snapshot);
			
			return strategy.executeGetBlob(context);
		}
		catch (Exception e)
		{
			AzureBlob response = new AzureBlob(null);
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse getBlobMetadata(
			String containerName,
			String blobName,
			String snapshot,
			String leaseId,
			GetBlobMetadataCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetBlobMetadata)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setSnapshot(snapshot);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse getBlobProperties(
			String containerName,
			String blobName,
			String snapshot,
			String leaseId,
			GetBlobPropertiesCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetBlobProperties)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setSnapshot(snapshot);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlockList getBlockList(String containerName, String blobName, String leaseId, String snapshot) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetBlockList)
				.setBlobName(blobName)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setSnapshot(snapshot);
			
			return strategy.execute(context, BlockListDOMAdapter.class);
		}
		catch (Exception e)
		{
			BlockList blockList = new BlockList();
			blockList.setException(e);
			return blockList;
		}
	}
	
	@Override
	public AzureBlob getBlockBlob(String containerName, String blobName) {
		return getBlob(containerName, blobName, null, null, null, null);
	}
	
	@Override
	public ACLCollection getContainerACL(String containerName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetContainerACL)
				.setContainerName(containerName)
				.setDate(new Date());
			
			ACLCollection collection =  strategy.execute(context, ACLCollectionDOMAdapter.class);
			collection.setContainerName(containerName);
			return collection;
		}
		catch (Exception e)
		{
			ACLCollection collection = new ACLCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public AzureContainerMetadata getContainerMetadata(String containerName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetContainerMetadata)
				.setContainerName(containerName)
				.setDate(new Date());
			
			AzureContainerMetadata metadata =  strategy.execute(context, AzureContainerMetadataBuilder.class);
			metadata.setContainerName(containerName);
			return metadata;
		}
		catch (Exception e)
		{
			AzureContainerMetadata metadata = new AzureContainerMetadata();
			metadata.setException(e);
			return metadata;
		}
	}
	
	@Override
	public AzureContainerMetadata getContainerProperties(String containerName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetContainerProperties)
				.setContainerName(containerName)
				.setDate(new Date());
			
			AzureContainerMetadata metadata =  strategy.execute(context, AzureContainerMetadataBuilder.class);
			metadata.setContainerName(containerName);
			return metadata;
		}
		catch (Exception e)
		{
			AzureContainerMetadata metadata = new AzureContainerMetadata();
			metadata.setException(e);
			return metadata;
		}
	}
	
	@Override
	public AzureBlob getPageBlob(String containerName, String blobName, BlobRange range) {
		return getBlob(containerName, blobName, null, range, null, null);
	}
	
	@Override
	public PageRangeCollection getPageRegions(
			String containerName,
			String blobName,
			String snapshot,
			BlobRange range,
			String leaseId,
			GetPageRegionsCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetPageRegions)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setRange(range)
				.setSnapshot(snapshot);
			
			return strategy.execute(context, PageRangeCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			PageRangeCollection collection = new PageRangeCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public BlobOperationResponse initializePageBlob(
			String containerName,
			String blobName,
			String leaseId,
			MetadataCollection metadata,
			int maxPageSize,
			Integer blobSequenceNumber,
			PutBlobCondition condition) {
		try
		{
			if (blobSequenceNumber == null)
				blobSequenceNumber = 0;
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutBlob)
				.setBlobName(blobName)
				.setBlobSequenceNumber(blobSequenceNumber)
				.setBlobType(BlobType.PAGE_BLOB)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setMaxPageSize(maxPageSize)
				.setMetadata(metadata);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse leaseBlob(
			String containerName,
			String blobName,
			String leaseId,
			LeaseAction action,
			LeaseBlobCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.LeaseBlob)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseAction(action)
				.setLeaseId(leaseId);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public AzureBlobCollection listAllBlobs(String containerName) {
		return listBlobs(containerName, null, null, null, null, EnumSet.of(EnumerationFilter.METADATA));
	}
	
	@Override
	public AzureBlobCollection listBlobs(
			String containerName,
			String prefix,
			String delimiter,
			String marker,
			Integer maxResults,
			Set<EnumerationFilter> includes) {
		try
		{
			if (maxResults == null)
				maxResults = MAX_RESULTS_DEFAULT;
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.ListBlobs)
				.setContainerName(containerName)
				.setDate(new Date())
				.setDelimiter(delimiter)
				.setEnumerationFilter(includes)
				.setMarker(marker)
				.setMaxResults(maxResults)
				.setPrefix(prefix);
			
			return strategy.execute(context, BlobCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureBlobCollection collection = new AzureBlobCollection(null);
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public AzureContainerCollection listAllContainers() {
		return listContainers(null, null, null);
	}

	@Override
	public AzureContainerCollection listContainers(String prefix, String marker, Integer maxResults) {
		try
		{
			if (maxResults == null)
				maxResults = MAX_RESULTS_DEFAULT;
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.ListContainers)
				.setDate(new Date())
				.setMarker(marker)
				.setMaxResults(maxResults)
				.setPrefix(prefix);
			
			return strategy.execute(context, ContainerCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureContainerCollection collection = new AzureContainerCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public BlobOperationResponse putBlock(String containerName, String blobName, String leaseId, Block block, BlobData blobData) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutBlock)
				.setBlobData(blobData)
				.setBlobName(blobName)
				.setBlockId(block.getBlockId())
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse putBlockBlob(
			String containerName,
			String blobName,
			String leaseId,
			MetadataCollection metadata,
			BlobData blobData,
			PutBlobCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutBlob)
				.setBlobData(blobData)
				.setBlobName(blobName)
				.setBlobType(BlobType.BLOCK_BLOB)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setMetadata(metadata);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse putBlockList(String containerName, String blobName, String leaseId, BlockList blockList) {
		return putBlockList(containerName, blobName, leaseId, null, blockList,
				null, null, null, null, null, null);
	}

	@Override
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
			String contentMd5) {
		try
		{
			String httpBody = new PutBlockListDOMBuilder(blockList).getXmlString(false);
			BlobDataContext blobData = new BlobDataContext(cacheControl, contentEncoding, contentLanguage, null, contentMd5, contentType);
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutBlockList)
				.setBlobName(blobName)
				.setBlobData(blobData)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setHttpBody(httpBody)
				.setLeaseId(leaseId)
				.setMetadata(metadata);
			
			return strategy.execute(context, BlobOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse putPage(
			String containerName,
			String blobName,
			BlobRange range,
			PutPageAction action,
			String leaseId,
			PutPageCondition condition,
			BlobData blobData) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutPage)
				.setBlobData(blobData)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setPutPageAction(action)
				.setRange(range);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse setBlobMetadata(
			String containerName,
			String blobName,
			String leaseId,
			SetBlobMetadataCondition condition,
			MetadataCollection metadata) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SetBlobMetadata)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setMetadata(metadata);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse setBlockBlobProperties(
			String containerName,
			String blobName,
			String leaseId,
			SetBlobPropertiesCondition condition,
			String cacheControl,
			String contentType,
			String contentMd5,
			String contentEncoding,
			String contentLanguage) {
		
		return setPageBlobProperties(
				containerName,
				blobName,
				leaseId,
				condition,
				cacheControl,
				contentType,
				contentMd5,
				contentEncoding,
				contentLanguage,
				null,
				null,
				null);
	}
	
	@Override
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
			Integer sequenceNumber) {
		try
		{
			BlobDataContext blobData = new BlobDataContext(cacheControl, contentEncoding, contentLanguage, contentLength, contentMd5, contentType);
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SetBlobProperties)
				.setBlobData(blobData)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setSequenceNumber(sequenceNumber)
				.setSequenceNumberAction(sequenceNumberAction);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse setContainerACL(String containerName, ContainerAccess containerAccess, ACLCollection acl) {
		try
		{
			String httpBody = (acl != null && acl.getAclCount() > 0) ? new ACLDOMBuilder(acl).getXmlString(false) : null;
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SetContainerACL)
				.setContainerAccess(containerAccess)
				.setContainerName(containerName)
				.setDate(new Date())
				.setHttpBody(httpBody);
			
			return strategy.execute(context, BlobOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse setContainerMetadata(String containerName, MetadataCollection metadata, SetContainerMetadataCondition condition) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SetContainerMetadata)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setMetadata(metadata);
			
			return strategy.execute(context, BlobOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public BlobOperationResponse snapshotBlob(
			String containerName,
			String blobName,
			String leaseId,
			SnapshotBlobCondition condition,
			MetadataCollection metadata) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SnapshotBlob)
				.setBlobName(blobName)
				.setCondition(condition)
				.setContainerName(containerName)
				.setDate(new Date())
				.setLeaseId(leaseId)
				.setMetadata(metadata);
			
			return strategy.executeBlobDataOperation(context);
		}
		catch (Exception e)
		{
			BlobOperationResponse response = new BlobOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	protected AzureStrategy<? extends AuthenticationToken> buildStrategy(AuthenticationToken token)
		throws UnableToBuildStrategyException {
		
		if (token instanceof DirectConnectToken)
			return new DirectConnectBlobStrategy((DirectConnectToken) token);
		
		if (token instanceof ProxyToken)
			return new ProxyBlobStrategy((ProxyToken) token);
		
		throw new UnableToBuildStrategyException();
	}

	private static final int MAX_RESULTS_DEFAULT = 5000;
}
