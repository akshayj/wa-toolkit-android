package com.windowsazure.samples.internal.table;

import java.util.Date;

import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.xml.AzureDOMBuilder;
import com.windowsazure.samples.internal.xml.XmlNode;

public final class CreateTableDOMBuilder extends AzureDOMBuilder {

	public CreateTableDOMBuilder(String title, Date updated, String authorName, String tableName) {
		this.title = title;
		this.updated = updated;
		this.authorName = authorName;
		this.tableName = tableName;
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
		XmlNode propertiesNode = addNode(contentNode, "m", "properties");
		addTextNode(propertiesNode, "d", "TableName", tableName);
	}
	
	private String authorName;
	private String tableName;
	private String title;
	private Date updated;
}
