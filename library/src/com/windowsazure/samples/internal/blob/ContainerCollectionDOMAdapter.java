package com.windowsazure.samples.internal.blob;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.blob.AzureContainer;
import com.windowsazure.samples.blob.AzureContainerCollection;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.XmlNode;


final public class ContainerCollectionDOMAdapter extends AzureDOMAdapter<AzureContainerCollection> {

	public ContainerCollectionDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	public AzureContainerCollection build()
		throws Exception {
		
		HttpStatusCode statusCode = xmlHttpResult.getStatusCode();
		if (! statusCode.isSuccess()) {
			AzureContainerCollection collection = new AzureContainerCollection();
			collection.setHttpStatusCode(statusCode);
			collection.setErrorCode(getErrorCode());
			return collection;
		}
		
		HttpHeader headers = xmlHttpResult.getHeaders();
		String requestId = headers.get(AzureHttpHeader.XMS_REQUEST_ID);
		String version = headers.get(AzureHttpHeader.XMS_VERSION);
		String dateText = headers.get(HttpHeader.DATE);
		Date date = Util.gmtFormatToDate(dateText);
		
		String prefixText = getOptionalInnerText("Prefix");
		String markerText = getOptionalInnerText("Marker");
		String maxResultsText = getOptionalInnerText("MaxResults");
		
		XmlNode containersNode = getNode("Containers");
		Collection<AzureContainer> containers =
			buildCollection(getNodeCollection(containersNode, "Container"), new ContainerDOMAdapter(xmlHttpResult.getEmptyResult()));
		
		String nextMarkerText = getInnerText("NextMarker");
		AzureContainerCollection collection = new AzureContainerCollection(
				requestId, version, date,
				prefixText, markerText, Util.isStringNullOrEmpty(maxResultsText) ? null : Integer.parseInt(maxResultsText),
				containers, nextMarkerText);
		
		collection.setHttpStatusCode(statusCode);
		return collection;
	}
}
