package com.windowsazure.samples.internal.authentication;

import com.windowsazure.samples.authentication.AuthenticationToken;


public final class DirectConnectToken implements AuthenticationToken {
	
	public DirectConnectToken(String account, String key) {
		this.account = account;
		this.key = key;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getHost() {
		return HOST;
	}
	
	public String getKey() {
		return key;
	}
	
	private static final String HOST = "core.windows.net";
	
	private String account;
	private String key;
}
