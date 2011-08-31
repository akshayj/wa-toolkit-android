package com.windowsazure.samples.test;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;

import junit.framework.Assert;

import android.test.AndroidTestCase;

import com.windowsazure.samples.HttpStatusCode;
import com.windowsazure.samples.blob.ACL;
import com.windowsazure.samples.blob.ACLCollection;
import com.windowsazure.samples.blob.Permission;
import com.windowsazure.samples.internal.blob.ACLDOMBuilder;
import com.windowsazure.samples.internal.table.TableCollectionDOMAdapter;
import com.windowsazure.samples.internal.util.Base64;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.HttpHeader;
import com.windowsazure.samples.internal.web.HttpMethod;
import com.windowsazure.samples.internal.web.XmlHttpResult;
import com.windowsazure.samples.internal.xml.XmlDOM;
import com.windowsazure.samples.internal.xml.XmlNode;
import com.windowsazure.samples.table.AzureTable;
import com.windowsazure.samples.table.AzureTableCollection;


public class UtilTests extends AndroidTestCase {
	
	public void testBase64() {
		// Empty
		String encoded = new String(Base64.encode(new byte[0]));
		String decoded = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals("", decoded);
		
		// One character
		encoded = new String(Base64.encode("I".getBytes()));
		decoded = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals("I", decoded);
		
		// Two characters
		encoded = new String(Base64.encode("Or".getBytes()));
		decoded = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals("Or", decoded);
		
		// Three characters
		encoded = new String(Base64.encode("And".getBytes()));
		decoded = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals("And", decoded);
		
		// Single line
		encoded = new String(Base64.encode("How now brown cow.".getBytes()));
		decoded = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals("How now brown cow.", decoded);
	}
	
	public void testBase64MultipleLines() {
		String message = "AAAAAAAAA+AAAAAAAAA+AAAAAAAAA+AAAAAAAAA+AAAAAAAAA+" +
			"BBBBBBBBB+BBBBBBBBB+BBBBBBBBB+BBBBBBBBB+BBBBBBBBB+" + 
			"CCCCCCCCC+CCCCCCCCC+CCCCCCCCC+CCCCCCCCC+CCCCCCCCC+" +
			"DDDDDDDDD+DDDDDDDDD+DDDDDDDDD+DDDDDDDDD+DDDDDDDDD.";
		String encoded = new String(Base64.encode(message.getBytes()));
		String actual = new String(Base64.decode(encoded.getBytes()));
		Assert.assertEquals(message, actual);
	}
	
	public void testDateToGmt() {
		try
		{
			String expected = "Sun, 02 Jan 2011 03:04:05 GMT";
			String actual = Util.dateToGmtString(new Date(2011 - 1900, 1 - 1, 2, 3, 4, 5));
			Assert.assertEquals(expected, actual);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDateToXmlStringWithoutTZ() {
		try
		{
			String expected = "2011-01-02T03:04:05";
			String actual = Util.dateToXmlStringWithoutTZ(new Date(2011 - 1900, 1 - 1, 2, 3, 4, 5));
			Assert.assertEquals(expected, actual);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testDateToXmlStringWithTZ() {
		try
		{
			String expected = "2011-01-02T03:04:05+0000";
			String actual = Util.dateToXmlStringWithTZ(new Date(2011 - 1900, 1 - 1, 2, 3, 4, 5));
			Assert.assertEquals(expected, actual);
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testTokenizer() {
		try
		{
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>" +
			    "<feed xml:base=\"http://myaccount.tables.core.windows.net/\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns=\"http://www.w3.org/2005/Atom\">" +
					"<title type=\"text\">Tables</title>" +
					"<id>http://myaccount.tables.core.windows.net/Tables</id>" +
					"<updated>2009-01-04T17:18:54.7062347Z</updated>" +
					"<link rel=\"self\" title=\"Tables\" href=\"Tables\" />" +
					"<entry>" +
					    "<id>http://myaccount.tables.core.windows.net/Tables('mytable')</id>" +
					    "<title type=\"text\">Tokenizer Test</title>" +
					    "<updated>2009-01-04T17:18:54.7062347Z</updated>" +
					    "<author>" +
					        "<name>JUnit</name>" +
					    "</author>" +
					    "<link rel=\"edit\" title=\"Tables\" href=\"Tables('mytable')\" />" +
					    "<category term=\"myaccount.Tables\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />" +
					    "<content type=\"application/xml\">" +
					        "<m:properties>" +
					        	"<d:TableName>mytable</d:TableName>" +
					        "</m:properties>" +
					    "</content>" +
					"</entry>" +
				"</feed>"; 
	
			XmlHttpResult xmlHttpResult = new XmlHttpResult(HttpMethod.GET, HttpStatusCode.OK, new HttpHeader(), null);
			xmlHttpResult.setXmlString(xml);
			TableCollectionDOMAdapter adapter = new TableCollectionDOMAdapter(xmlHttpResult);
			AzureTableCollection collection = adapter.build();
			
			Assert.assertEquals(1, collection.getTables().size());
			
			AzureTable table = collection.iterator().next();
			Assert.assertEquals("Tokenizer Test", table.getTitle());
			Assert.assertEquals("JUnit", table.getAuthorName());
			Assert.assertEquals("mytable", table.getTableName());
		}
		catch (Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	public void testXpath() {
		ACLCollection aclCollection = new ACLCollection();
		Date start = new Date(2012 - 1900, 1 - 1, 1);
		Date expiry = new Date(2012 - 1900, 1 - 1, 2);
		aclCollection.add(ACL.newACL(start, expiry, EnumSet.of(Permission.READ)));
		aclCollection.add(ACL.newACL(start, expiry, EnumSet.of(Permission.WRITE)));
		
		String xml = new ACLDOMBuilder(aclCollection).getXmlString(false);
		XmlDOM dom = new XmlDOM();
		dom.fromString(xml);
		
		XmlNode node = dom.getSingleNode("/");
		Assert.assertEquals("SignedIdentifiers", node.getLocalName());
		
		node = dom.getSingleNode("/SignedIdentifiers/SignedIdentifier/AccessPolicy/Permission");
		Assert.assertEquals("r", node.getInnerText());
		
		Collection<XmlNode> nodes = dom.getNodes("//AccessPolicy/Expiry");
		Assert.assertEquals(2, nodes.size());
	}
}
