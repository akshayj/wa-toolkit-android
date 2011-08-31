package com.windowsazure.samples.internal.blob;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.windowsazure.samples.Metadata;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.EnumerationFilter;
import com.windowsazure.samples.blob.LeaseAction;
import com.windowsazure.samples.blob.SequenceNumberAction;
import com.windowsazure.samples.blob.SnapshotDeleteAction;
import com.windowsazure.samples.blob.condition.Condition;
import com.windowsazure.samples.blob.data.BlobData;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.OperationNotSupportedException;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpUri;
import com.windowsazure.samples.internal.web.XmlHttp;
import com.windowsazure.samples.internal.web.XmlHttpResult;


public final class ProxyBlobStrategy extends AzureStrategy<ProxyToken> {

	public ProxyBlobStrategy(ProxyToken token) {
		super(token);
	}

	@Override
	protected void addCommonHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
			
		headers.put(HttpHeader.ACCEPT, "*/*");
		headers.put(HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_ENCODING_IDENTITY);
		headers.put(HttpHeader.CACHE_CONTROL, HttpHeader.CACHE_CONTROL_NO_CACHE);
		headers.put(HttpHeader.USER_AGENT, HttpHeader.USER_AGENT_NATIVE_HOST);
	}

	@Override
	protected void addContentHeaders(AzureStrategyContext context, AzureHttpHeader headers) {
		String httpBody = context.getHttpBody();
		int contentLength = (httpBody != null) ? httpBody.length() : 0;
		
		headers.put(HttpHeader.CONTENT_LENGTH, contentLength);
		headers.put(HttpHeader.CONTENT_TYPE, null);
	}

	@Override
	protected void addOperationHeaders(AzureStrategyContext context, AzureHttpHeader headers) {
		AzureOperation operation = context.getOperation();
		BlobData blobData = context.getBlobData();
		int blobSequenceNumber = context.getBlobSequenceNumber();
		BlobType blobType = context.getBlobType();
		String cacheControl = null;
		Condition condition = context.getCondition();
		String contentEncoding = null;
		String contentLanguage = null;
		Integer contentLength = null;
		String contentMd5 = null;
		String contentType = null;
		LeaseAction leaseAction = context.getLeaseAction();
		String leaseId = context.getLeaseId();
		int maxPageSize = context.getMaxPageSize();
		MetadataCollection metadata = context.getMetadata();
		BlobRange range = context.getRange();
		Integer sequenceNumber = context.getSequenceNumber();
		SequenceNumberAction sequenceNumberAction = context.getSequenceNumberAction();
		SnapshotDeleteAction snapshotDeleteAction = context.getSnapshotDeleteAction();
		
		switch (operation) {
		
		case DeleteBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			if (snapshotDeleteAction != null)
				headers.put("x-ms-delete-snapshots", snapshotDeleteAction.toString());
			break;
			
		case GetBlob:
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
			if (condition != null)
				addConditionsToHeaders(headers, condition);
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
			
		case SnapshotBlob:
			if (condition != null)
				addConditionsToHeaders(headers, condition);
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			if (! Util.isStringNullOrEmpty(leaseId))
				headers.put("x-ms-lease-id", leaseId);
			break;
		}
	}

	@Override
	protected String getHost(AzureStrategyContext context)
		throws Exception {
		
		return getSssResponse(context).getBlobHost();
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context)
		throws Exception {
		
		AzureOperation operation = context.getOperation();
		String blobName = context.getBlobName();
		String blockId = context.getBlockId();
		String delimiter = context.getDelimiter();
		Set<EnumerationFilter> enumerationFilter = context.getEnumerationFilter();
		String marker = context.getMarker();
		int maxResults = context.getMaxResults();
		String prefix = context.getPrefix();
		String snapshot = context.getSnapshot();
		SharedSignatureServiceResponse sssResponse = getSssResponse(context);
		HttpUri uri = null;
		
		switch (operation) {
		
		case DeleteBlob:
			uri = sssResponse.getAnyUri();
			uri.appendPath("/" + blobName);
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlob:
		case GetBlobProperties:
			uri = sssResponse.getBlobCollection().getUriForBlob(blobName);
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlobMetadata:
			uri = sssResponse.getBlobCollection().getUriForBlob(blobName);
			uri.addParameterWithoutEncoding("comp", "metadata");
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			break;
			
		case GetBlockList:
			uri = sssResponse.getBlobCollection().getUriForBlob(blobName);
			uri.addParameterWithoutEncoding("comp", "blocklist");
			if (snapshot != null)
				uri.addParameterWithoutEncoding("snapshot", snapshot);
			uri.addParameterWithoutEncoding("blocklisttype", "all");
			break;
			
		case LeaseBlob:
			uri = sssResponse.getAnyUri();
			uri.addParameterWithoutEncoding("comp", "lease");
			break;
			
		case ListBlobs:
			uri = sssResponse.getAnyUri();
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
			
		case PutBlob:
			uri = sssResponse.getAnyUri();
			uri.appendPath("/" + blobName);
			break;
			
		case PutBlock:
			uri = sssResponse.getAnyUri();
			uri.addParameterWithoutEncoding("comp", "block");
			uri.addParameterWithoutEncoding("blockid", blockId);
			break;
			
		case PutBlockList:
			uri = sssResponse.getAnyUri();
			uri.addParameterWithoutEncoding("comp", "blocklist");
			break;
			
		case SetBlobMetadata:
			uri = sssResponse.getAnyUri();
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "metadata");
			break;
			
		case SetBlobProperties:
			uri = sssResponse.getAnyUri();
			uri.appendPath("/" + blobName);
			uri.addParameterWithoutEncoding("comp", "properties");
			break;
			
		case SnapshotBlob:
			uri = sssResponse.getAnyUri();
			uri.addParameterWithoutEncoding("comp", "snapshot");
			break;
		
		default:
			throw new Exception("Missing case for operation " + operation);
		}
		
		return uri;
	}
	
	@Override
	protected void onBeginExecute(AzureStrategyContext context)
		throws Exception {
		
		if (! authenticationToken.isAuthenticated())
			throw new NotAuthenticatedException();
		
		AzureOperation operation = context.getOperation();
		switch (operation) {
		case CopyBlob:
		case CreateContainer:
		case DeleteContainer:
		case GetBlockList:
		case GetContainerACL:
		case GetContainerMetadata:
		case GetContainerProperties:
		case GetPageRegions:
		case LeaseBlob:
		case ListContainers:
		case PutBlock:
		case PutBlockList:
		case PutPage:
		case SetBlobProperties:
		case SetContainerACL:
		case SetContainerMetadata:
		case SnapshotBlob:
			throw new OperationNotSupportedException(operation, this);
		}
		
		sssResponse = null;
	};
	
	private void addConditionsToHeaders(AzureHttpHeader headers, Condition condition) {
		Map<String, String> conditions = condition.getConditions();
		for (String key : conditions.keySet()) {
			String value = conditions.get(key);
			headers.put(key, value);
		}
	}
	
	private void addMetadataToHeaders(AzureHttpHeader headers, MetadataCollection metadata) {
		for (Metadata datum : metadata) {
			headers.put(AzureHttpHeader.XMS_META_PREFIX + datum.getName(), datum.getValue());
		}
	}
	
	private AzureHttpHeader getHeadersForSharedAccessSignatureService(AzureStrategyContext context)
		throws Exception {
		
		AzureHttpHeader headers = new AzureHttpHeader();
		headers.put(HttpHeader.ACCEPT, "*/*");
		headers.put(HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_ENCODING_IDENTITY);
		headers.put(AzureHttpHeader.AUTH_TOKEN, authenticationToken.getToken());
		headers.put(HttpHeader.USER_AGENT, HttpHeader.USER_AGENT_NATIVE_HOST);
		return headers;
	}
	
	private SharedSignatureServiceResponse getSssResponse(AzureStrategyContext context)
		throws Exception {
		
		if (sssResponse == null) {
			HttpUri uri = getUriForSharedSignatureService(context);
			AzureHttpHeader headers = getHeadersForSharedAccessSignatureService(context);
			XmlHttpResult xmlHttpResult = XmlHttp.GetSSL(authenticationToken.getHost(), uri.toString(), headers);
			sssResponse = new SharedSignatureServiceResponseDOMAdapter(xmlHttpResult).build();
		}
		return sssResponse;
	}
	
	private HttpUri getUriForSharedSignatureService(AzureStrategyContext context) {
		HttpUri uri = new HttpUri(authenticationToken.getSharedAccessSignatureServicePath());
		uri.addParameterWithoutEncoding("IncrementalSeed", Long.toString(new Date().getTime()));
		
		AzureOperation operation = context.getOperation();
		switch (operation) {
		
		case GetBlob:
		case GetBlobMetadata:
		case GetBlobProperties:
		case GetBlockList:
		case GetPageRegions:
		case PutPage:
			uri.appendPath("/blob");
			break;
			
		default:
		    uri.appendPath("/container");
		    break;
		}
		
		return uri;
	}
	
	private SharedSignatureServiceResponse sssResponse;
}
