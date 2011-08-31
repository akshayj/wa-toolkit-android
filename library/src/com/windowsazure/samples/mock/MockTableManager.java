package com.windowsazure.samples.mock;

import com.windowsazure.samples.authentication.AuthenticationTokenFactory;
import com.windowsazure.samples.table.AzureTableManager;

public final class MockTableManager extends AzureTableManager {

	public MockTableManager()
		throws Exception {
		
		super(AuthenticationTokenFactory.buildMockToken());
	}
}
