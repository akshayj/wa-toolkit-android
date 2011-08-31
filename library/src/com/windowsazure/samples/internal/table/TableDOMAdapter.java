package com.windowsazure.samples.internal.table;

import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.table.AzureTable;

public final class TableDOMAdapter extends AzureDOMAdapter<AzureTable> {

	public TableDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureTable build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureTable table = new AzureTable();
			table.setHttpStatusCode(statusCode);
			table.setErrorCode(getErrorCode());
			return table;
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
		String tableName = extractTableNameFromTableId(idText);
		
		AzureTable table = new AzureTable(
				requestId, version, date,
				idText, titleText, Util.xmlStringToDate(updatedText),
				authorNameText, tableName);
		table.setHttpStatusCode(statusCode);
		return table;
	}
}
