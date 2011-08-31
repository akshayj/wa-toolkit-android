package com.windowsazure.samples.internal.queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import com.windowsazure.samples.Metadata;
import com.windowsazure.samples.MetadataCollection;
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


public final class DirectConnectQueueStrategy extends AzureStrategy<DirectConnectToken> {

	public DirectConnectQueueStrategy(DirectConnectToken token) {
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
		
		HttpUri uri = getPath(context);
		String authenticationHeader = buildAuthenticationHeader(context, uri, headers);
		headers.put(HttpHeader.AUTHORIZATION, authenticationHeader);
	}

	@Override
	protected String getHost(AzureStrategyContext context) {
		return authenticationToken.getAccount() + ".queue.core.windows.net";
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context)
		throws Exception {
		
		HttpUri uri = new HttpUri("/");

		AzureOperation operation = context.getOperation();
		String messageId = context.getMessageId();
		int numberOfMessages = context.getNumberOfMessages();
		String popReceipt = context.getPopReceipt();
		String queueName = context.getQueueName();
		int timeToLiveInterval = context.getTimeToLiveInterval();
		int visibilityTimeout = context.getVisibilityTimeout();
		
		switch (operation) {
		
		case ClearMessages:
			uri.appendPath(queueName);
			uri.appendPath("/messages");
			break;
			
		case CreateQueue:
		case DeleteQueue:
			uri.appendPath(queueName);
			break;
			
		case DeleteMessage:
			uri.appendPath(queueName);
			uri.appendPath("/messages");
			uri.appendPath("/" + messageId);
			uri.addParameterWithoutEncoding("popreceipt", popReceipt);
			break;
			
		case GetMessages:
			uri.appendPath(queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("numofmessages", Integer.toString(numberOfMessages));
			uri.addParameterWithoutEncoding("visibilitytimeout", Integer.toString(visibilityTimeout));
			break;
			
		case GetQueueMetadata:
		case SetQueueMetadata:
			uri.appendPath(queueName);
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
			uri.appendPath(queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("peekonly", "true");
			uri.addParameterWithoutEncoding("numofmessages", Integer.toString(numberOfMessages));
			break;
			
		case PutMessage:
			uri.appendPath(queueName);
			uri.appendPath("/messages");
			uri.addParameterWithoutEncoding("messagegetti", Integer.toString(timeToLiveInterval));
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
