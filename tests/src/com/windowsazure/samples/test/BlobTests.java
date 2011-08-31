package com.windowsazure.samples.test;

import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.blob.ACL;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.AzureBlobCollection;
import com.windowsazure.samples.blob.AzureBlobManager;
import com.windowsazure.samples.blob.AzureContainer;
import com.windowsazure.samples.blob.AzureContainerCollection;
import com.windowsazure.samples.blob.AzureContainerMetadata;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobReader;
import com.windowsazure.samples.blob.BlobWriter;
import com.windowsazure.samples.blob.Block;
import com.windowsazure.samples.blob.BlockList;
import com.windowsazure.samples.blob.ContainerAccess;
import com.windowsazure.samples.blob.BlobOperationResponse;
import com.windowsazure.samples.blob.LeaseAction;
import com.windowsazure.samples.blob.PageRangeCollection;
import com.windowsazure.samples.blob.Permission;
import com.windowsazure.samples.blob.PutPageAction;
import com.windowsazure.samples.blob.condition.GetBlobCondition;
import com.windowsazure.samples.blob.data.OctetBlobData;
import com.windowsazure.samples.blob.data.TextBlobData;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.mock.MockBlobManager;

import junit.framework.Assert;
import android.test.AndroidTestCase;


public class BlobTests extends AndroidTestCase {

	public void testACL() {
		try
		{
			if (isProxyTest() || isMockTest())
				return;
			
			String containerName = generateContainerName("testacl");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			ACLCollection aclCollection = new ACLCollection();
			Date start = new Date(2012 - 1900, 1 - 1, 1);
			Date expiry = new Date(2012 - 1900, 1 - 1, 2);
			aclCollection.add(ACL.newACL(start, expiry, Permission.FULL));
			BlobOperationResponse response = getWriter().setContainerACL(containerName, ContainerAccess.CONTAINER, aclCollection);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			
			ACLCollection collection = getReader().getContainerACL(containerName);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(containerName, collection.getContainerName());
			Assert.assertEquals(ContainerAccess.CONTAINER, collection.getContainerAccess());
			Assert.assertEquals(1, collection.getAclCount());
			
			ACL expected = aclCollection.iterator().next();
			ACL actual = collection.iterator().next();
			Assert.assertEquals(expected.getId(), actual.getId());
			Assert.assertEquals(expiry, actual.getExpiry());
			
			boolean canDelete = actual.getPermissions().contains(Permission.DELETE);
			Assert.assertTrue(canDelete);
			
			response = getWriter().setContainerACL(containerName, ContainerAccess.BLOB, null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			collection = getReader().getContainerACL(containerName);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(ContainerAccess.BLOB, collection.getContainerAccess());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testBlobMetadata() {
		try
		{
			if (isProxyTest()) {
				MetadataCollection metadata = new MetadataCollection();
				metadata.add("value1", "Red");
				metadata.add("value2", "Blue");
				
				String blobName = "metadatatest";
				String data = "Blob for metadata test.";
				TextBlobData blobData = new TextBlobData(data);
				BlobOperationResponse response = getWriter().putBlockBlob(null, blobName, null, metadata, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
				
				response = getReader().getBlobMetadata(null, blobName, null, null, null);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				metadata = response.getMetadata();
				Assert.assertEquals(2, metadata.getCount());
				Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
				Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
				
				metadata = new MetadataCollection();
				metadata.add("value1", "NotRed");
				metadata.add("value3", "Green");
				response = getWriter().setBlobMetadata(null, blobName, null, null, metadata);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				response = getReader().getBlobMetadata(null, blobName, null, null, null);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				metadata = response.getMetadata();
				Assert.assertEquals(2, metadata.getCount());
				Assert.assertEquals("NotRed", metadata.getMetadata("value1").getValue());
				Assert.assertEquals("Green", metadata.getMetadata("value3").getValue());
				
				getWriter().deleteBlob(null, blobName, null, null);
			} else {
				String containerName = generateContainerName("testcontainera");
				getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
				
				MetadataCollection metadata = new MetadataCollection();
				metadata.add("value1", "Red");
				metadata.add("value2", "Blue");
				
				String blobName = "metadatatest";
				String data = "Blob for metadata test.";
				TextBlobData blobData = new TextBlobData(data);
				BlobOperationResponse response = getWriter().putBlockBlob(containerName, blobName, null, metadata, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
				
				response = getReader().getBlobMetadata(containerName, blobName, null, null, null);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				metadata = response.getMetadata();
				Assert.assertEquals(2, metadata.getCount());
				Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
				Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
				
				metadata = new MetadataCollection();
				metadata.add("value1", "NotRed");
				metadata.add("value3", "Green");
				response = getWriter().setBlobMetadata(containerName, blobName, null, null, metadata);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				response = getReader().getBlobMetadata(containerName, blobName, null, null, null);
				Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
				
				metadata = response.getMetadata();
				Assert.assertEquals(2, metadata.getCount());
				Assert.assertEquals("NotRed", metadata.getMetadata("value1").getValue());
				Assert.assertEquals("Green", metadata.getMetadata("value3").getValue());
				
				getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testBlobProperties() {
		try
		{
			if (isProxyTest())
				return;
				
			String containerName = generateContainerName("testcontainerb");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "propertytest";
			String data = "Blob for property test.";
			TextBlobData blobData = new TextBlobData(data);
			getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
			
			BlobOperationResponse response = getReader().getBlobProperties(containerName, blobName, null, null, null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			Assert.assertEquals(blobData.getContentEncoding(), response.getContentEncoding());
			
			response = getWriter().setBlockBlobProperties(containerName, blobName, null, null, null, null, null, "big5", null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			
			response = getReader().getBlobProperties(containerName, blobName, null, null, null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			Assert.assertEquals("big5", response.getContentEncoding());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testBlockOperations() {
		try
		{
			if (isProxyTest() || isMockTest())
				return;
			
			String containerName = generateContainerName("testblockoperations");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "stooges";
			TextBlobData blobData = new TextBlobData("Blob for block tests.");
			getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
			
			Block blockIn = Block.newBlock();
			blobData = new TextBlobData("Moe Howard");
			BlobOperationResponse response = getWriter().putBlock(containerName, blobName, null, blockIn, blobData);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			
			BlockList blockList = getReader().getBlockList(containerName, blobName, null, null);
			Assert.assertEquals(HttpStatusCode.OK, blockList.getHttpStatusCode());
			Assert.assertEquals(0, blockList.getCommittedBlocks().size());
			Assert.assertEquals(1, blockList.getUncommittedBlocks().size());
			
			Block blockOut = blockList.getUncommittedBlocks().iterator().next();
			Assert.assertEquals(blockIn.getBlockId(), blockOut.getBlockId());
			Assert.assertEquals("Moe Howard".length(), (int) blockOut.getSize());
			Assert.assertFalse(blockOut.isCommitted());
			
			response = getWriter().putBlockList(containerName, blobName, null, blockList);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			
			blockList = getReader().getBlockList(containerName, blobName, null, null);
			Assert.assertEquals(1, blockList.getCommittedBlocks().size());
			Assert.assertEquals(0, blockList.getUncommittedBlocks().size());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testConditionalHeaders() {
		try
		{
			if (isProxyTest()) {
				String blobName = "conditional";
				String data = "Conditional Header test";
				TextBlobData blobData = new TextBlobData(data);
				getWriter().putBlockBlob(null, blobName, null, null, blobData, null);
				
				AzureBlob blob = getReader().getBlockBlob(null, blobName);
				String etag = blob.getEtag();
				
				GetBlobCondition condition = new GetBlobCondition();
				condition.ifMatch(etag);
				blob = getReader().getBlob(null, blobName, null, null, null, condition);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				
				condition.ifMatch("bogus");
				blob = getReader().getBlob(null, blobName, null, null, null, condition);
				Assert.assertEquals(HttpStatusCode.PreconditionFailed, blob.getHttpStatusCode());
				
				getWriter().deleteBlob(null, blobName, null, null);
			} else {
				String containerName = generateContainerName("testconditions");
				getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
				
				String blobName = "greetings";
				String data = "Hello, world.";
				TextBlobData blobData = new TextBlobData(data);
				getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
				
				AzureBlobCollection collection = getReader().listAllBlobs(containerName);
				Assert.assertEquals(1, collection.getBlobs().size());
				String etag = collection.getBlobs().iterator().next().getEtag();
				
				GetBlobCondition condition = new GetBlobCondition();
				condition.ifMatch(etag);
				AzureBlob blob = getReader().getBlob(containerName, blobName, null, null, null, condition);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				
				condition.ifMatch("bogus");
				blob = getReader().getBlob(containerName, blobName, null, null, null, condition);
				Assert.assertEquals(HttpStatusCode.PreconditionFailed, blob.getHttpStatusCode());
				
				getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testContainerMetadata() {
		try
		{
			if (isProxyTest())
				return;
				
			String containerName = generateContainerName("testcontainerc");
			
			MetadataCollection metadata = new MetadataCollection();
			metadata.add("value1", "Red");
			metadata.add("value2", "Blue");
			getWriter().createContainer(containerName, metadata, ContainerAccess.PRIVATE);
			
			AzureContainer queue = getContainer(containerName);
			metadata = queue.getMetadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
			
			AzureContainerMetadata containerMetadata = getReader().getContainerMetadata(containerName);
			metadata = containerMetadata.getMetatadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
			
			metadata = new MetadataCollection();
			metadata.add("value1", "NotRed");
			metadata.add("value3", "Green");
			getWriter().setContainerMetadata(containerName, metadata, null);
			
			containerMetadata = getReader().getContainerMetadata(containerName);
			metadata = containerMetadata.getMetatadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("NotRed", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Green", metadata.getMetadata("value3").getValue());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testCopyBlob() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontainerd");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String sourceBlobName = "source";
			String destinationBlobName = "destination";
			String data = "Blob for copy test.";
			TextBlobData blobData = new TextBlobData(data);
			getWriter().putBlockBlob(containerName, sourceBlobName, null, null, blobData, null);
			
			AzureBlobCollection collection = getReader().listAllBlobs(containerName);
			Assert.assertEquals(1, collection.getBlobs().size());
			
			BlobOperationResponse response = getWriter().copyBlob(containerName, sourceBlobName, null, null,
					containerName, destinationBlobName, null, null, null);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			
			collection = getReader().listAllBlobs(containerName);
			Assert.assertEquals(2, collection.getBlobs().size());
			
			AzureBlob blob = getReader().getBlockBlob(containerName, destinationBlobName);
			blobData = TextBlobData.fromBlob(blob);
			Assert.assertEquals(data, blobData.getText());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testCreateContainer() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontainere");
			MetadataCollection metadata = new MetadataCollection();
			EntityBase entity = getWriter().createContainer(containerName, metadata, ContainerAccess.PRIVATE);
			Assert.assertEquals(HttpStatusCode.Created, entity.getHttpStatusCode());
			
			AzureContainer container = getContainer(containerName);
			Assert.assertNotNull(container);
			
			getWriter().deleteContainer(containerName, null);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteAllTestContainers() {
		try
		{
			if (isProxyTest())
				return;
			
			AzureContainerCollection containers = getReader().listAllContainers();
			for (AzureContainer container : containers) {
				String containerName = container.getContainerName();
				if (containerName.indexOf("test") == 0)
					getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteBlob() {
		try
		{
			if (isProxyTest()) {
				String blobName = "greetings";
				String data = "Hello, world.";
				TextBlobData blobData = new TextBlobData(data);
				getWriter().putBlockBlob(null, blobName, null, null, blobData, null);
				
				AzureBlobCollection collection = getReader().listAllBlobs(null);
				int blobCount = collection.getBlobs().size();
				
				BlobOperationResponse response = getWriter().deleteBlob(null, blobName, null, null);
				Assert.assertEquals(HttpStatusCode.Accepted, response.getHttpStatusCode());
				
				collection = getReader().listAllBlobs(null);
				Assert.assertEquals(blobCount - 1, collection.getBlobs().size());
			} else {
				String containerName = generateContainerName("testcontainerf");
				getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
				
				String blobName = "greetings";
				String data = "Hello, world.";
				TextBlobData blobData = new TextBlobData(data);
				getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
				
				AzureBlobCollection collection = getReader().listAllBlobs(containerName);
				Assert.assertEquals(1, collection.getBlobs().size());
				
				BlobOperationResponse response = getWriter().deleteBlob(containerName, blobName, null, null);
				Assert.assertEquals(HttpStatusCode.Accepted, response.getHttpStatusCode());
				
				collection = getReader().listAllBlobs(containerName);
				Assert.assertEquals(0, collection.getBlobs().size());
				
				getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteContainer() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontainerg");
			MetadataCollection metadata = new MetadataCollection();
			getWriter().createContainer(containerName, metadata, ContainerAccess.PRIVATE);
			AzureContainer container = getContainer(containerName);
			Assert.assertNotNull(container);
			
			EntityBase entity = getWriter().deleteContainer(containerName, null);
			Assert.assertEquals(HttpStatusCode.Accepted, entity.getHttpStatusCode());
			
			container = getContainer(containerName);
			Assert.assertNull(container);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testGetAllBlobs() {
		try
		{
			if (isProxyTest()) {
				AzureBlobCollection collection = getReader().listAllBlobs(null);
				int blobCount = 0;
				int successCount = 0;
				int failureCount = 0;
				
				for (AzureBlob item : collection) {
					++blobCount;
					AzureBlob blob = getReader().getBlob(null, item.getBlobName(), null, null, null, null);
					if (blob.getHttpStatusCode().isSuccess())
						++successCount;
					else
						++failureCount;
				}
				
				Assert.assertEquals(blobCount, successCount);
			}
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testGetContainerProperties() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontainerh");
			
			MetadataCollection metadata = new MetadataCollection();
			metadata.add("value1", "Red");
			metadata.add("value2", "Blue");
			getWriter().createContainer(containerName, metadata, ContainerAccess.PRIVATE);
			
			AzureContainerMetadata containerMetadata = getReader().getContainerProperties(containerName);
			Assert.assertEquals(HttpStatusCode.OK, containerMetadata.getHttpStatusCode());
			metadata = containerMetadata.getMetatadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testGetPageRegions() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontaineri");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "stooges";
			BlobOperationResponse response = getWriter().initializePageBlob(containerName, blobName, null, null, 2048, null, null);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			
			PageRangeCollection collection = getReader().getPageRegions(containerName, blobName, null, null, null, null);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(0, collection.getRanges().size());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testLeaseBlob() {
		try
		{
			if (isProxyTest())
				return;

			String containerName = generateContainerName("testcontainerj");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "leasee";
			String data = "test blob for lease operations.";
			TextBlobData blobData = new TextBlobData(data);
			getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
			
			// Acquire a lease
			BlobOperationResponse response = getWriter().leaseBlob(containerName, blobName, null, LeaseAction.ACQUIRE, null);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			String leaseId = response.getLeaseId();
			Assert.assertFalse(Util.isStringNullOrEmpty(leaseId));
			
			// Release it
			response = getWriter().leaseBlob(containerName, blobName, leaseId, LeaseAction.RELEASE, null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			
			// Acquire another
			response = getWriter().leaseBlob(containerName, blobName, null, LeaseAction.ACQUIRE, null);
			leaseId = response.getLeaseId();
			
			// Renew it
			response = getWriter().leaseBlob(containerName, blobName, leaseId, LeaseAction.RENEW, null);
			Assert.assertEquals(HttpStatusCode.OK, response.getHttpStatusCode());
			leaseId = response.getLeaseId();
			Assert.assertFalse(Util.isStringNullOrEmpty(leaseId));
			
			// Break it
			response = getWriter().leaseBlob(containerName, blobName, leaseId, LeaseAction.BREAK, null);
			Assert.assertEquals(HttpStatusCode.Accepted, response.getHttpStatusCode());
			Integer leaseTime = response.getLeaseTime();
			Assert.assertNotNull(leaseTime);
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testListBlobs() {
		try
		{
			AzureBlobCollection collection = getReader().listAllBlobs("images");
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertTrue(collection.getBlobs().size() > 0);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testListContainers() {
		try
		{
			if (isProxyTest())
				return;
			
			AzureContainerCollection collection = getReader().listAllContainers();
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertTrue(collection.getContainers().size() > 0);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testPutOctetBlockBlob() {
		try
		{
			if (isProxyTest()) {
				String blobName = "greetings";
				String data = "Hello, world.";
				OctetBlobData blobData = new OctetBlobData(data.getBytes());
				BlobOperationResponse putResponse = getWriter().putBlockBlob(null, blobName, null, null, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, putResponse.getHttpStatusCode());
				
				AzureBlob blob = getReader().getBlockBlob(null, blobName);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				blobData = OctetBlobData.fromBlob(blob);
				Assert.assertEquals(data, new String(blobData.getBytes()));
			} else {
				String containerName = generateContainerName("testcontainerk");
				getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
				
				String blobName = "greetings";
				String data = "Hello, world.";
				OctetBlobData blobData = new OctetBlobData(data.getBytes());
				BlobOperationResponse putResponse = getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, putResponse.getHttpStatusCode());
				
				AzureBlob blob = getReader().getBlockBlob(containerName, blobName);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				blobData = OctetBlobData.fromBlob(blob);
				Assert.assertEquals(data, new String(blobData.getBytes()));
				
				getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testPutPage() {
		try
		{
			if (isProxyTest())
				return;
			
			String containerName = generateContainerName("testcontainerl");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "stooges";
			BlobOperationResponse response = getWriter().initializePageBlob(containerName, blobName, null, null, 2048, null, null);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			
			BlobRange range = BlobRange.fromStartAndLength(0, 512);
			OctetBlobData blobData = new OctetBlobData("Moe Howard");
			response = getWriter().putPage(containerName, blobName, range, PutPageAction.UPDATE, null, null, blobData);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());

			AzureBlob blob = getReader().getPageBlob(containerName, blobName, range);
			blobData = OctetBlobData.fromBlob(blob);
			Assert.assertEquals("Moe Howard", blobData.getAsString());
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testPutStringBlockBlob() {
		try
		{
			if (isProxyTest()) {
				String blobName = "greetings";
				String data = "Hello, world.";
				TextBlobData blobData = new TextBlobData(data);
				BlobOperationResponse putResponse = getWriter().putBlockBlob(null, blobName, null, null, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, putResponse.getHttpStatusCode());
				
				AzureBlob blob = getReader().getBlockBlob(null, blobName);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				blobData = TextBlobData.fromBlob(blob);
				Assert.assertEquals(data, blobData.getText());
				
				getWriter().deleteBlob(null, blobName, null, null);
			} else {
				String containerName = generateContainerName("testcontainerm");
				getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
				
				String blobName = "greetings";
				String data = "Hello, world.";
				TextBlobData blobData = new TextBlobData(data);
				BlobOperationResponse putResponse = getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
				Assert.assertEquals(HttpStatusCode.Created, putResponse.getHttpStatusCode());
				
				AzureBlob blob = getReader().getBlockBlob(containerName, blobName);
				Assert.assertEquals(HttpStatusCode.OK, blob.getHttpStatusCode());
				blobData = TextBlobData.fromBlob(blob);
				Assert.assertEquals(data, blobData.getText());
				
				getWriter().deleteContainer(containerName, null);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testSnapshotBlob() {
		try
		{
			if (isProxyTest())
				return;

			String containerName = generateContainerName("testcontainern");
			getWriter().createContainer(containerName, null, ContainerAccess.PRIVATE);
			
			String blobName = "subject";
			String data = "Snapshot me.";
			TextBlobData blobData = new TextBlobData(data);
			getWriter().putBlockBlob(containerName, blobName, null, null, blobData, null);
			
			BlobOperationResponse response = getWriter().snapshotBlob(containerName, blobName, null, null, null);
			Assert.assertEquals(HttpStatusCode.Created, response.getHttpStatusCode());
			String snapshot = response.getSnapshot();
			Assert.assertFalse(Util.isStringNullOrEmpty(snapshot));
			
			getWriter().deleteContainer(containerName, null);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	private String generateContainerName(String prefix)
		throws Exception {
		
		AzureContainerCollection containers = getReader().listAllContainers();
		int containerCount = containers.getContainers().size();
		return prefix + containerCount;
	}
	
	private AuthenticationToken getAuthenticationToken()
		throws NotAuthenticatedException {
		
		if (token == null) {
			token = USE_PROXY ?
			    AuthenticationTests.buildProxyToken() :
			    AuthenticationTests.buildDirectConnectToken();
		}
		return token;
	}
	
	private AzureContainer getContainer(String containerName)
		throws Exception {
		
		AzureContainerCollection containers = getReader().listAllContainers();
		for (AzureContainer container : containers) {
			if (container.getContainerName().equals(containerName))
				return container;
		}
		return null;
	}
	
	private BlobReader getReader()
		throws Exception {
		
		if (blobReader == null) {
			AuthenticationToken token = getAuthenticationToken();
			blobReader = USE_MOCK ?
		        new MockBlobManager(token) :
		        new AzureBlobManager(token);
		}
		return blobReader;
	}
	
	private BlobWriter getWriter()
		throws Exception {
		
		if (blobWriter == null) {
			AuthenticationToken token = getAuthenticationToken();
			blobWriter = USE_MOCK ?
		        new MockBlobManager(token) :
		        new AzureBlobManager(token);
		}
		return blobWriter;
	}
	
	private boolean isMockTest() {
		return USE_MOCK;
	}
	
	private boolean isProxyTest() {
		return (! USE_MOCK && USE_PROXY);
	}
	
	private static final Boolean USE_MOCK = false;
	private static final Boolean USE_PROXY = false;
	
	private BlobReader blobReader;
	private BlobWriter blobWriter;
	private AuthenticationToken token;
}
