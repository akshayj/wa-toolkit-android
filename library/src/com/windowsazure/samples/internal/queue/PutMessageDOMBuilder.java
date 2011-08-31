package com.windowsazure.samples.internal.queue;

import com.windowsazure.samples.internal.util.Base64;
import com.windowsazure.samples.internal.xml.AzureDOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;

public final class PutMessageDOMBuilder extends AzureDOMBuilder {

	public PutMessageDOMBuilder(String messageContent) {
		this.messageContent = messageContent;
	}
	
	@Override
	protected void buildDOM() {
		String base64MessageText = new String(Base64.encode(messageContent.getBytes()));
		XmlNode queueMessageNode = addRootNode("QueueMessage");
		addTextNode(queueMessageNode, "MessageText" , base64MessageText);
	}
	
	private String messageContent;
}
