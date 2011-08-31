package com.windowsazure.samples.internal.table;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;


public final class TableCollectionDOMAdapter extends AzureDOMAdapter<AzureTableCollection> {

	public TableCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureTableCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureTableCollection collection = new AzureTableCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		String continuation = null;
		String requestId = null;
		String version = null;
		String dateText = null;
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		for (String key : headers.keySet()) {
			if (key.indexOf(CONTINUATION_PREFIX) != -1)
				continuation = key.substring(CONTINUATION_PREFIX.length() + 1, key.length()) + ":" + headers.get(key);
			if (key.equals(AzureHttpHeader.XMS_REQUEST_ID))
				requestId = headers.get(key);
			if (key.equals(AzureHttpHeader.XMS_VERSION))
				version = headers.get(key);
			if (key.equals(HttpHeader.DATE))
				dateText = headers.get(key);
		}
		
		Date date = null;
		try {date = Util.gmtFormatToDate(dateText);} catch(Exception e) {}
		
		String titleText = getInnerText("title");
		String idText = getInnerText("id");
		String updatedText = getInnerText("updated");
		Collection<AzureTable> tables = buildCollection(getNodeCollection("entry"), new TableDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		AzureTableCollection collection = new AzureTableCollection(
				continuation, requestId, version, date,
				idText, titleText, Util.xmlStringToDate(updatedText), tables);
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
	
	private static final String CONTINUATION_PREFIX = "x-ms-continuation-";
}
