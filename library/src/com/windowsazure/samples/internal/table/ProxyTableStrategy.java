package com.windowsazure.samples.internal.table;

import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpUri;
import com.windowsazure.samples.table.Filter;

public final class ProxyTableStrategy extends AzureStrategy<ProxyToken> {

	public ProxyTableStrategy(ProxyToken token) {
		super(token);
	}

	@Override
	protected void addCommonHeaders(AzureStrategyContext context, AzureHttpHeader headers)
		throws Exception {
		
		if (! authenticationToken.isAuthenticated())
			throw new NotAuthenticatedException();
			
		headers.put(HttpHeader.ACCEPT, HttpHeader.CONTENT_TYPE_ATOM);
		headers.put(HttpHeader.ACCEPT_ENCODING, HttpHeader.ACCEPT_ENCODING_IDENTITY);
		headers.put(AzureHttpHeader.AUTH_TOKEN, authenticationToken.getToken());
		headers.put(AzureHttpHeader.MAX_DATA_SERVICE_VERSION, AzureHttpHeader.MAX_DATA_SERVICE_VERSION_TWO);
		headers.put(HttpHeader.USER_AGENT, HttpHeader.USER_AGENT_NATIVE_HOST);
	}

	@Override
	protected void addContentHeaders(AzureStrategyContext context, AzureHttpHeader headers) {
		headers.put(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_TYPE_ATOM);
		headers.put(HttpHeader.CONTENT_LENGTH, context.getHttpBody().length());
	}

	@Override
	protected void addOperationHeaders(AzureStrategyContext context, AzureHttpHeader headers) {
		AzureOperation operation = context.getOperation();
		IfMatch match = context.getIfMatch();
		
		switch (operation) {
		
		case DeleteTableEntity:
			headers.put(HttpHeader.CONTENT_LENGTH, 0);
			headers.put(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_TYPE_ATOM);
			headers.put(AzureHttpHeader.IF_MATCH, match.getRepresentation());
			break;
			
		case DeleteTable:
			headers.put(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_TYPE_ATOM);
			break;
			
		case MergeTableEntity:
		case UpdateTableEntity:
			headers.put(AzureHttpHeader.IF_MATCH, match.getRepresentation());
			break;
		}
	}

	@Override
	protected String getHost(AzureStrategyContext context) {
		return authenticationToken.getHost();
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context)
		throws Exception {
		
		HttpUri uri = new HttpUri("/AzureTablesProxy.axd");
		
		AzureOperation operation = context.getOperation();
		String tableName = context.getTableName();
		Filter filter = context.getFilter();
		String partitionKey = context.getPartitionKey();
		String rowKey = context.getRowKey();
		Integer top = context.getTop();
		
		switch (operation) {
		
		case CreateTable:
		case QueryTables:
			uri.appendPath("/Tables");
			break;
			
		case DeleteTableEntity:
		case MergeTableEntity:
		case UpdateTableEntity:
			uri.appendPath(String.format("/%s(PartitionKey='%s',RowKey='%s')", tableName, partitionKey, rowKey));
			break;
			
		case DeleteTable:
			uri.appendPath(String.format("/Tables('%s')", context.getTableName()));
			break;
			
		case InsertTableEntity:
			uri.appendPath("/" + tableName);
			break;
			
		case QueryTableEntities:
			if (! Util.isStringNullOrEmpty(partitionKey))
				uri.appendPath(String.format("/%s(PartitionKey='%s',RowKey='%s')", tableName, partitionKey, rowKey));
			else {
				uri.appendPath("/" + tableName + "()");
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
}
