package com.windowsazure.samples.internal;

import java.util.Collection;

import com.windowsazure.samples.Metadata;
import com.windowsazure.samples.MetadataCollection;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlDOM;
import com.windowsazure.samples.internal.xml.XmlNode;


public final class MetadataCollectionDOMAdapter extends AzureDOMAdapter<MetadataCollection> {

	public MetadataCollectionDOMAdapter() {
		super((XmlNode) null);
	}
	
	public MetadataCollection build(XmlNode node) {
		dom = new XmlDOM(node);
		return build();
	}
	
	@Override
	public MetadataCollection build() {
		
		MetadataCollection collection = new MetadataCollection();
		
		Collection<XmlNode> nodes = getNodeCollection(ALL_NODES);
		for (XmlNode node : nodes) {
			String name = node.getLocalName();
			String value = node.getInnerText();
			if (name.equals(X_MS_INVALID_NAME))
				collection.add(Metadata.buildInvalid(value));
			else
				collection.add(Metadata.buildValid(name, value));
		}
		
		return collection;
	}
	
	private static final String X_MS_INVALID_NAME = "x-ms-invalid-name";
}
