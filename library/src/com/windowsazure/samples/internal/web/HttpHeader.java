package com.windowsazure.samples.internal.web;

import java.util.HashMap;

public class HttpHeader extends HashMap<String, String>  {

	public static final String ACCEPT = "Accept";
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String     ACCEPT_ENCODING_COMPRESS = "compress";
	public static final String     ACCEPT_ENCODING_DEFLATE = "deflate";
	public static final String     ACCEPT_ENCODING_GZIP = "gzip";
	public static final String     ACCEPT_ENCODING_IDENTITY = "identity";
	public static final String     ACCEPT_ENCODING_SDCH = "sdch";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String     CACHE_CONTROL_NO_CACHE = "no-cache";
	public static final String CONNECTION = "Connection";
	public static final String     CONNECTION_CLOSE = "close";
	public static final String     CONNECTION_KEEP_ALIVE = "Keep-Alive";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LANGUAGE = "Content-Language";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_MD5 = "Content-MD5";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String     CONTENT_TYPE_ATOM = "application/atom+xml";
	public static final String     CONTENT_TYPE_HTML = "text/html";
	public static final String     CONTENT_TYPE_TEXT = "text/plain";
	public static final String     CONTENT_TYPE_XML = "text/xml";
	public static final String DATE = "Date";
	public static final String HOST = "Host";
	public static final String USER_AGENT = "User-Agent";
	public static final String     USER_AGENT_NATIVE_HOST = "NativeHost";
	
	public static final int PORT_NORMAL = 80;
	public static final int PORT_SSL = 443;
	
	public void put(String key, int value) {
		super.put(key, "" + value);
	}
	
	private static final long serialVersionUID = 6212396518823210447L;
}
