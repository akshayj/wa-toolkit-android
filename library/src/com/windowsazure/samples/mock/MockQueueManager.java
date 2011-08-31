package com.windowsazure.samples.mock;

import com.windowsazure.samples.authentication.AuthenticationTokenFactory;
import com.windowsazure.samples.queue.AzureQueueManager;

public final class MockQueueManager extends AzureQueueManager {

	public MockQueueManager()
		throws Exception {
		
		super(AuthenticationTokenFactory.buildMockToken());
	}
}