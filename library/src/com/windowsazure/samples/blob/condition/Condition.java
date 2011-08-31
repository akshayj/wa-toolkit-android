package com.windowsazure.samples.blob.condition;

import java.util.HashMap;
import java.util.Map;


public abstract class Condition {

	public static final String IF_MATCH = "If-Match";
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String IF_NONE_MATCH = "If-None-Match";
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	
	public Map<String, String> getConditions() {
		return conditions;
	}
	
	protected static final String XMS_SOURCE_IF_MATCH = "x-ms-source-if-match";
	protected static final String XMS_SOURCE_IF_MODIFIED_SINCE = "x-ms-source-if-modified-since";
	protected static final String XMS_SOURCE_IF_NONE_MATCH = "x-ms-source-if-none-match";
	protected static final String XMS_SOURCE_IF_UNMODIFIED_SINCE = "x-ms-source-if-unmodified-since";
	
	protected void add(String key, String value) {
		if (conditions.containsKey(key))
			conditions.remove(key);
		conditions.put(key, value);
	}
	
	private HashMap<String, String> conditions = new HashMap<String, String>();
}
