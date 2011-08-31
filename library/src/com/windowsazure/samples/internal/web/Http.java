package com.windowsazure.samples.internal.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Logger;
import com.windowsazure.samples.internal.util.Util;

public final class Http {

	public static HttpResult Get(String host, String path, HttpHeader headers)
		throws	Exception {
	
		return Imp(HttpMethod.GET, host, HttpHeader.PORT_NORMAL, path, headers, null);
	}
	
	public static HttpResult GetSSL(String host, String path, HttpHeader headers)
		throws	Exception {
	
		return Imp(HttpMethod.GET, host, HttpHeader.PORT_SSL, path, headers, null);
	}
	
	public static HttpResult Post(String host, String path, HttpHeader headers, String data)
		throws	Exception {
		
		return Imp(HttpMethod.POST, host, HttpHeader.PORT_NORMAL, path, headers, data);
	}
	
	public static HttpResult PostSSL(String host, String path, HttpHeader headers, String data)
		throws	Exception {
		
		return Imp(HttpMethod.POST, host, HttpHeader.PORT_SSL, path, headers, data);
	}
	
	public static HttpResult Rest(HttpMethod httpMethod, String host, String path, HttpHeader headers, String data)
		throws	Exception {
		
		return Imp(httpMethod, host, HttpHeader.PORT_NORMAL, path, headers, data);
	}
	
	public static HttpResult RestSSL(HttpMethod httpMethod, String host, String path, HttpHeader headers, String data)
		throws	Exception {
		
		return Imp(httpMethod, host, HttpHeader.PORT_SSL, path, headers, data);
	}
	
	protected static HttpResult Imp(HttpMethod method, String host, int port, String path, HttpHeader headers, String data)
		throws	Exception {
		
		Logger.verbose("Http", "begin");
		boolean hasContent = method.hasContent() && data != null;
		if (hasContent) {
		    if (! headers.containsKey(HttpHeader.CONTENT_TYPE))
		    	headers.put(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_TYPE_TEXT);
		    
		    if (! headers.containsKey(HttpHeader.CONTENT_LENGTH))
		    	headers.put(HttpHeader.CONTENT_LENGTH, data.length());
	    }
		
		StringBuffer sb = new StringBuffer();
		sb.append(method.toString() + " "  + path + " HTTP/1.1\r\n");
		sb.append(HttpHeader.HOST + ": " + host + ":" + port + "\r\n");
		if (! headers.containsKey(HttpHeader.CONNECTION))
			sb.append(HttpHeader.CONNECTION + ": " + HttpHeader.CONNECTION_CLOSE + "\r\n");
	    for (Map.Entry<String, String> entry : headers.entrySet()) {
	    	String headerValue = entry.getValue();
	    	if (! Util.isStringNullOrEmpty(headerValue))
	    		sb.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
	    }
	    sb.append("\r\n");
	    String request = sb.toString();
		
		SocketFactory socketFactory = (port == HttpHeader.PORT_SSL) ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
		InetAddress ip = InetAddress.getByName(host);
		Socket socket = socketFactory.createSocket(ip, port);
		
		//Logger.verbose("Http", "writing");
		OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
		int requestLength = request.length() + (hasContent ? data.length() : 0);
		BufferedWriter bw = new BufferedWriter(osw, requestLength);
		bw.write(request);
	    if (hasContent)
	    	bw.write(data);
	    bw.flush();
	    bw.close();
	    osw.close();
		
	    //Logger.verbose("Http", "reading");
		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		BufferedReader br = new BufferedReader(isr, 8192);
		HttpResult result = buildResult(method, br);
		
		br.close();
		isr.close();
		socket.close();
		Logger.verbose("Http", "end");
		return result;
	}
	
	private static HttpResult buildResult(HttpMethod method, BufferedReader reader)
		throws Exception {
		
		// Extract the Http status from the response line.
		String statusLine = reader.readLine();
		String[] parts = statusLine.split(" ");
		int status = Integer.parseInt(parts[1].trim());
		HttpStatusCode statusCode = HttpStatusCode.fromInt(status);
		
		// Extract the headers.
		HttpHeader headers = new HttpHeader();
		String headerLine = reader.readLine().trim();
		while (headerLine.length() > 0) {
			parts = headerLine.split(":");
			headers.put(parts[0].trim(), parts[1].trim());
			headerLine = reader.readLine();
		}
		
		// Extract the body.
		StringBuffer sb = new StringBuffer();
		String bodyLine = reader.readLine();
		while (bodyLine != null) {
			sb.append(bodyLine);
			bodyLine = reader.readLine();
		}
		String body = sb.toString();
		
		return new HttpResult(method, statusCode, headers, body);
	}
	
	private Http() {}
}
