package com.windowsazure.samples.blob.condition;

import com.windowsazure.samples.blob.condition.CommonConditions;


public class PutPageCondition extends CommonConditions {
	
	public void ifSequenceNumberEqual(int sequenceNumber) {
		add("x-ms-if-sequence-number-eq", Integer.toString(sequenceNumber));
	}
	
	public void ifSequenceNumberLessThan(int sequenceNumber) {
		add("x-ms-if-sequence-number-lt", Integer.toString(sequenceNumber));
	}
	
	public void ifSequenceNumberLessThanOrEqual(int sequenceNumber) {
		add("x-ms-if-sequence-number-lte", Integer.toString(sequenceNumber));
	}
}
