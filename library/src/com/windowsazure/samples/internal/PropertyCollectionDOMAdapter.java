package com.windowsazure.samples.internal;

import java.util.Collection;

import com.windowsazure.samples.EdmType;
import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlAttribute;
import com.windowsazure.samples.internal.xml.XmlDOM;
import com.windowsazure.samples.internal.xml.XmlNode;

public final class PropertyCollectionDOMAdapter extends AzureDOMAdapter<PropertyCollection> {

	public PropertyCollectionDOMAdapter() {
		super((XmlNode) null);
	}
	
	public PropertyCollection build(XmlNode node)
		throws Exception {
		
		dom = new XmlDOM(node);
		return build();
	}

	@Override
	public PropertyCollection build()
		throws Exception {
		
		PropertyCollection collection = new PropertyCollection();
		
		Collection<XmlNode> nodes = getNodeCollection(ALL_NODES);
		for (XmlNode node : nodes) {
			String propertyName = node.getLocalName();
			String valueRepresentation = node.getInnerText();
			
			XmlAttribute attribute = node.getAttributeByLocalName(Property.TYPE);
			String typeAttribute = (attribute != null) ? attribute.getValue() : null;
			EdmType edmType = (EdmType) (Util.isStringNullOrEmpty(typeAttribute) ? 
					Property.DEFAULT_TYPE : EdmType.fromRepresentation(typeAttribute));
			collection.add(Property.fromRepresentation(propertyName, edmType, valueRepresentation));
		}
		
		return collection;
	}
}
