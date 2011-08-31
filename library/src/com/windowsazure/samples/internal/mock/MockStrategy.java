package com.windowsazure.samples.internal.mock;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.internal.AzureOperation;
import com.windowsazure.samples.internal.AzureStrategy;
import com.windowsazure.samples.internal.AzureStrategyContext;
import com.windowsazure.samples.internal.authentication.MockToken;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.HttpUri;
import com.windowsazure.samples.internal.xml.XmlDOM;
import com.windowsazure.samples.internal.xml.XmlNode;


public abstract class MockStrategy extends AzureStrategy<MockToken> {

	@Override
	public <V extends EntityBase>
	V execute(AzureStrategyContext context, Class<? extends Builder<V>> adapterClass)
		throws Exception {
		
		this.context = context;
		this.dom = null;
		
		AzureOperation operation = context.getOperation();
		return performOperation(operation, context);
	}
	
	protected MockStrategy(MockToken token) {
		super(token);
	}

	@Override
	protected void addCommonHeaders(AzureStrategyContext context,
			AzureHttpHeader headers) throws Exception {
	}

	@Override
	protected void addContentHeaders(AzureStrategyContext context,
			AzureHttpHeader headers) throws Exception {
	}

	@Override
	protected void addOperationHeaders(AzureStrategyContext context,
			AzureHttpHeader headers) throws Exception {
	}
	
	protected Date extractDate(String xpath) {
		XmlNode node = getDOM().getSingleNode(xpath);
		if (node == null)
			return null;
		
		String dateText = node.getInnerText();
		return Util.xmlStringToDate(dateText);
	}
	
	protected String extractString(String xpath) {
		XmlNode node = getDOM().getSingleNode(xpath);
		return (node != null) ? node.getInnerText() : "";
	}
	
	protected Collection<XmlNode> getNodes(String xpath) {
		return getDOM().getNodes(xpath);
	}
	
	protected XmlNode getSingleNode(String xpath) {
		return getDOM().getSingleNode(xpath);
	}
	
	protected abstract <V extends EntityBase> V performOperation(AzureOperation operation, AzureStrategyContext context) throws Exception;

	@Override
	protected String getHost(AzureStrategyContext context) throws Exception {
		return null;
	}

	@Override
	protected HttpUri getPath(AzureStrategyContext context) throws Exception {
		return null;
	}
	
	private XmlDOM getDOM() {
		if (dom == null) {
			dom = new XmlDOM();
			dom.fromString(context.getHttpBody());
		}
		return dom;
	}
	
	private AzureStrategyContext context;
	private XmlDOM dom;
}
