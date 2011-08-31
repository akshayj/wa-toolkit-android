package com.windowsazure.samples.internal.table;

import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.authentication.SharedKey;
import com.windowsazure.samples.internal.authentication.TableServiceSharedKey;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpMethod;
import com.windowsazure.samples.internal.web.HttpUri;
import com.windowsazure.samples.table.Filter;


public final class DirectConnectTableStrategy extends AzureStrategy<DirectConnectToken> {

	public DirectConnectTableStrategy(DirectConnectToken token) {
		super(token);
	}

	@Override
	protected void addCommonHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		sharedKey = new TableServiceSharedKey();
		
		headers.put(HttpHeader.DATE, Util.dateToGmtString(context.getDate()));
		headers.put(AzureHttpHeader.DATA_SERVICE_VERSION, AzureHttpHeader.DATA_SERVICE_VERSION_ONE);
		headers.put(AzureHttpHeader.MAX_DATA_SERVICE_VERSION, AzureHttpHeader.MAX_DATA_SERVICE_VERSION_ONE);
	}

	@Override
	protected void addContentHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		String httpBody = context.getHttpBody();
		String contentMd5 = SharedKey.signDataWithKey(httpBody, authenticationToken.getKey());
		String contentType = HttpHeader.CONTENT_TYPE_ATOM;
		
		sharedKey
			.setContentMd5(contentMd5)
			.setContentType(contentType);
		
		headers.put(HttpHeader.CONTENT_LENGTH, httpBody.length());
		headers.put(HttpHeader.CONTENT_MD5, contentMd5);
		headers.put(HttpHeader.CONTENT_TYPE, contentType);
	}

	@Override
	protected void addOperationHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		AzureOperation operation = context.getOperation();
		String contentType = HttpHeader.CONTENT_TYPE_ATOM;
		IfMatch match = context.getIfMatch();
		
		switch (operation) {
		
		case DeleteTableEntity:
			sharedKey.setContentType(contentType);
			headers.put(HttpHeader.CONTENT_LENGTH, 0);
			headers.put(HttpHeader.CONTENT_TYPE, contentType);
			headers.put(AzureHttpHeader.IF_MATCH, match.getRepresentation());
			break;
			
		case DeleteTable:
			sharedKey.setContentType(contentType);
			headers.put(HttpHeader.CONTENT_TYPE, contentType);
			break;
		
		case MergeTableEntity:
		case UpdateTableEntity:
			headers.put(AzureHttpHeader.IF_MATCH, match.getRepresentation());
			break;
		}
		
		HttpUri uri = getPath(context);
		String authenticationHeader = buildAuthenticationHeader(context, uri);
		headers.put(HttpHeader.AUTHORIZATION, authenticationHeader);
	}

	@Override
	protected String getHost(AzureStrategyContext context)
		throws Exception {
		
		return authenticationToken.getAccount() + ".table.core.windows.net";
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context)
		throws Exception {
		
		HttpUri uri = new HttpUri(null);
		
		AzureOperation operation = context.getOperation();
		String tableName = context.getTableName();
		Filter filter = context.getFilter();
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		Integer top = context.getTop();
		
		switch (operation) {
		
		case CreateTable:
		case QueryTables:
			uri.setPath("/Tables");
			break;
			
		case DeleteTableEntity:
		case MergeTableEntity:
		case UpdateTableEntity:
			uri.setPath(String.format("/%s(PartitionKey='%s',RowKey='%s')", tableName, partitionKey, rowKey));
			break;
			
		case DeleteTable:
			uri.setPath(String.format("/Tables('%s')", tableName));
			break;
			
		case InsertTableEntity:
			uri.setPath("/" + tableName);
			break;
			
		case QueryTableEntities:
			if (! Util.isStringNullOrEmpty(partitionKey))
				uri.setPath(String.format("/%s(PartitionKey='%s',RowKey='%s')", tableName, partitionKey, rowKey));
			else {
				uri.setPath("/" + tableName + "()");
				if (filter != null) {
					uri.addParameterWithoutEncoding("$filter", filter.getRepresentation());
				}
				if (top != null)
					uri.addParameterWithoutEncoding("$top", top.toString());
			}
			break;
			
		default:
			throw new Exception("Missing case for operation " + operation);
		}
		
		return uri;
	}
	
	private String buildAuthenticationHeader(AzureStrategyContext context, HttpUri uri)
		throws 	Exception {
		
		AzureOperation operation = context.getOperation();
		HttpMethod httpMethod = operation.getHttpMethod();
		sharedKey
			.setVerb(httpMethod.toString())
			.setDateString(Util.dateToGmtString(context.getDate()))
			.setCanoncalizedResource(buildCanonicalizedResource(uri));
		
		return sharedKey.getAuthorizationHeader(authenticationToken.getAccount(), authenticationToken.getKey());
	}
	
	private String buildCanonicalizedResource(HttpUri uri)
		throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append('/');
		sb.append(authenticationToken.getAccount());
		
		sb.append(uri.getPath());
		return sb.toString();
	}
	
	private TableServiceSharedKey sharedKey;
}
