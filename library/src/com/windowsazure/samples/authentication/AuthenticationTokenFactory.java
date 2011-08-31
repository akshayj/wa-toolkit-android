package com.windowsazure.samples.authentication;

import com.windowsazure.samples.internal.authentication.DirectConnectToken;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.authentication.ProxyToken;

public final class AuthenticationTokenFactory {

	public static AuthenticationToken buildDirectConnectToken(String account, String key) {
		return new DirectConnectToken(account, key);
	}
	
	public static AuthenticationToken buildMockToken() {
		return new MockToken();
	}
	
	public static AuthenticationToken buildProxyToken(String host, String username, String password)
		throws NotAuthenticatedException {
		
		return new ProxyToken(host, username, password);
	}
	
	private AuthenticationTokenFactory() {}
}
