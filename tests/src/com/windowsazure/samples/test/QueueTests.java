package com.windowsazure.samples.test;

import junit.framework.Assert;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.queue.AzureQueue;
import com.windowsazure.samples.queue.AzureQueueCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.queue.AzureQueueMetadata;
import com.windowsazure.samples.queue.QueueReader;
import com.windowsazure.samples.queue.QueueWriter;

import android.test.AndroidTestCase;

public class QueueTests extends AndroidTestCase {
	
	public void testClearMessages() {
		try
		{
			String queueName = generateQueueName("test-queue-a");
			String messageText = "Here is a test message.";
			getWriter().createQueue(queueName);
			getWriter().putMessage(queueName, messageText, null);
			
			AzureQueueMessageCollection collection = getReader().peekMessages(queueName, 1);
			Assert.assertEquals(1, collection.getMessages().size());
			
			EntityBase entity = getWriter().clearMessages(queueName);
			Assert.assertEquals(HttpStatusCode.NoContent, entity.getHttpStatusCode());
			
			collection = getReader().peekMessages(queueName, 1);
			Assert.assertEquals(0, collection.getMessages().size());
			
			getWriter().deleteQueue(queueName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testCreateAndDeleteQueue() {
		try
		{
			String queueName = generateQueueName("test-queue-b");
			EntityBase entity = getWriter().createQueue(queueName);
			Assert.assertEquals(HttpStatusCode.Created, entity.getHttpStatusCode());
			
			AzureQueue queue = getQueue(queueName);
			Assert.assertNotNull(queue);
			
			entity = getWriter().deleteQueue(queueName);
			Assert.assertEquals(HttpStatusCode.NoContent, entity.getHttpStatusCode());
			
			queue = getQueue(queueName);
			Assert.assertNull(queue);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteAllTestQueues() {
		try
		{
			AzureQueueCollection collection = getReader().listAllQueues();
			for (AzureQueue queue : collection) {
				String queueName = queue.getQueueName();
				if (queueName.indexOf("test-queue-") == 0)
					getWriter().deleteQueue(queueName);
			}
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDeleteMessage() {
		try
		{
			String queueName = generateQueueName("test-queue-dm");
			String messageText = "Here is a test message.";
			getWriter().createQueue(queueName);
			getWriter().putMessage(queueName, messageText, null);
			
			AzureQueueMessageCollection collection = getReader().getMessages(queueName, 1, TEST_VISIBILITY_TIMEOUT);
			Assert.assertEquals(1, collection.getMessages().size());
			AzureQueueMessage message = collection.getMessages().iterator().next();
			String messageId = message.getMessageId();
			String popReceipt = message.getPopReceipt();
			
			EntityBase entity = getWriter().deleteMessage(queueName, messageId, popReceipt);
			Assert.assertEquals(HttpStatusCode.NoContent, entity.getHttpStatusCode());
			
			collection = getReader().peekMessages(queueName, 1);
			Assert.assertEquals(0, collection.getMessages().size());
			
			getWriter().deleteQueue(queueName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testGetMessages() {
		try
		{
			String queueName = generateQueueName("test-queue-d");
			String messageText = "Here is a test message.";
			getWriter().createQueue(queueName);
			
			EntityBase entity = getWriter().putMessage(queueName, messageText, null);
			Assert.assertEquals(HttpStatusCode.Created, entity.getHttpStatusCode());
			
			AzureQueueMessageCollection collection = getReader().getMessages(queueName, 1, TEST_VISIBILITY_TIMEOUT);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(1, collection.getMessages().size());
			AzureQueueMessage message = collection.getMessages().iterator().next();
			Assert.assertEquals(messageText, message.getMessageText());
			
			collection = getReader().getMessages(queueName, 1, TEST_VISIBILITY_TIMEOUT);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(0, collection.getMessages().size());
			
			getWriter().clearMessages(queueName);
			getWriter().deleteQueue(queueName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testMetadata() {
		try
		{
			String queueName = generateQueueName("test-queue-e");
			
			MetadataCollection metadata = new MetadataCollection();
			metadata.add("value1", "Red");
			metadata.add("value2", "Blue");
			getWriter().createQueue(queueName, metadata);
			
			AzureQueue queue = getQueue(queueName);
			metadata = queue.getMetadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
			
			AzureQueueMetadata queueMetadata = getReader().getQueueMetadata(queueName);
			metadata = queueMetadata.getMetatadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("Red", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Blue", metadata.getMetadata("value2").getValue());
			
			metadata = new MetadataCollection();
			metadata.add("value1", "NotRed");
			metadata.add("value3", "Green");
			getWriter().setQueueMetadata(queueName, metadata);
			
			queueMetadata = getReader().getQueueMetadata(queueName);
			metadata = queueMetadata.getMetatadata();
			Assert.assertEquals(2, metadata.getCount());
			Assert.assertEquals("NotRed", metadata.getMetadata("value1").getValue());
			Assert.assertEquals("Green", metadata.getMetadata("value3").getValue());
			
			getWriter().deleteQueue(queueName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testListAllQueues() {
		try
		{
			AzureQueueCollection queues = getReader().listAllQueues();
			int queueCount = queues.getQueues().size();
			if (queueCount == 0)
				testCreateAndDeleteQueue();
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testPeekMessages() {
		try
		{
			String queueName = generateQueueName("test-queue-pm");
			String messageText = "Here is a test message.";
			getWriter().createQueue(queueName);
			getWriter().putMessage(queueName, messageText, null);
			
			AzureQueueMessageCollection collection = getReader().peekMessages(queueName, 1);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(1, collection.getMessages().size());
			AzureQueueMessage message = collection.getMessages().iterator().next();
			Assert.assertEquals(messageText, message.getMessageText());
			
			collection = getReader().peekMessages(queueName, 1);
			Assert.assertEquals(HttpStatusCode.OK, collection.getHttpStatusCode());
			Assert.assertEquals(1, collection.getMessages().size());
			
			getWriter().clearMessages(queueName);
			getWriter().deleteQueue(queueName);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	private String generateQueueName(String prefix)
		throws Exception {
		
		AzureQueueCollection queues = getReader().listAllQueues();
		int queueCount = queues.getQueues().size();
		return prefix + queueCount;
	}
	
	private AuthenticationToken getAuthenticationToken()
		throws NotAuthenticatedException {
		
		if (token == null) {
			switch (TARGET) {
			
			case DIRECT:
				token = AuthenticationTests.buildDirectConnectToken();
				break;
				
			case PROXY:
				token = AuthenticationTests.buildProxyToken();
				break;
				
			case MOCK:
				token = AuthenticationTests.buildMockToken();
				break;
			}
		}
		return token;
	}
	
	private QueueReader getReader()
		throws Exception {
		
		if (queueReader == null)
			queueReader = new AzureQueueManager(getAuthenticationToken());
		return queueReader;
	}
	
	private AzureQueue getQueue(String queueName)
		throws Exception {
		
		AzureQueueCollection queues = getReader().listAllQueues();
		for (AzureQueue queue : queues.getQueues()) {
			if (queue.getQueueName().equals(queueName))
				return queue;
		}
		return null;
	}
	
	private QueueWriter getWriter()
		throws Exception {
	
		if (queueWriter == null)
			queueWriter = new AzureQueueManager(getAuthenticationToken());
		return queueWriter;
	}

	private static final TestTarget TARGET = TestTarget.DIRECT;
	private static final int TEST_VISIBILITY_TIMEOUT = /* 2 hrs (in seconds) */ 2 * 60 * 60;
	
	private QueueReader queueReader;
	private QueueWriter queueWriter;
	private AuthenticationToken token;
}
