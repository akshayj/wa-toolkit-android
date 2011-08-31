package com.windowsazure.samples.internal.table;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.PropertyCollectionDOMAdapter;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;
import com.windowsazure.samples.table.AzureTableEntity;

public final class TableEntityDOMAdapter extends AzureDOMAdapter<AzureTableEntity> {

	public TableEntityDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureTableEntity build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureTableEntity entity = new AzureTableEntity();
			entity.setHttpStatusCode(statusCode);
			entity.setErrorCode(getErrorCode());
			return entity;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		String idText = getInnerText("id");
		String titleText = getInnerText("title");
		String updatedText = getInnerText("updated");
		String authorNameText = getInnerText(getNode("author"), "name");
		String tableName = extractTableNameFromEntityId(idText);
		
		XmlNode contentNode = getNode("content");
		XmlNode propertyNode = getNode(contentNode, "properties");
		PropertyCollection properties = (propertyNode != null) ?
				propertyAdapter.build(propertyNode) :
				new PropertyCollection();
		
		AzureTableEntity entity = new AzureTableEntity(
			requestId, version, date,
			idText, titleText, Util.xmlStringToDate(updatedText), authorNameText,
			tableName, properties.getPartitionKey(), properties.getRowKey(),
			properties);
		
		entity.setHttpStatusCode(statusCode);
		return entity;
	}
	
	private static final PropertyCollectionDOMAdapter propertyAdapter = new PropertyCollectionDOMAdapter();
}
