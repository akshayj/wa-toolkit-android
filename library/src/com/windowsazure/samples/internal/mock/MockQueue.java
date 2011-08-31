package com.windowsazure.samples.internal.mock;

import java.util.Vector;

import com.windowsazure.samples.Metadata;

public class MockQueue {
	public String name;
	public String url;
	
	public Vector<MockQueueMessage> messages = new Vector<MockQueueMessage>();
	public Vector<Metadata> metadata = new Vector<Metadata>();
}
