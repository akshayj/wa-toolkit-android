package com.windowsazure.samples.internal.blob;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.windowsazure.samples.Metadata;
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
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.authentication.BlobAndQueueServiceSharedKey;
import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.util.Pair;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpMethod;
import com.windowsazure.samples.internal.web.HttpUri;


public final class DirectConnectBlobStrategy extends AzureStrategy<DirectConnectToken> {

	public DirectConnectBlobStrategy(DirectConnectToken token) {
		super(token);
	}

	@Override
	protected void addCommonHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		sharedKey = new BlobAndQueueServiceSharedKey();
		
		headers.put(AzureHttpHeader.XMS_Date, Util.dateToGmtString(context.getDate()));
		headers.put(AzureHttpHeader.XMS_VERSION, AzureHttpHeader.XMS_VERSION_VALUE);
	}

	@Override
	protected void addContentHeaders(AzureStrategyContext context, AzureHttpHeader headers) {
		
		String httpBody = context.getHttpBody();
		int contentLength = (httpBody != null) ? httpBody.length() : 0;
		
		sharedKey.setContentLength(Integer.toString(contentLength));
		headers.put(HttpHeader.CONTENT_LENGTH, contentLength);
			
		headers.put(HttpHeader.ACCEPT_CHARSET, "UTF-8");
		headers.put(HttpHeader.CONTENT_TYPE, null);
	}

	@Override
	protected void addOperationHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		AzureOperation operation = context.getOperation();
		BlobData blobData = context.getBlobData();
		String blobName = context.getBlobName();
		int blobSequenceNumber = context.getBlobSequenceNumber();
		BlobType blobType = context.getBlobType();
		String cacheControl = null;
		Condition condition = context.getCondition();
		ContainerAccess containerAccess = context.getContainerAccess();
		String containerName = context.getContainerName();
		String contentEncoding = null;
		String contentLanguage = null;
		Integer contentLength = null;
		String contentMd5 = null;
		String contentType = null;
		LeaseAction leaseAction = context.getLeaseAction();
		String leaseId = context.getLeaseId();
		int maxPageSize = context.getMaxPageSize();
		MetadataCollection metadata = context.getMetadata();
		PutPageAction putPageAction = context.getPutPageAction();
		BlobRange range = context.getRange();
		String secondaryLeaseId = context.getSecondaryLeaseId();
		Integer sequenceNumber = context.getSequenceNumber();
		SequenceNumberAction sequenceNumberAction = context.getSequenceNumberAction();
		String snapshot = context.getSnapshot();
		SnapshotDeleteAction snapshotDeleteAction = context.getSnapshotDeleteAction();
		
		switch (operation) {
		
		case CopyBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			StringBuilder sb = new StringBuilder();
			sb.append("/" + authenticationToken.getAccount());
			if (! Util.isStringNullOrEmpty(containerName))
				sb.append("/" + containerName);
			sb.append("/" + blobName);
			if (snapshot != null)
				sb.append("?snapshot=" + snapshot);
			headers.put("x-ms-copy-source", sb.toString());
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			if (! Util.isStringNullOrEmpty(secondaryLeaseId))
				headers.put("x-ms-lease-id", secondaryLeaseId);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-source-lease-id", leaseId);
			break;
			
		case CreateContainer:
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			if (containerAccess != ContainerAccess.PRIVATE)
				headers.put("x-ms-blob-public-access", containerAccess.toString().toLowerCase());
			break;
			
		case DeleteBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			if (snapshotDeleteAction != null)
				headers.put("x-ms-delete-snapshots", snapshotDeleteAction.toString());
			break;
			
		case DeleteContainer:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			break;
			
		case GetBlob:
		case GetPageRegions:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (range != null)
				headers.put("x-ms-range", range.toString());
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
			
		case GetBlobMetadata:
		case GetBlobProperties:
		case GetBlockList:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
			
		case LeaseBlob:
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			headers.put("x-ms-lease-action", leaseAction.toString());
			break;
			
		case PutBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			headers.put("x-ms-blob-type", blobType.toString());
			
			if (blobData != null) {
				cacheControl = blobData.getCacheControl();
				if (! Util.isStringNullOrEmpty(cacheControl))
					headers.put(HttpHeader.CACHE_CONTROL, cacheControl);
				
				contentLanguage = blobData.getContentLanguage();
				if (! Util.isStringNullOrEmpty(contentLanguage))
					headers.put(HttpHeader.CONTENT_LANGUAGE, contentLanguage);
				
				contentLength = blobData.GetContentLength();
				sharedKey.setContentLength(contentLength.toString());
				headers.put(HttpHeader.CONTENT_LENGTH, contentLength);
				
				contentMd5 = blobData.getContentMd5();
				if (! Util.isStringNullOrEmpty(contentMd5))
					headers.put(HttpHeader.CONTENT_MD5, contentMd5);
				
				contentEncoding = blobData.getContentEncoding();
				if (! Util.isStringNullOrEmpty(contentEncoding))
					headers.put(AzureHttpHeader.XMS_CONTENT_ENCODING, contentEncoding);
				
				headers.put(AzureHttpHeader.XMS_CONTENT_TYPE, blobData.getContentType());
				
				if (! Util.isStringNullOrEmpty(leaseId))
					headers.put("x-ms-lease-id", leaseId);
				
				if (metadata != null)
					addMetadataToHeaders(headers, metadata);
			}
			
			if (blobType == BlobType.PAGE_BLOB) {
				headers.put("x-ms-blob-content-length", maxPageSize);
				headers.put("x-ms-blob-sequence-number", blobSequenceNumber);
			}
			break;
		
		case PutBlock:
			contentLength = blobData.GetContentLength();
			sharedKey.setContentLength(Integer.toString(contentLength));
			headers.put(HttpHeader.CONTENT_LENGTH, contentLength);
			contentMd5 = blobData.getContentMd5();
			if (! Util.isStringNullOrEmpty(contentMd5))
				headers.put(HttpHeader.CONTENT_MD5, contentMd5);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
			
		case PutBlockList:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			cacheControl = blobData.getCacheControl();
			if (! Util.isStringNullOrEmpty(cacheControl))
				headers.put("x-ms-blob-cache-control", cacheControl);
			contentType = blobData.getContentType();
			if (! Util.isStringNullOrEmpty(contentType))
				headers.put("x-ms-blob-content-type", contentType);
			contentEncoding = blobData.getContentEncoding();
			if (! Util.isStringNullOrEmpty(contentEncoding))
				headers.put("x-ms-blob-content-encoding", contentEncoding);
			contentLanguage = blobData.getContentLanguage();
			if (! Util.isStringNullOrEmpty(contentLanguage))
				headers.put("x-ms-blob-content-language", contentLanguage);
			contentMd5 = blobData.getContentMd5();
			if (! Util.isStringNullOrEmpty(contentMd5))
				headers.put("x-ms-blob-content-md5", contentMd5);
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
			
		case PutPage:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (range != null)
				headers.put("x-ms-range", range.toString());
			
			contentLength = range.getLength();
			sharedKey.setContentLength(contentLength.toString());
			headers.put(HttpHeader.CONTENT_LENGTH, contentLength);
			
			//contentMd5 = blobData.getContentMd5();
			//if (! Util.isStringNullOrEmpty(contentMd5))
			//	headers.put(HttpHeader.CONTENT_MD5, contentMd5);
			
			headers.put("x-ms-page-write", putPageAction.toString());
			
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
			
		case SetBlobMetadata:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			addMetadataToHeaders(headers, metadata);
			break;
			
		case SetBlobProperties:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			
			cacheControl = blobData.getCacheControl();
			contentType = blobData.getContentType();
			contentMd5 = blobData.getContentMd5();
			contentEncoding = blobData.getContentEncoding();
			contentLanguage = blobData.getContentLanguage();
			contentLength = blobData.GetContentLength();
			if (! Util.isStringNullOrEmpty(cacheControl))
				headers.put("x-ms-blob-cache-control", cacheControl);
			if (! Util.isStringNullOrEmpty(contentType))
				headers.put("x-ms-blob-content-type", contentType);
			if (! Util.isStringNullOrEmpty(contentMd5))
				headers.put("x-ms-blob-content-md5", contentMd5);
			if (! Util.isStringNullOrEmpty(contentEncoding))
				headers.put("x-ms-blob-content-encoding", contentEncoding);
			if (! Util.isStringNullOrEmpty(contentLanguage))
				headers.put("x-ms-blob-content-language", contentLanguage);
			if (contentLength != null)
				headers.put("x-ms-blob-content-length", contentLength.toString());
			if (sequenceNumberAction != null)
				headers.put("x-ms-sequence-number-action", sequenceNumberAction.toString());
			if (sequenceNumber != null)
				headers.put("x-ms-blob-sequence-number", sequenceNumber.toString());
			break;
			
		case SetContainerACL:
			if (containerAccess != null && containerAccess != ContainerAccess.PRIVATE)
				headers.put("x-ms-blob-public-access", containerAccess.toString().toLowerCase());
			break;
			
		case SetContainerMetadata:
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			break;
			
		case SnapshotBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
		}
		
		HttpUri uri = getPath(context);
		String authenticationHeader = buildAuthenticationHeader(context, uri, headers);
		headers.put(HttpHeader.AUTHORIZATION, authenticationHeader);
	}

	@Override
	protected String getHost(AzureStrategyContext context) {
		return authenticationToken.getAccount() + ".blob.core.windows.net";
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context)
		throws Exception {
		
		HttpUri uri = new HttpUri("/");

		AzureOperation operation = context.getOperation();
		String blobName = context.getBlobName();
		String blockId = context.getBlockId();
		String containerName = context.getContainerName();
		String delimiter = context.getDelimiter();
		Set<EnumerationFilter> enumerationFilter = context.getEnumerationFilter();
		String marker = context.getMarker();
		int maxResults = context.getMaxResults();
		String prefix = context.getPrefix();
		String secondaryBlobName = context.getSecondaryBlobName();
		String secondaryContainerName = context.getSecondaryContainerName();
		String snapshot = context.getSnapshot();
		
		switch (operation) {
		
		case CopyBlob:
			uri.appendPath(secondaryContainerName);
			uri.appendPath("/" + secondaryBlobName);
			break;
			
		case CreateContainer:
		case DeleteContainer:
		case GetContainerProperties:
			uri.appendPath(containerName);
			uri.addParameterWithoutEncoding("restype", "container");
			break;
			
		case DeleteBlob:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlob:
		case GetBlobProperties:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlobMetadata:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "metadata");
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlockList:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "blocklist");
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			uri.addParameterWithoutEncoding("blocklisttype", "all");
			break;
			
		case GetContainerACL:
		case SetContainerACL:
			uri.appendPath(containerName);
			uri.addParameterWithoutEncoding("restype", "container");
			uri.addParameterWithoutEncoding("comp", "acl");
			break;
			
		case GetContainerMetadata:
		case SetContainerMetadata:
			uri.appendPath(containerName);
			uri.addParameterWithoutEncoding("restype", "container");
			uri.addParameterWithoutEncoding("comp", "metadata");
			break;
		
		case GetPageRegions:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "pagelist");
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case LeaseBlob:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "lease");
			break;
			
		case ListBlobs:
			uri.appendPath(containerName);
			uri.addParameterWithoutEncoding("restype", "container");
			uri.addParameterWithoutEncoding("comp", "list");
			if (! Util.isStringNullOrEmpty(prefix))
				uri.addParameterWithoutEncoding("prefix", prefix);
			if (! Util.isStringNullOrEmpty(delimiter))
				uri.addParameterWithoutEncoding("delimiter", delimiter);
			if (! Util.isStringNullOrEmpty(marker))
				uri.addParameterWithoutEncoding("marker", marker);
			uri.addParameterWithoutEncoding("maxresults", Integer.toString(maxResults));
			if (enumerationFilter != null)
				uri.addParameterWithoutEncoding("include", EnumerationFilter.toString(enumerationFilter));
			break;
			
		case ListContainers:
			uri.addParameterWithoutEncoding("comp", "list");
			uri.addParameterWithoutEncoding("include", "metadata");
			if (! Util.isStringNullOrEmpty(marker))
				uri.addParameterWithoutEncoding("marker", marker);
			uri.addParameterWithoutEncoding("maxresults", Integer.toString(maxResults));
			if (! Util.isStringNullOrEmpty(prefix))
				uri.addParameterWithoutEncoding("prefix", prefix);
			break;
			
		case PutBlob:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			break;
			
		case PutBlock:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "block");
			uri.addParameterWithoutEncoding("blockid", blockId);
			break;
			
		case PutBlockList:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "blocklist");
			break;
			
		case PutPage:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "page");
			break;
			
		case SetBlobMetadata:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "metadata");
			break;
		
		case SetBlobProperties:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "properties");
			break;
			
		case SnapshotBlob:
			uri.appendPath(containerName);
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "snapshot");
			break;
			
		default:
			throw new Exception("Missing case for operation " + operation);
		}
		
		return uri;
	}
	
	private class ParameterComparer implements Comparator<Pair<String, String>> {
		@Override
		public int compare(Pair<String, String> object1, Pair<String, String> object2) {
			return object1.getFirst().compareTo(object2.getFirst());
		}
	}
	
	private void addConditionsToHeaders(AzureHttpHeader headers, Condition condition) {
		Map<String, String> conditions = condition.getConditions();
		for (String key : conditions.keySet()) {
			String value = conditions.get(key);
			headers.put(key, value);
			
			if (key.equals(Condition.IF_MATCH))
				sharedKey.setIfMatch(value);
			if (key.equals(Condition.IF_MODIFIED_SINCE))
				sharedKey.setIfModifiedSince(value);
			if (key.equals(Condition.IF_NONE_MATCH))
				sharedKey.setIfNoneMatch(value);
			if (key.equals(Condition.IF_UNMODIFIED_SINCE))
				sharedKey.setIfUnmodifiedSince(value);
		}
	}
	
	private void addMetadataToHeaders(AzureHttpHeader headers, MetadataCollection metadata) {
		for (Metadata datum : metadata) {
			headers.put(AzureHttpHeader.XMS_META_PREFIX + datum.getName(), datum.getValue());
		}
	}
	
	private String buildAuthenticationHeader(AzureStrategyContext context, HttpUri uri, AzureHttpHeader headers)
		throws 	Exception {
		
		AzureOperation operation = context.getOperation();
		HttpMethod httpMethod = operation.getHttpMethod();
		sharedKey
			.setVerb(httpMethod.toString())
			.setCanonicalizedHeaders(headers.toCanonicalizedForm())
			.setCanonicalizedResource(buildCanonicalizedResource(uri));
		
		return sharedKey.getAuthorizationHeader(authenticationToken.getAccount(), authenticationToken.getKey());
	}
	
	private String buildCanonicalizedResource(HttpUri uri)
		throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append('/');
		sb.append(authenticationToken.getAccount());
		sb.append(uri.getPath());
		
		Vector<Pair<String, String>> parameters = new Vector<Pair<String, String>>(uri.getParameters());
		Collections.sort(parameters, new ParameterComparer());
		for (Pair<String, String> parameter : parameters) {
				sb.append('\n');
				sb.append(parameter.getFirst());
				sb.append(':');
				sb.append(parameter.getSecond());
		}
		
		return sb.toString();
	}
	
	private BlobAndQueueServiceSharedKey sharedKey;
}
