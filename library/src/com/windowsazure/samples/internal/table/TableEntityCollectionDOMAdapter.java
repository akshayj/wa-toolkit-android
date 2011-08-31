package com.windowsazure.samples.internal.table;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;


public final class TableEntityCollectionDOMAdapter extends AzureDOMAdapter<AzureTableEntityCollection> {

	public TableEntityCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureTableEntityCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureTableEntityCollection collection = new AzureTableEntityCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String nextPartitionKey = headers.get("x-ms-continuation-NextPartitionKey");
		String nextRowKey = headers.get("x-ms-continuation-NextRowKey");
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch (Exception e) {}
		
		String titleText = getInnerText("title");
		String idText = getInnerText("id");
		String updatedText = getInnerText("updated");
		String tableName = extractTableNameFromEntityId(idText);
		Collection<AzureTableEntity> entities =
			buildCollection(getNodeCollection("entry"), new TableEntityDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		AzureTableEntityCollection collection = new AzureTableEntityCollection(
			nextPartitionKey, nextRowKey, requestId, version, date,
			titleText, idText, Util.xmlStringToDate(updatedText),
			tableName, entities);
		
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
