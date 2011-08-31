package com.windowsazure.samples.blob.condition;

import java.util.Date;

import com.windowsazure.samples.blob.condition.Condition;
import com.windowsazure.samples.internal.util.Util;


public abstract class CommonConditions extends Condition {

	public void ifDoesntExist() {
		add(IF_NONE_MATCH, "*");
	}
	
	public void ifMatch(String etag) {
		add(IF_MATCH, etag);
	}
	
	public void ifModifiedSince(Date date) {
		add(IF_MODIFIED_SINCE, Util.dateToGmtString(date));
	}
	
	public void ifNoneMatch(String etag) {
		add(IF_NONE_MATCH, etag);
	}
	
	public void ifUnmodifiedSince(Date date) {
		add(IF_UNMODIFIED_SINCE, Util.dateToGmtString(date));
	}
}
