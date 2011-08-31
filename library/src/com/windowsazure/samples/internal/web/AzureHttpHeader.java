package com.windowsazure.samples.internal.web;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import com.windowsazure.samples.internal.util.Pair;

public final class AzureHttpHeader extends HttpHeader {
	
	public static final String AUTH_TOKEN = "AuthToken";
	public static final String DATA_SERVICE_VERSION = "DataServiceVersion";
	public static final String     DATA_SERVICE_VERSION_ONE = "1.0;NetFx";
	public static final String IF_MATCH = "If-Match";
	public static final String MAX_DATA_SERVICE_VERSION = "MaxDataServiceVersion";
	public static final String     MAX_DATA_SERVICE_VERSION_ONE = "1.0;NetFx";
	public static final String     MAX_DATA_SERVICE_VERSION_TWO = "2.0;NetFx";
	
	public static final String XMS_CONTENT_ENCODING = "x-ms-content-encoding";
	public static final String XMS_CONTENT_TYPE = "x-ms-content-type";
	public static final String XMS_Date = "x-ms-date";
	public static final String XMS_META_PREFIX = "x-ms-meta-";
	public static final String XMS_REQUEST_ID = "x-ms-request-id";
	public static final String XMS_VERSION = "x-ms-version";
	public static final String     XMS_VERSION_VALUE = "2009-09-19";
	
	public String toCanonicalizedForm() {
		
		// 1. Retrieve all headers for the resource that begin with x-ms-
		Vector<Pair<String, String>> xmsHeaders = new Vector<Pair<String, String>>();
		for (Entry<String,String> item : this.entrySet()) {
			if (item.getKey().indexOf(CANONICALIZED_HEADER_PREFIX) == 0)
				xmsHeaders.add(new Pair<String, String>(item.getKey(), item.getValue()));
		}
		
		// 2. Convert each HTTP header name to lowercase.
		for (Pair<String, String> header : xmsHeaders) {
			header.setFirst(header.getFirst().toLowerCase());
		}
		
		// 3. Sort the headers lexicographically by header name, in ascending order.
		Collections.sort(xmsHeaders, new HeaderComparator());
		
		// 6. Concatenate all the headers into a single string, terminating each name:value pair with a newline character.
		StringBuilder sb = new StringBuilder();
		for (Pair<String, String> header : xmsHeaders) {
			sb.append(header.getFirst() + ":" + header.getSecond() + "\n");
		}
		
		if (sb.length() == 0)
			sb.append('\n');
		return sb.toString();
	}
	
	private class HeaderComparator implements Comparator<Pair<String, String>> {
		@Override
		public int compare(Pair<String, String> arg0, Pair<String, String> arg1) {
			return arg0.getFirst().compareTo(arg1.getFirst());
		}
	}
	
	private static final String CANONICALIZED_HEADER_PREFIX = "x-ms-";
	private static final long serialVersionUID = -8771482885053048426L;

}
