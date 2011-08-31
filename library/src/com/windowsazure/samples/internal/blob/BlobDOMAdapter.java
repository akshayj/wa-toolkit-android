package com.windowsazure.samples.internal.blob;

import java.util.Date;

import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.BlobType;
import com.windowsazure.samples.blob.LeaseStatus;
import com.windowsazure.samples.internal.MetadataCollectionDOMAdapter;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.AzureDOMAdapter;
import com.windowsazure.samples.internal.xml.NodeNotFoundException;
import com.windowsazure.samples.internal.xml.XmlNode;


final class BlobDOMAdapter extends AzureDOMAdapter<AzureBlob> {

	@Override
	public AzureBlob build()
		throws NodeNotFoundException {
		
		setter.clear();
		setter.setBlobName(getInnerText("Name"));
		
		setter.setSnapshot(getOptionalInnerText("Snapshot"));
		
		setter.setUrl(getInnerText("Url"));
		
		XmlNode propertiesNode = getOptionalNode("Properties");
		if (propertiesNode != null) {
			String lastModifiedText = getOptionalInnerText(propertiesNode, "Last-Modified");
			Date lastModified = null;
			try {lastModified = Util.gmtFormatToDate(lastModifiedText);}catch(Exception e){}
			setter.setLastModified(lastModified);
			
			setter.setEtag(getOptionalInnerText(propertiesNode, "Etag"));
			
			String contentLengthText = getOptionalInnerText(propertiesNode, "Content-Length");
			Integer contentLength = null;
			try {contentLength = Integer.parseInt(contentLengthText);} catch (Exception e) {}
			setter.setContentLength(contentLength);
			
			setter.setContentType(getOptionalInnerText(propertiesNode, "Content-Type"));
			setter.setContentEncoding(getOptionalInnerText(propertiesNode, "Content-Encoding"));
			setter.setContentLanguage(getOptionalInnerText(propertiesNode, "Content-Language"));
			setter.setContentMd5(getOptionalInnerText(propertiesNode, "Content-MD5"));
			setter.setCacheControl(getOptionalInnerText(propertiesNode, "Cache-Control"));
			setter.setSequenceNumber(getOptionalInnerText(propertiesNode, "x-ms-blob-sequence-number"));
			
			String blobTypeText = getOptionalInnerText(propertiesNode, "BlobType");
			if (! Util.isStringNullOrEmpty(blobTypeText))
				setter.setBlobType(BlobType.fromRepresentation(blobTypeText));
			
			String leaseStatusText = getOptionalInnerText(propertiesNode, "LeaseStatus");
			if (! Util.isStringNullOrEmpty(leaseStatusText))
				setter.setLeaseStatus(LeaseStatus.fromString(leaseStatusText));
		} else {
			String lastModifiedText = getOptionalInnerText("LastModified");
			Date lastModified = null;
			try {lastModified = Util.gmtFormatToDate(lastModifiedText);}catch(Exception e){}
			setter.setLastModified(lastModified);
			
			setter.setEtag(getOptionalInnerText("Etag"));
			
			String contentLengthText = getOptionalInnerText("ContentLength");
			Integer contentLength = null;
			try {contentLength = Integer.parseInt(contentLengthText);} catch (Exception e) {}
			setter.setContentLength(contentLength);
			
			setter.setContentType(getOptionalInnerText("ContentType"));
			setter.setContentEncoding(getOptionalInnerText("ContentEncoding"));
			setter.setContentLanguage(getOptionalInnerText("ContentLanguage"));
			setter.setContentMd5(getOptionalInnerText("ContentMD5"));
			setter.setCacheControl(getOptionalInnerText("CacheControl"));
			setter.setSequenceNumber(getOptionalInnerText("x-ms-blob-sequence-number"));
			
			String blobTypeText = getOptionalInnerText("BlobType");
			if (! Util.isStringNullOrEmpty(blobTypeText))
				setter.setBlobType(BlobType.fromRepresentation(blobTypeText));
			
			String leaseStatusText = getOptionalInnerText("LeaseStatus");
			if (! Util.isStringNullOrEmpty(leaseStatusText))
				setter.setLeaseStatus(LeaseStatus.fromString(leaseStatusText));
		}
		
		XmlNode metadataNode = getOptionalNode("Metadata");
		if (metadataNode != null)
			setter.setMetadata(metadataAdapter.build(metadataNode));
		
		return new AzureBlob(setter);
	}
	
	protected BlobDOMAdapter(XmlHttpResult result) {
		super(result);
	}
	
	@Override
	protected String getOptionalInnerText(XmlNode parent, String localName) {
		return (parent != null) ? super.getOptionalInnerText(parent, localName) : null;
	}
	
	private static final MetadataCollectionDOMAdapter metadataAdapter = new MetadataCollectionDOMAdapter();
	private static final AzureBlobSetter setter = new AzureBlobSetter();
}
