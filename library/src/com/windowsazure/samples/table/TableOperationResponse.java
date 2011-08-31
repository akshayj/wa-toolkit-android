package com.windowsazure.samples.table;

import java.util.Date;

import com.windowsazure.samples.EntityBase;


public class TableOperationResponse extends EntityBase {

	public TableOperationResponse() {
		this(null, null, null, null);
	}
	
	public TableOperationResponse(String etag, String requestId, String version, Date date) {
		this.etag = etag;
		this.requestId = requestId;
		this.version = version;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getEtag() {
		return etag;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String getVersion() {
		return version;
	}
	
	private Date date;
	private String etag;
	private String requestId;
	private String version;
}
