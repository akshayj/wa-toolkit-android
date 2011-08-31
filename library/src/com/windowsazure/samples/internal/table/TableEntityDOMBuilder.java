package com.windowsazure.samples.internal.table;

import java.util.Date;

import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.xml.AzureDOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;

public final class TableEntityDOMBuilder extends AzureDOMBuilder {

	public TableEntityDOMBuilder(String title, Date updated, String authorName, PropertyCollection properties) {
		this.title = title;
		this.updated = updated;
		this.authorName = authorName;
		this.properties = properties;
	}
	
	@Override
	protected void buildDOM() {
		XmlNode entryNode = addRootNode("entry");
		addDefaultNamespace(entryNode, ATOM_NS);
		addNamespace(entryNode, "d", DATA_SERVICES_NS);
		addNamespace(entryNode, "m", METADATA_NS);
		
		addTextNode(entryNode, "title" , title);
		addTextNode(entryNode, "updated" , Util.dateToXmlStringWithTZ(updated));
		
		XmlNode authorNode = addNode(entryNode, "author");
		addTextNode(authorNode, "name", authorName);
		
		addTextNode(entryNode, "id", null);
		
		XmlNode contentNode = addNode(entryNode, "content");
		addAttribute(contentNode, "type", "application/xml");
		
		XmlNode propertiesNode = addNode(contentNode, "m:properties");
		for (Property<?> property : properties) {
			XmlNode propertyNode = addTextNode(propertiesNode, "d", property.getName(), property.getRepresentation());
			addAttribute(propertyNode, "m", "type", property.getEdmType().toString());
		}
	}
	
	private String authorName;
	private PropertyCollection properties;
	private String title;
	private Date updated;
}
