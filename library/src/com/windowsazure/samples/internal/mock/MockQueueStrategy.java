package com.windowsazure.samples.internal.mock;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.Metadata;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.OperationNotSupportedException;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.util.Base64;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.queue.AzureQueue;
import com.windowsazure.samples.queue.AzureQueueCollection;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.queue.AzureQueueMetadata;
import com.windowsazure.samples.queue.QueueOperationResponse;


public class MockQueueStrategy extends MockStrategy {
	
	public MockQueueStrategy(MockToken token) {
		super(token);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <V extends EntityBase> V performOperation(AzureOperation operation, AzureStrategyContext context)
	    throws Exception {
		
		switch (operation) {
		case ClearMessages:			return (V) clearMessages(context);
		case CreateQueue:			return (V) createQueue(context);
		case DeleteMessage:			return (V) deleteMessage(context);
		case DeleteQueue:			return (V) deleteQueue(context);
		case GetMessages:			return (V) getMessages(context);
		case GetQueueMetadata:		return (V) getQueueMetadata(context);
		case ListQueues:			return (V) listQueues(context);
		case PeekMessages:			return (V) peekMessages(context);
		case PutMessage:			return (V) putMessage(context);
		case SetQueueMetadata:		return (V) setQueueMetadata(context);
		
		default:					throw new OperationNotSupportedException(operation, this);
		}
	}
	
	protected QueueOperationResponse clearMessages(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		mq.messages.clear();
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	protected QueueOperationResponse createQueue(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		String url = makeQueueUrl(queueName);
		MetadataCollection metadata = context.getMetadata();
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq != null) {
			response.setHttpStatusCode(HttpStatusCode.Conflict);
			return response;
		}
		
		mq = new MockQueue();
		mq.metadata = metadataCollectionToVector(metadata);
		mq.name = queueName;
		mq.url = url;
		
		queues.add(mq);
		
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	protected QueueOperationResponse deleteMessage(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		String messageId = context.getMessageId();
		String popReceipt = context.getPopReceipt();
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		MockQueueMessage mqm = (mq != null) ? getMockQueueMessageById(queueName, messageId) : null;
		
		if (mqm == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		if (! mqm.popReceipt.equals(popReceipt)) {
			response.setHttpStatusCode(HttpStatusCode.BadRequest);
			return response;
		}
		
		mq.messages.remove(mqm);
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	protected QueueOperationResponse deleteQueue(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		queues.remove(mq);
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	protected AzureQueueMessageCollection getMessages(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		int numberOfMessages = context.getNumberOfMessages();
		int visibilityTimeout = context.getVisibilityTimeout();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			AzureQueueMessageCollection response = new AzureQueueMessageCollection();
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		Vector<AzureQueueMessage> messageVector = new Vector<AzureQueueMessage>();
		
		Date now = new Date();
		for (MockQueueMessage mqm : mq.messages) {
			if (now.after(mqm.timeNextVisible) && now.before(mqm.expirationTime)) {
				++mqm.dequeueCount;
				mqm.popReceipt = UUID.randomUUID().toString();
				Calendar timeNextVisible = Calendar.getInstance();
				timeNextVisible.setTime(now);
				timeNextVisible.add(Calendar.SECOND, visibilityTimeout);
				mqm.timeNextVisible = timeNextVisible.getTime();
				
				messageVector.add(new AzureQueueMessage(
					mqm.messageId,
					mqm.insertionTime,
					mqm.expirationTime,
					mqm.popReceipt,
					mqm.timeNextVisible,
					mqm.dequeueCount,
					mqm.messageText));
				
				if (--numberOfMessages == 0)
					break;
			}
		}
		
		AzureQueueMessageCollection collection = new AzureQueueMessageCollection(
			Long.toString(++requestId),
			VERSION,
			now,
			messageVector);
		
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
	}
	
	protected AzureQueueMetadata getQueueMetadata(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			AzureQueueMetadata response = new AzureQueueMetadata();
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		AzureQueueMetadata response = new AzureQueueMetadata(
			mq.messages.size(),
			metadataVectorToCollection(mq.metadata),
			Long.toString(++requestId),
			VERSION,
			new Date());
		
		response.setHttpStatusCode(HttpStatusCode.OK);
		return response;
	}
	
	// Not supported:
	//    marker
	//    maxresults
	protected AzureQueueCollection listQueues(AzureStrategyContext context) {
		String prefix = context.getPrefix();
		
		Vector<AzureQueue> queueVector = new Vector<AzureQueue>();
		for (MockQueue mq : queues) {
			if (Util.isStringNullOrEmpty(prefix) || mq.name.toLowerCase().indexOf(prefix.toLowerCase()) == 0)
				queueVector.add(new AzureQueue(
					mq.name,
					mq.url,
					metadataVectorToCollection(mq.metadata)));
		}
		
		AzureQueueCollection collection = new AzureQueueCollection(
				Long.toString(++requestId),
				VERSION,
				new Date(),
				prefix,
				null,
				null,
				queueVector,
				null);
		
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
	}
	
	protected AzureQueueMessageCollection peekMessages(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		int numberOfMessages = context.getNumberOfMessages();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			AzureQueueMessageCollection response = new AzureQueueMessageCollection();
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		Vector<AzureQueueMessage> messageVector = new Vector<AzureQueueMessage>();
		
		Date now = new Date();
		for (MockQueueMessage mqm : mq.messages) {
			if (now.after(mqm.timeNextVisible) && now.before(mqm.expirationTime)) {
				messageVector.add(new AzureQueueMessage(
					mqm.messageId,
					mqm.insertionTime,
					mqm.expirationTime,
					null,
					null,
					mqm.dequeueCount,
					mqm.messageText));
				
				if (--numberOfMessages == 0)
					break;
			}
		}
		
		AzureQueueMessageCollection collection = new AzureQueueMessageCollection(
			Long.toString(++requestId),
			VERSION,
			now,
			messageVector);
		
		collection.setHttpStatusCode(HttpStatusCode.OK);
		return collection;
	}
	
	protected QueueOperationResponse putMessage(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		int timeToLiveInterval = context.getTimeToLiveInterval();
		String messageBase64 = extractString("/QueueMessage/MessageText");
		String messageText = new String(Base64.decode(messageBase64.getBytes()));
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		Date now = new Date();
		Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.SECOND, timeToLiveInterval);
		
		MockQueueMessage mqm = new MockQueueMessage();
		mqm.dequeueCount = 0;
		mqm.expirationTime = expiration.getTime();
		mqm.insertionTime = now;
		mqm.messageId = UUID.randomUUID().toString();
		mqm.messageText = messageText;
		mqm.popReceipt = null;
		mqm.timeNextVisible = now;
		
		mq.messages.add(mqm);
		
		response.setHttpStatusCode(HttpStatusCode.Created);
		return response;
	}
	
	protected QueueOperationResponse setQueueMetadata(AzureStrategyContext context) {
		String queueName = context.getQueueName();
		MetadataCollection metadata = context.getMetadata();
		
		QueueOperationResponse response = new QueueOperationResponse();
		
		MockQueue mq = getMockQueueByName(queueName);
		if (mq == null) {
			response.setHttpStatusCode(HttpStatusCode.NotFound);
			return response;
		}
		
		mq.metadata = metadataCollectionToVector(metadata);
		
		response.setHttpStatusCode(HttpStatusCode.NoContent);
		return response;
	}
	
	private MockQueue getMockQueueByName(String queueName) {
		for (MockQueue mq : queues) {
			if (mq.name.equals(queueName))
				return mq;
		}
		return null;
	}
	
	private MockQueueMessage getMockQueueMessageById(String queueName, String messageId) {
		MockQueue mq = getMockQueueByName(queueName);
		assert(mq != null);
		
		for (MockQueueMessage mqm : mq.messages) {
			if (mqm.messageId.equals(messageId))
				return mqm;
		}
		return null;
	}
	
	private Vector<Metadata> metadataCollectionToVector(MetadataCollection collection) {
		Vector<Metadata> metadata = new Vector<Metadata>();
		if (collection != null) {
			for (Metadata item : collection) {
			    Metadata datum = Metadata.buildValid(item.getName(), item.getValue());
			    metadata.add(datum);
			}
		}
		return metadata;
	}
	
	private MetadataCollection metadataVectorToCollection(Vector<Metadata> vector) {
		MetadataCollection metadata = new MetadataCollection();
		for (Metadata item : vector) {
			Metadata datum = Metadata.buildValid(item.getName(), item.getValue());
			metadata.add(datum);
		}
		return metadata;
	}
	
	private String makeQueueUrl(String queueName) {
		return BASE_URL + "/" + queueName.toLowerCase();
	}
	
	private static final String BASE_URL = "/mock/queues";
	private static final String VERSION = "MockQueueStrategy1.0";
	
	private static long requestId = 0;
	private static Vector<MockQueue> queues = new Vector<MockQueue>();
}
