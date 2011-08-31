package com.windowsazure.samples.queue;

import java.util.Date;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.internal.AzureManager;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.UnableToBuildStrategyException;
import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.authentication.ProxyToken;
import com.windowsazure.samples.internal.mock.MockQueueStrategy;
import com.windowsazure.samples.internal.queue.AzureQueueMetadataBuilder;
import com.windowsazure.samples.internal.queue.DirectConnectQueueStrategy;
import com.windowsazure.samples.internal.queue.ProxyQueueStrategy;
import com.windowsazure.samples.internal.queue.PutMessageDOMBuilder;
import com.windowsazure.samples.internal.queue.QueueCollectionDOMAdapter;
import com.windowsazure.samples.internal.queue.QueueMessageCollectionDOMAdapter;
import com.windowsazure.samples.internal.queue.QueueOperationResponseAdapter;


public class AzureQueueManager extends AzureManager implements QueueReader, QueueWriter {

	public AzureQueueManager(AuthenticationToken token)
		throws Exception {
		
		super(token);
	}
	
	@Override
	public QueueOperationResponse clearMessages(String queueName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.ClearMessages)
				.setDate(new Date())
				.setQueueName(queueName);
			
			return strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public QueueOperationResponse createQueue(String queueName) {
		return createQueue(queueName, null);
	}

	@Override
	public QueueOperationResponse createQueue(String queueName, MetadataCollection metadata) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.CreateQueue)
				.setDate(new Date())
				.setMetadata(metadata)
				.setQueueName(queueName);
			
			return strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public QueueOperationResponse deleteMessage(String queueName, String messageId, String popReceipt) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.DeleteMessage)
				.setDate(new Date())
				.setMessageId(messageId)
				.setPopReceipt(popReceipt)
				.setQueueName(queueName);
			
			return strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}

	@Override
	public QueueOperationResponse deleteQueue(String queueName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.DeleteQueue)
				.setDate(new Date())
				.setQueueName(queueName);
			
			return strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public AzureQueueMessageCollection getMessages(String queueName, Integer numberOfMessages, Integer visibilityTimeout) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetMessages)
				.setDate(new Date())
				.setNumberOfMessages((numberOfMessages != null) ? numberOfMessages : NUMBER_OF_MESSAGES_DEFAULT)
				.setQueueName(queueName)
				.setVisibilityTimeout((visibilityTimeout != null) ? visibilityTimeout : VISIBILITY_TIMEOUT_DEFAULT);
			
			return strategy.execute(context, QueueMessageCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureQueueMessageCollection collection = new AzureQueueMessageCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public AzureQueueMetadata getQueueMetadata(String queueName) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.GetQueueMetadata)
				.setDate(new Date())
				.setQueueName(queueName);
			
			AzureQueueMetadata metadata =  strategy.execute(context, AzureQueueMetadataBuilder.class);
			metadata.setQueueName(queueName);
			return metadata;
		}
		catch (Exception e)
		{
			AzureQueueMetadata metadata = new AzureQueueMetadata();
			metadata.setException(e);
			return metadata;
		}
	}
	
	@Override
	public AzureQueueCollection listAllQueues() {
		return listQueues(null, null, null);
	}
	
	@Override
	public AzureQueueCollection listQueues(String prefix, String marker, Integer maxResults) {
		try
		{
			if (maxResults == null)
				maxResults = 5000;
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.ListQueues)
				.setDate(new Date())
				.setMaxResults(maxResults);
			
			return strategy.execute(context, QueueCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureQueueCollection collection = new AzureQueueCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public AzureQueueMessageCollection peekMessages(String queueName, Integer numberOfMessages) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PeekMessages)
				.setDate(new Date())
				.setNumberOfMessages((numberOfMessages != null) ? numberOfMessages : NUMBER_OF_MESSAGES_DEFAULT)
				.setQueueName(queueName);
			
			return strategy.execute(context, QueueMessageCollectionDOMAdapter.class);
		}
		catch (Exception e)
		{
			AzureQueueMessageCollection collection = new AzureQueueMessageCollection();
			collection.setException(e);
			return collection;
		}
	}
	
	@Override
	public QueueOperationResponse putMessage(String queueName, String messageText, Integer timeToLiveInterval) {
		try
		{
			String httpBody = new PutMessageDOMBuilder(messageText).getXmlString(false);
			
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.PutMessage)
				.setDate(new Date())
				.setHttpBody(httpBody)
				.setQueueName(queueName)
				.setTimeToLiveInterval((timeToLiveInterval != null) ? timeToLiveInterval : TIME_TO_LIVE_INTERVAL_DEFAULT);
			
			return strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}
	
	@Override
	public QueueOperationResponse setQueueMetadata(String queueName, MetadataCollection metadata) {
		try
		{
			AzureStrategyContext context = new AzureStrategyContext()
				.setOperation(AzureOperation.SetQueueMetadata)
				.setDate(new Date())
				.setMetadata(metadata)
				.setQueueName(queueName);
			
			return  strategy.execute(context, QueueOperationResponseAdapter.class);
		}
		catch (Exception e)
		{
			QueueOperationResponse response = new QueueOperationResponse();
			response.setException(e);
			return response;
		}
	}

	@Override
	protected AzureStrategy<? extends AuthenticationToken> buildStrategy(AuthenticationToken token)
		throws UnableToBuildStrategyException {
		
		if (token instanceof DirectConnectToken)
			return new DirectConnectQueueStrategy((DirectConnectToken) token);
		
		if (token instanceof ProxyToken)
			return new ProxyQueueStrategy((ProxyToken) token);
		
		if (token instanceof MockToken)
			return new MockQueueStrategy((MockToken) token);
		
		throw new UnableToBuildStrategyException();
	}

	private static final int NUMBER_OF_MESSAGES_DEFAULT = 1;
	private static final int TIME_TO_LIVE_INTERVAL_DEFAULT = /* 7 days (in seconds) */ 7 * 24 * 60 * 60;
	private static final int VISIBILITY_TIMEOUT_DEFAULT = /* in seconds */ 30;
}
