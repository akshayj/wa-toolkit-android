package com.windowsazure.samples.blob;

import java.util.Set;

public enum EnumerationFilter {
	METADATA,
	SNAPSHOTS,
	UNCOMMITED_BLOBS;
	
	public static String toString(Set<EnumerationFilter> set) {
		StringBuilder sb = new StringBuilder();
		for (EnumerationFilter enumerationFilter : set) {
			if (sb.length() > 0)
				sb.append(URL_ENCODED_COMMA);
			sb.append(enumerationFilter.toString());
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
	
	private static final String URL_ENCODED_COMMA = "%82";
}
