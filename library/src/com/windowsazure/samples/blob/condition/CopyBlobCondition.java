package com.windowsazure.samples.blob.condition;

import java.util.Date;

import com.windowsazure.samples.blob.condition.Condition;
import com.windowsazure.samples.internal.util.Util;


public class CopyBlobCondition extends Condition {

	public void ifDestinationDoesntExist() {
		add(IF_NONE_MATCH, "*");
	}
	
	public void ifDestinationMatch(String etag) {
		add(IF_MATCH, etag);
	}
	
	public void ifDestinationModifiedSince(Date date) {
		add(IF_MODIFIED_SINCE, Util.dateToGmtString(date));
	}
	
	public void ifDestinationNoneMatch(String etag) {
		add(IF_NONE_MATCH, etag);
	}
	
	public void ifDestinationUnmodifiedSince(Date date) {
		add(IF_UNMODIFIED_SINCE, Util.dateToGmtString(date));
	}
	
	public void ifSourceDoesntExist() {
		add(XMS_SOURCE_IF_NONE_MATCH, "*");
	}
	
	public void ifSourceMatch(String etag) {
		add(XMS_SOURCE_IF_MATCH, etag);
	}
	
	public void ifSourceModifiedSince(Date date) {
		add(XMS_SOURCE_IF_MODIFIED_SINCE, Util.dateToGmtString(date));
	}
	
	public void ifSourceNoneMatch(String etag) {
		add(XMS_SOURCE_IF_NONE_MATCH, etag);
	}
	
	public void ifSourceUnmodifiedSince(Date date) {
		add(XMS_SOURCE_IF_UNMODIFIED_SINCE, Util.dateToGmtString(date));
	}
}
