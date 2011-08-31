package com.windowsazure.samples.blob;

import java.util.Set;

import com.windowsazure.samples.blob.condition.GetBlobCondition;
import com.windowsazure.samples.blob.condition.GetBlobMetadataCondition;
import com.windowsazure.samples.blob.condition.GetBlobPropertiesCondition;
import com.windowsazure.samples.blob.condition.GetPageRegionsCondition;


public interface BlobReader {
	public AzureContainerCollection listAllContainers();
	
	// maxResults: Optional. Specifies the maximum number of containers to return.
	// If maxResults is null, then a maximum of 5000 items will be returned.
	public AzureContainerCollection listContainers(String prefix, String marker, Integer maxResults);
	
	public AzureContainerMetadata getContainerProperties(String containerName);
	public AzureContainerMetadata getContainerMetadata(String containerName);
	public ACLCollection getContainerACL(String containerName);
	
	// Get all blobs for the specified container.  Includes snapshots, metadata, and uncommitted blobs.
	public AzureBlobCollection listAllBlobs(String containerName);
	
	// The List Blobs operation enumerates the list of blobs under the specified container.
	// prefix: Optional. Filters the results to return only blobs whose names begin with the specified prefix.
	// delimiter: Optional. When the request includes this parameter, the operation returns a BlobPrefix element
	//            in the response body that acts as a placeholder for all blobs whose names begin with the same
	//            substring up to the appearance of the delimiter character. The delimiter may be a single
	//            character or string.
	// marker: Optional. A string value that identifies the portion of the list to be returned with the next list
	//         operation. The operation returns a marker value within the response body if the list returned was
	//         not complete. The marker value may then be used in a subsequent call to request the next set
	//         of list items. The marker value is opaque to the client.
	// maxresults: Optional. Specifies the maximum number of blobs to return, including all BlobPrefix elements.
	//             If the request does not specify maxresults or specifies a value greater than 5000, the server
	//             will return up to 5000 items.
	// includes: Optional. Specifies that the response should include one or more of the following subsets:
	//           SNAPSHOTS: Specifies that snapshots should be included in the enumeration.
	//           METADATA: Specifies that blob metadata should be returned in the response.
	//           UNCOMITTED_BLOBS: Specifies that blobs for which blocks have been uploaded, but which have not
	//                             been committed using Put Block List, be included in the response.
	public AzureBlobCollection listBlobs(
			String containerName,
			String prefix,
			String delimiter,
			String marker,
			Integer maxResults,
			Set<EnumerationFilter> includes);
	
	// The following three procedures implement the Get Blob operation.
	// snapshot: The timestamp of the desired snapshot.
	// range: Optional. Return only the bytes in the blob in the specified range.
	// leaseId: Optional. This will return a blob only if the blob's lease is currently active and the lease ID
	//          matches that of the blob.
	public AzureBlob getBlockBlob(String containerName, String blobName);
	public AzureBlob getPageBlob(String containerName, String blobName, BlobRange range);
	public AzureBlob getBlob(String containerName, String blobName, String snapshot, BlobRange range, String leaseId, GetBlobCondition condition);
	
	public BlobOperationResponse getBlobProperties(String containerName, String blobName, String snapshot, String leaseId, GetBlobPropertiesCondition condition);
	public BlobOperationResponse getBlobMetadata(String containerName, String blobName, String snapshot, String leaseId, GetBlobMetadataCondition condition);
	
	public BlockList getBlockList(String containerName, String blobName, String leaseId, String snapshot);
	
	public PageRangeCollection getPageRegions(
			String containerName,
			String blobName,
			String snapshot,
			BlobRange range,
			String leaseId,
			GetPageRegionsCondition condition);
}
