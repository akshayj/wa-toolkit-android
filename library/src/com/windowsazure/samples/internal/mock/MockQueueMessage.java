package com.windowsazure.samples.internal.mock;

import java.util.Date;

public class MockQueueMessage {
	int dequeueCount;
	Date expirationTime;
	Date insertionTime;
	String messageId;
	String messageText;
	String popReceipt;
	Date timeNextVisible;
}
