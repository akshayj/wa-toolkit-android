package com.windowsazure.samples.internal.web;

import com.windowsazure.samples.internal.xml.XmlDOM;

public final class XmlHttp {
	
	public static XmlHttpResult Get(String host, String path, HttpHeader headers)
		throws	Exception {
		
		return Imp(HttpMethod.GET, host, HttpHeader.PORT_NORMAL, path, headers, null);
	}
	
	public static XmlHttpResult GetSSL(String host, String path, HttpHeader headers)
		throws	Exception {
		
		return Imp(HttpMethod.GET, host, HttpHeader.PORT_SSL, path, headers, null);
	}
	
	public static XmlHttpResult Post(String host, String path, HttpHeader headers, String xmlData)
		throws	Exception {
		
		return Imp(HttpMethod.POST, host, HttpHeader.PORT_NORMAL, path, headers, xmlData);
	}
	
	public static XmlHttpResult PostSSL(String host, String path, HttpHeader headers, String xmlData)
		throws	Exception {
		
		return Imp(HttpMethod.POST, host, HttpHeader.PORT_SSL, path, headers, xmlData);
	}
	
	public static XmlHttpResult Rest(HttpMethod method, String host, String path, HttpHeader headers, String xmlData)
		throws	Exception {
		
		return Imp(method, host, HttpHeader.PORT_NORMAL, path, headers, xmlData);
	}
	
	public static XmlHttpResult RestSSL(HttpMethod method, String host, String path, HttpHeader headers, String xmlData)
		throws	Exception {
		
		return Imp(method, host, HttpHeader.PORT_SSL, path, headers, xmlData);
	}
	
	private static XmlHttpResult Imp(HttpMethod method, String host, int port, String path, HttpHeader headers, String xmlData)
		throws	Exception {
	
		if (method.hasContent()) {
			if (headers == null)
				headers = new HttpHeader();
			
			if (! headers.containsKey(HttpHeader.CONTENT_TYPE))
				headers.put(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_TYPE_XML);
			
			if (! headers.containsKey(HttpHeader.CONTENT_LENGTH))
				headers.put(HttpHeader.CONTENT_LENGTH, xmlData.length());
		}
		
		HttpResult httpResult = Http.Imp(method, host, port, path, headers, xmlData);
		XmlHttpResult xmlHttpResult = new XmlHttpResult(
				httpResult.getMethod(),
				httpResult.getStatusCode(),
				httpResult.getHeaders(),
				httpResult.getBody());
		
		String body = xmlHttpResult.getBody();
		int lastTagIndex = body.lastIndexOf("</");
		if (lastTagIndex == -1) {
			xmlHttpResult.setXmlString(XmlDOM.EmptyXml);
			return xmlHttpResult;
		}
		
		int endOfLastTagIndex = body.indexOf('>', lastTagIndex);
		if (endOfLastTagIndex == -1)
			throw new XmlMalformedException();
		
		String rootTag = body.substring(lastTagIndex + 2, endOfLastTagIndex);
		int firstTagIndex = body.indexOf("<" + rootTag);
		if (firstTagIndex == -1)
			throw new XmlMalformedException();
		String xmlString = body.substring(firstTagIndex, endOfLastTagIndex + 1);
		xmlHttpResult.setXmlString(xmlString);
		
		return xmlHttpResult;
	}

	private XmlHttp() {}
}
