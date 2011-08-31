package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.blob.AzureContainer;
import com.windowsazure.samples.internal.MetadataCollectionDOMAdapter;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;


final class ContainerDOMAdapter extends AzureDOMAdapter<AzureContainer> {

	@Override
	public AzureContainer build()
		throws Exception {
		
		String name = getInnerText("Name");
		String url = getInnerText("Url");
		
		XmlNode propertiesNode = getNode("Properties");
		String lastModifiedText = getInnerText(propertiesNode, "Last-Modified");
		Date lastModified = Util.gmtFormatToDate(lastModifiedText);
		String etag = getInnerText(propertiesNode, "Etag");
		
		XmlNode metadataNode = getOptionalNode("Metadata");
		MetadataCollection metadata = (metadataNode != null) ?
				metadataAdapter.build(metadataNode) :
				new MetadataCollection();
		
		return new AzureContainer(name, url, lastModified, etag, metadata);
	}
	
	protected ContainerDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	private static final MetadataCollectionDOMAdapter metadataAdapter = new MetadataCollectionDOMAdapter();
}
