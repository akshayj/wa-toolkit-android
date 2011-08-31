package com.windowsazure.samples.blob;

public enum BlockStatus {
	COMMITTED("CommittedBlocks"),
	UNCOMMITTED("UncommittedBlocks");
	
	public String getXmlNodeName() {
		return xmlNodeName;
	}
	
	private BlockStatus(String xmlNodeName) {
		this.xmlNodeName = xmlNodeName;
	}
	
	private String xmlNodeName;
}
