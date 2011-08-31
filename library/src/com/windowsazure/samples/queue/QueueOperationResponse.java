package com.windowsazure.samples.queue;

import java.util.Date;

import com.windowsazure.samples.EntityBase;


public class QueueOperationResponse extends EntityBase {

	public QueueOperationResponse() {
		this(null, null, null);
	}
	
	public QueueOperationResponse(String requestId, String version, Date date) {
		this.requestId = requestId;
		this.version = version;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	private Date date;
	private String requestId;
	private String version;
}
