package com.windowsazure.samples.blob.condition;

import java.util.Date;

import com.windowsazure.samples.blob.condition.Condition;
import com.windowsazure.samples.internal.util.Util;


public class GetBlobPropertiesCondition extends Condition {
	
	public void ifModifiedSince(Date date) {
		add(IF_MODIFIED_SINCE, Util.dateToGmtString(date));
	}
	
	public void ifUnmodifiedSince(Date date) {
		add(IF_UNMODIFIED_SINCE, Util.dateToGmtString(date));
	}
}
