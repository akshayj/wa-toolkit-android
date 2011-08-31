package com.windowsazure.samples.mock;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobCollection;
import com.windowsazure.samples.blob.AzureContainer;
import com.windowsazure.samples.blob.AzureContainerCollection;
import com.windowsazure.samples.blob.AzureContainerMetadata;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobReader;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.BlobWriter;
import com.windowsazure.samples.blob.Block;
import com.windowsazure.samples.blob.BlockList;
import com.windowsazure.samples.blob.ContainerAccess;
import com.windowsazure.samples.blob.EnumerationFilter;
import com.windowsazure.samples.blob.BlobOperationResponse;
import com.windowsazure.samples.blob.LeaseAction;
import com.windowsazure.samples.blob.PageRangeCollection;
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
import com.windowsazure.samples.blob.data.TextBlobData;
import com.windowsazure.samples.internal.AzureManager;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.BlobDataContext;
import com.windowsazure.samples.internal.UnableToBuildStrategyException;
import com.windowsazure.samples.internal.blob.AzureBlobCollectionSetter;
import com.windowsazure.samples.internal.blob.AzureBlobSetter;


public final class MockBlobManager extends AzureManager implements BlobReader, BlobWriter {

	public MockBlobManager(AuthenticationToken token)
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
		
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer sourceContainer = getContainer(sourceContainerName);
		Blob sourceBlob = getMockBlob(sourceContainerName, sourceBlobName);
		AzureContainer destinationContainer = getContainer(destinationContainerName);
		if (sourceContainer == null || sourceBlob == null || destinationContainer == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		Blob destinationBlob = getMockBlob(destinationContainerName, destinationBlobName);
		if (destinationBlob != null)
			blobs.remove(destinationBlob);
		
		Blob newBlob = new Blob();
		newBlob.blobData = sourceBlob.blobData;
		newBlob.blobName = destinationBlobName;
		newBlob.blobType = sourceBlob.blobType;
		newBlob.containerName = destinationContainerName;
		newBlob.metadata = metadata;
		newBlob.properties = sourceBlob.properties;
		newBlob.range = sourceBlob.range;
		blobs.add(newBlob);
		
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	@Override
	public BlobOperationResponse createContainer(String containerName, MetadataCollection metadata, ContainerAccess containerAccess) {
		BlobOperationResponse response = new BlobOperationResponse();
		
		AzureContainer container = getContainer(containerName);
		if (container != null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		containers.add(new AzureContainer(containerName, "http://mock/blob/" + containerName, new Date(), UUID.randomUUID().toString(), metadata));
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	@Override
	public BlobOperationResponse deleteBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		blobs.remove(mockBlob);
		response.setHttpStatusCode(HttpStatusCode.Accepted);
		return response;
	}
	
	@Override
	public BlobOperationResponse deleteContainer(String containerName, DeleteContainerCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		containers.remove(container);
		response.setHttpStatusCode(HttpStatusCode.Accepted);
		return response;
	}
	
	@Override
	public BlobOperationResponse deleteSnapshotBlob(String containerName, String blobName, String leaseId, DeleteBlobCondition condition, String snapshot, SnapshotDeleteAction action) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AzureBlob getBlob(String containerName, String blobName, String snapshot, BlobRange range, String leaseId, GetBlobCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BlobOperationResponse getBlobMetadata(String containerName, String blobName, String snapshot, String leaseId, GetBlobMetadataCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		response = new BlobOperationResponse(
		    null,
		    mockBlob.metadata,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    null,
		    UUID.randomUUID().toString(),
		    "mock",
		    new Date());
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
	}
	
	@Override
	public BlobOperationResponse getBlobProperties(String containerName, String blobName, String snapshot, String leaseId, GetBlobPropertiesCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		response = new BlobOperationResponse(
		    null,
		    mockBlob.metadata,
		    mockBlob.blobType,
		    null,
		    null,
		    null,
		    mockBlob.properties.GetContentLength(),
		    mockBlob.properties.getContentType(),
		    null,
		    mockBlob.properties.getContentMd5(),
		    mockBlob.properties.getContentEncoding(),
		    mockBlob.properties.getContentLanguage(),
		    mockBlob.properties.getCacheControl(),
		    null,
		    null,
		    UUID.randomUUID().toString(),
		    "mock",
		    new Date());
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
	}
	
	@Override
	public AzureBlob getBlockBlob(String containerName, String blobName) {
		AzureBlob azureBlob = new AzureBlob(null);
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			azureBlob.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return azureBlob;
		}
		
		AzureBlobSetter setter = new AzureBlobSetter()
			.setBlobType(mockBlob.blobType)
			.setCacheControl(mockBlob.properties.getCacheControl())
			.setContentEncoding(mockBlob.properties.getContentEncoding())
			.setContentLanguage(mockBlob.properties.getContentLanguage())
			.setContentLength(mockBlob.properties.GetContentLength())
			.setContentMd5(mockBlob.properties.getContentMd5())
			.setContentType(mockBlob.properties.getContentType())
			.setMetadata(mockBlob.metadata)
			.setServerData(mockBlob.blobData.encode());
		azureBlob = new AzureBlob(setter);
		azureBlob.setHttpStatusCode(HttpStatusCode.OK);
		return azureBlob;
	}
	
	@Override
	public ACLCollection getContainerACL(String containerName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AzureContainerMetadata getContainerMetadata(String containerName) {
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			AzureContainerMetadata result = new AzureContainerMetadata();
			result.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return result;
		}
		
		AzureContainerMetadata result = new AzureContainerMetadata(
				container.getMetadata(),
				container.getEtag(),
				container.getLastModified(),
				Integer.toString(++requestId),
				"mock",
				new Date());
		result.setHttpStatusCode(HttpStatusCode.OK);
		return result;
	}
	
	@Override
	public AzureContainerMetadata getContainerProperties(String containerName) {
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			AzureContainerMetadata result = new AzureContainerMetadata();
			result.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return result;
		}
		
		AzureContainerMetadata result = new AzureContainerMetadata(
				container.getMetadata(),
				container.getEtag(),
				container.getLastModified(),
				Integer.toString(++requestId),
				"mock",
				new Date());
		result.setHttpStatusCode(HttpStatusCode.OK);
		return result;
	}
	
	@Override
	public AzureBlob getPageBlob(String containerName, String blobName, BlobRange range) {
		AzureBlob azureBlob = new AzureBlob(null);
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockPageBlob(containerName, blobName, range);
		if (container == null || mockBlob == null) {
			azureBlob.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return azureBlob;
		}
		
		AzureBlobSetter setter = new AzureBlobSetter()
			.setBlobType(mockBlob.blobType)
			.setCacheControl(mockBlob.properties.getCacheControl())
			.setContentEncoding(mockBlob.properties.getContentEncoding())
			.setContentLanguage(mockBlob.properties.getContentLanguage())
			.setContentLength(mockBlob.properties.GetContentLength())
			.setContentMd5(mockBlob.properties.getContentMd5())
			.setContentType(mockBlob.properties.getContentType())
			.setMetadata(mockBlob.metadata)
			.setRange(range)
			.setServerData(mockBlob.blobData.encode());
		azureBlob = new AzureBlob(setter);
		azureBlob.setHttpStatusCode(HttpStatusCode.OK);
		return azureBlob;
	}
	
	@Override
	public PageRangeCollection getPageRegions(
			String containerName,
			String blobName,
			String snapshot,
			BlobRange range,
			String leaseId,
			GetPageRegionsCondition condition) {
	
		PageRangeCollection collection = new PageRangeCollection();
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			collection.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return collection;
		}
		
		Vector<BlobRange> ranges = new Vector<BlobRange>();
		for (Blob mockBlob : blobs) {
			if (mockBlob.containerName.equals(containerName) &&
				mockBlob.blobName.equals(blobName))
				ranges.add(mockBlob.range);
		}
		
		collection = new PageRangeCollection(null, null, null, null, null, null, ranges);
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
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
		
		BlobOperationResponse response = new BlobOperationResponse();
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	@Override
	public BlobOperationResponse leaseBlob(String containerName, String blobName, String leaseId, LeaseAction action, LeaseBlobCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		switch (action) {
		case ACQUIRE:
			response = new BlobOperationResponse(null, null, null, UUID.randomUUID().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			response.setHttpStatusCode(HttpStatusCode.Created);
			break;
		case BREAK:
			response = new BlobOperationResponse(null, null, null, null, null, 1, null, null, null, null, null, null, null, null, null, null, null, null);
			response.setHttpStatusCode(HttpStatusCode.Accepted);
			break;
		case RELEASE:
			response.setHttpStatusCode(HttpStatusCode.OK);
			break;
		case RENEW:
			response = new BlobOperationResponse(null, null, null, leaseId, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			response.setHttpStatusCode(HttpStatusCode.OK);
			break;
		}
		return response;
	}
	
	@Override
	public AzureBlobCollection listAllBlobs(String containerName) {
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			AzureBlobCollection result = new AzureBlobCollection(null);
			result.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return result;
		}
		
		Vector<AzureBlob> blobVector = new Vector<AzureBlob>();
		for (Blob blob : blobs) {
			if (blob.containerName.equals(containerName)) {
				AzureBlobSetter blobSetter = new AzureBlobSetter()
				    .setBlobName(blob.blobName)
				    .setBlobType(blob.blobType)
				    .setCacheControl(blob.properties.getCacheControl())
				    .setContentEncoding(blob.properties.getContentEncoding())
				    .setContentLanguage(blob.properties.getContentLanguage())
				    .setContentLength(blob.properties.GetContentLength())
				    .setContentMd5(blob.properties.getContentMd5())
				    .setContentType(blob.properties.getContentType())
				    .setMetadata(blob.metadata)
				    .setUrl("http://mock/blob/" + containerName + "/" + blob.blobName);
				AzureBlob azureBlob = new AzureBlob(blobSetter);
				blobVector.add(azureBlob);
			}
		}
		
		AzureBlobCollectionSetter collectionSetter = new AzureBlobCollectionSetter()
		    .setBlobs(blobVector)
		    .setDate(new Date());
		AzureBlobCollection collection = new AzureBlobCollection(collectionSetter);
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
	}

	@Override
	public AzureContainerCollection listAllContainers() {
		AzureContainerCollection result = new AzureContainerCollection(null, null, null, null, null, containers.size(), containers, null);
		result.setHttpStatusCode(HttpStatusCode.OK);
		return result;
	}
	
	@Override
	public AzureBlobCollection listBlobs(
			String containerName,
			String prefix,
			String delimiter,
			String marker,
			Integer maxResults,
			Set<EnumerationFilter> includes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AzureContainerCollection listContainers(String prefix, String marker, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BlobOperationResponse putBlockBlob(
			String containerName,
			String blobName,
			String leaseId,
			MetadataCollection metadata,
			BlobData blobData,
			PutBlobCondition condition) {
	
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob blob = getMockBlockBlob(containerName, blobName);
		if (container == null || blob != null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		blob = new Blob();
		blob.blobData = blobData;
		blob.blobName = blobName;
		blob.blobType = BlobType.BLOCK_BLOB;
		blob.containerName = containerName;
		blob.metadata = metadata;
		blobs.add(blob);
		
		blob.properties = new BlobDataContext(
			blobData.getCacheControl(),
			blobData.getContentEncoding(),
			blobData.getContentLanguage(),
			blobData.GetContentLength(),
			blobData.getContentMd5(),
			blobData.getContentType());
		
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
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
		
		BlobOperationResponse response = new BlobOperationResponse();
		
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockPageBlob(containerName, blobName, range);
		
		if (container == null ||
			(action == PutPageAction.CLEAR && mockBlob == null)) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		if (mockBlob != null)
			blobs.remove(mockBlob);
		
		if (action == PutPageAction.UPDATE) {
			Blob blob = new Blob();
			blob.blobData = blobData;
			blob.blobName = blobName;
			blob.blobType = BlobType.PAGE_BLOB;
			blob.containerName = containerName;
			blob.range = range;
			blobs.add(blob);
			
			blob.properties = new BlobDataContext(
				blobData.getCacheControl(),
				blobData.getContentEncoding(),
				blobData.getContentLanguage(),
				blobData.GetContentLength(),
				blobData.getContentMd5(),
				blobData.getContentType());
			 
		}
		
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	@Override
	public BlobOperationResponse setBlobMetadata(String containerName, String blobName, String leaseId, SetBlobMetadataCondition condition, MetadataCollection metadata) {
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		mockBlob.metadata = metadata;
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
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
		
		BlobOperationResponse response = new BlobOperationResponse();
		AzureContainer container = getContainer(containerName);
		Blob mockBlob = getMockBlockBlob(containerName, blobName);
		if (container == null || mockBlob == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		mockBlob.properties = new BlobDataContext(cacheControl, contentEncoding, contentLanguage, mockBlob.properties.GetContentLength(), contentMd5, contentType);
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
	}
	
	@Override
	public BlobOperationResponse setContainerACL(String containerName, ContainerAccess containerAccess, ACLCollection acl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlobOperationResponse setContainerMetadata(String containerName, MetadataCollection metadata, SetContainerMetadataCondition condition) {
		BlobOperationResponse response = new BlobOperationResponse();
		
		AzureContainer container = getContainer(containerName);
		if (container == null) {
			response.setHttpStatusCode(HttpStatusCode.ExpectationFailed);
			return response;
		}
		
		AzureContainer newContainer = new AzureContainer(containerName, "http://mock/blob" + containerName, new Date(), container.getEtag(), metadata);
		containers.remove(container);
		containers.add(newContainer);
		
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
	}
	
	@Override
	public BlobOperationResponse snapshotBlob(String containerName, String blobName, String leaseId, SnapshotBlobCondition condition, MetadataCollection metadata) {
		BlobOperationResponse response = new BlobOperationResponse(
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        null,
	        new Date().toString(),
	        null,
	        null,
	        null);
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}

	@Override
	protected AzureStrategy<? extends AuthenticationToken> buildStrategy(
			AuthenticationToken token) throws UnableToBuildStrategyException {

		return null;
	}
	
	private static class Blob {
		public BlobData blobData;
		public String blobName;
		public BlobType blobType;
		public String containerName;
		public MetadataCollection metadata;
		public BlobDataContext properties;
		public BlobRange range;
	}
	
	private Blob getMockBlob(String containerName, String blobName) {
		for (Blob blob : blobs) {
			if (blob.containerName.equals(containerName) &&
				blob.blobName.equals(blobName))
				return blob;
		}
		return null;
	}
	
	private Blob getMockBlockBlob(String containerName, String blobName) {
		for (Blob blob : blobs) {
			if (blob.blobType == BlobType.BLOCK_BLOB &&
				blob.containerName.equals(containerName) &&
				blob.blobName.equals(blobName))
				return blob;
		}
		return null;
	}
	
	private Blob getMockPageBlob(String containerName, String blobName, BlobRange range) {
		for (Blob blob : blobs) {
			if (blob.blobType == BlobType.PAGE_BLOB &&
				blob.containerName.equals(containerName) &&
				blob.blobName.equals(blobName) &&
				blob.range.equals(range))
				return blob;
		}
		return null;
	}
	
	private AzureContainer getContainer(String containerName) {
		for (AzureContainer container : containers) {
			if (container.getContainerName().equals(containerName))
				return container;
		}
		return null;
	}
	
	static {
		Date now = new Date();
		Vector<AzureContainer> containerVector = new Vector<AzureContainer>();
		containerVector.add(new AzureContainer("crab", "http://mock/blob/crab", now, UUID.randomUUID().toString(), new MetadataCollection()));
		containerVector.add(new AzureContainer("lobster", "http://mock/blob/lobster", now, UUID.randomUUID().toString(), new MetadataCollection()));
		containerVector.add(new AzureContainer("shrimp", "http://mock/blob/shrimp", now, UUID.randomUUID().toString(), new MetadataCollection()));
		containerVector.add(new AzureContainer("images", "http://mock/blob/images", now, UUID.randomUUID().toString(), new MetadataCollection()));
		containers = containerVector;
		
		Vector<Blob> blobVector = new Vector<Blob>();
		Blob blob = new Blob();
		blob.blobData = new TextBlobData("I like seafood.");
		blob.blobName = "message";
		blob.blobType = BlobType.BLOCK_BLOB;
		blob.containerName = "images";
		blob.properties = new BlobDataContext(null, null, null, 15, null, "text/plain");
		blobVector.add(blob);
	
		blobs = blobVector;
	}
	
	private static Vector<Blob> blobs;
	private static Vector<AzureContainer> containers;
	private static int requestId = 0;
	
	// Block operations not implemented
	@Override
	public BlobOperationResponse putBlock(String containerName,
			String blobName, String leaseId, Block block, BlobData blobData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockList getBlockList(String containerName, String blobName,
			String leaseId, String snapshot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlobOperationResponse putBlockList(String containerName,
			String blobName, String leaseId, BlockList blockList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlobOperationResponse putBlockList(String containerName,
			String blobName, String leaseId, PutBlockListCondition condition, BlockList blockList,
			MetadataCollection metadata, String cacheControl,
			String contentType, String contentEncoding, String contentLanguage,
			String contentMd5) {
		// TODO Auto-generated method stub
		return null;
	}
}
