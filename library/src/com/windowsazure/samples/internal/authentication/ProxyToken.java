package com.windowsazure.samples.internal.authentication;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.authentication.NotAuthenticatedException;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttp;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.DOMAdapter;
import com.windowsazure.samples.internal.xml.DOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;


public final class ProxyToken implements AuthenticationToken {
	
	public ProxyToken(String host, String username, String password)
		throws NotAuthenticatedException {
		
		this.host = host;
		this.username = username;
		this.password = password;
		authenticate();
	}
	
	public String getHost() {
		return host;
	}
	
	public String getSharedAccessSignatureServicePath() {
		return SHARED_ACCESS_SIGNATURE_SERVICE_PATH;
	}
	
	public String getToken() {
		return token;
	}
	
	public boolean isAuthenticated() {
		return ! Util.isStringNullOrEmpty(token);
	}
	
	private class LoginRequestDOMBuilder extends DOMBuilder {
		
		public LoginRequestDOMBuilder(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		@Override
		protected void buildDOM() {
			XmlNode loginNode = addRootNode(LOGIN_NODE_NAME);
			addDefaultNamespace(loginNode, CREDENTIAL_NS);
			addNamespace(loginNode, "i", INSTANCE_NS);
			
			addTextNode(loginNode, PASSWORD_NODE_NAME , password);
			addTextNode(loginNode, USERNAME_NODE_NAME , username);
		}
		
		private static final String CREDENTIAL_NS = "http://schemas.datacontract.org/2004/07/Microsoft.Samples.WindowsPhoneCloud.StorageClient.Credentials";
		private static final String INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance";
		private static final String LOGIN_NODE_NAME = "Login";
		private static final String PASSWORD_NODE_NAME = "Password";
		private static final String USERNAME_NODE_NAME = "UserName";
		
		private String password;
		private String username;
	}
	
	private class LoginResponse {
		public boolean isAuthenticated;
		public String token;
	}
	
	private class LoginResponseDOMAdapter extends DOMAdapter<LoginResponse> {
		
		public LoginResponseDOMAdapter(String xmlString) {
			super(xmlString);
		}

		@Override
		public LoginResponse build() {
			LoginResponse result = new LoginResponse();
			result.isAuthenticated = false;
			
			try
			{
				result.token = getRootNode().getInnerText();
				result.isAuthenticated = true;
			}
			catch (Exception e)
			{
			} 
			
			return result;
		}
	}
	
	private void authenticate()
		throws NotAuthenticatedException {
		
		try
		{
			String loginXmlString = new LoginRequestDOMBuilder(username, password).getXmlString(true);
			XmlHttpResult result = XmlHttp.PostSSL(host, LOGIN_PATH, null, loginXmlString);
			if (result.getStatusCode() == HttpStatusCode.OK) {
				LoginResponse response = new LoginResponseDOMAdapter(result.getXmlString()).build();
				if (response.isAuthenticated)
					token = response.token;
			}
		}
		catch (Exception e)
		{
			throw new NotAuthenticatedException();
		}
	}
	
	private static final String LOGIN_PATH = "/AuthenticationService/login";
	private static final String SHARED_ACCESS_SIGNATURE_SERVICE_PATH = "/SharedAccessSignatureService";
	
	private String host;
	private String password;
	private String token;
	private String username;
}
