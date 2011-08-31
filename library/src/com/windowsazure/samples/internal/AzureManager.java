package com.windowsazure.samples.internal;

import com.windowsazure.samples.authentication.AuthenticationToken;

public abstract class AzureManager {
	
	protected AzureManager(AuthenticationToken token)
		throws UnableToBuildStrategyException {
		
		this.strategy = buildStrategy(token);
	}
	
	protected AzureStrategy<? extends AuthenticationToken> strategy;
	
	protected abstract AzureStrategy<? extends AuthenticationToken> buildStrategy(AuthenticationToken token) throws UnableToBuildStrategyException;
}
