package com.windowsazure.samples.internal.queue;

import com.windowsazure.samples.Metadata;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpUri;

public final class ProxyQueueStrategy extends AzureStrategy<ProxyToken> {

	public ProxyQueueStrategy(ProxyToken token) {
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
		headers.put(AzureHttpHeader.XMS_Date, Util.dateToGmtString(context.getDate()));
		headers.put(AzureHttpHeader.XMS_VERSION, AzureHttpHeader.XMS_VERSION_VALUE);
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
		int maxResults = context.getMaxResults();
		MetadataCollection metadata = context.getMetadata();
		
		switch (operation) {
		
		case CreateQueue:
		case SetQueueMetadata:
			if (metadata != null)
				addMetadataToHeaders(headers, metadata);
			break;
			
		case ListQueues:
			headers.put("maxresults", maxResults);
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
		
		HttpUri uri = new HttpUri("/AzureQueuesProxy.axd");
		AzureOperation operation = context.getOperation();
		String messageId = context.getMessageId();
		int numberOfMessages = context.getNumberOfMessages();
		String popReceipt = context.getPopReceipt();
		String queueName = context.getQueueName();
		int timeToLiveInterval = context.getTimeToLiveInterval();
		int visibilityTimeout = context.getVisibilityTimeout();
		
		switch (operation) {
		
		case ClearMessages:
			uri.appendPath("/" + queueName);
			uri.appendPath("/messages");
			break;
			
		case CreateQueue:
		case DeleteQueue:
			uri.appendPath("/" + queueName);
			break;
			
		case DeleteMessage:
			uri.appendPath("/" + queueName);
			uri.appendPath("/messages");
			uri.appendPath("/" + messageId);
			uri.addParameterWithoutEncoding("popreceipt", popReceipt);
			break;
			
		case GetMessages:
			uri.appendPath("/" + queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("numofmessages", Integer.toString(numberOfMessages));
			uri.addParameterWithoutEncoding("visibilitytimeout", Integer.toString(visibilityTimeout));
			break;
			
		case GetQueueMetadata:
		case SetQueueMetadata:
			uri.appendPath("/" + queueName);
			uri.addParameterWithoutEncoding("comp", "metadata");
			break;
			
		case ListQueues:
			String prefix = context.getPrefix();
			String marker = context.getMarker();
			
			uri.addParameterWithoutEncoding("comp", "list");
			uri.addParameterWithoutEncoding("include", "metadata");
			if (! Util.isStringNullOrEmpty(prefix))
				uri.addParameterWithoutEncoding("prefix", prefix);
			if (! Util.isStringNullOrEmpty(marker))
				uri.addParameterWithoutEncoding("marker", marker);
			break;
			
		case PeekMessages:
			uri.appendPath("/" + queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("peekonly", "true");
			uri.addParameterWithoutEncoding("numofmessages", Integer.toString(numberOfMessages));
			break;
			
		case PutMessage:
			uri.appendPath("/" + queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("messagegetti", Integer.toString(timeToLiveInterval));
			break;
		
		default:
			throw new Exception("Missing case for operation " + operation);
		}
		
		return uri;
	}
	
	private void addMetadataToHeaders(AzureHttpHeader headers, MetadataCollection metadata) {
		for (Metadata datum : metadata) {
			headers.put(AzureHttpHeader.XMS_META_PREFIX + datum.getName(), datum.getValue());
		}
	}
}
