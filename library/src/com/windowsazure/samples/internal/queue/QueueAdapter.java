package com.windowsazure.samples.internal.queue;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.internal.MetadataCollectionDOMAdapter;
import com.windowsazure.samples.internal.util.Coalescer;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;
import com.windowsazure.samples.queue.AzureQueue;


final class QueueDOMAdapter extends AzureDOMAdapter<AzureQueue> {

	@Override
	public AzureQueue build()
		throws Exception {
		
		// The <Name> node is name "QueueName" from direct-connect, and "Name" from the proxy.
		// "Name" is the correct form.
		XmlNode nameNode = getOptionalNode("Name");
		XmlNode queueNameNode = getOptionalNode("QueueName");
		String nameText = new Coalescer<XmlNode>().coalesce(nameNode, queueNameNode).getInnerText();
		
		String urlText = getInnerText("Url");
		XmlNode metadataNode = getOptionalNode("Metadata");
		
		MetadataCollection metadata = (metadataNode != null) ?
				metadataAdapter.build(metadataNode) :
				new MetadataCollection();
		
		return new AzureQueue(nameText, urlText, metadata);
	}
	
	protected QueueDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	private static final MetadataCollectionDOMAdapter metadataAdapter = new MetadataCollectionDOMAdapter();
}
