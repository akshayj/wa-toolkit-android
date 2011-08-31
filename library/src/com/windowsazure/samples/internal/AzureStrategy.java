package com.windowsazure.samples.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.windowsazure.samples.EntityBase;
import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.blob.AzureBlob;
import com.windowsazure.samples.blob.BlobOperationResponse;
import com.windowsazure.samples.blob.BlobRange;
import com.windowsazure.samples.blob.data.BlobData;
import com.windowsazure.samples.internal.blob.BlobHeaderAdapter;
import com.windowsazure.samples.internal.blob.BlobOperationResponseAdapter;
import com.windowsazure.samples.internal.util.Builder;
import com.windowsazure.samples.internal.util.Logger;
import com.windowsazure.samples.internal.util.Util;
import com.windowsazure.samples.internal.web.AzureHttpHeader;
import com.windowsazure.samples.internal.web.Http;
import com.windowsazure.samples.internal.web.HttpMethod;
import com.windowsazure.samples.internal.web.HttpResult;
import com.windowsazure.samples.internal.web.HttpUri;
import com.windowsazure.samples.internal.web.XmlHttp;
import com.windowsazure.samples.internal.web.XmlHttpResult;


public abstract class AzureStrategy<T extends AuthenticationToken> {
	
	public <V extends EntityBase>
		V execute(AzureStrategyContext context, Class<? extends Builder<V>> adapterClass)
		throws Exception {
		 
		try
		{
			Logger.verbose("AzureStrategy", context.getOperation().toString() + " begin");
			onBeginExecute(context);
			AzureOperation operation = context.getOperation();
			HttpMethod httpMethod = operation.getHttpMethod();
			String host = getHost(context);
			HttpUri path = getPath(context);
			AzureHttpHeader headers = getHeaders(context);
			String httpBody = context.getHttpBody();
			
			Builder<V> adapter = null;
			
			switch (operation.getReturnType()) {
				case EMPTY: {
					HttpResult httpResult = Http.RestSSL(httpMethod, host, path.toString(), headers, httpBody);
					Constructor<? extends Builder<V>> adapterConstructor = adapterClass.getConstructor(new Class[] {HttpResult.class});
					adapter = adapterConstructor.newInstance(httpResult);
					break;
				}
				
				case XML_STRING: {
					XmlHttpResult httpResult = XmlHttp.RestSSL(httpMethod, host, path.toString(), headers, httpBody);
					Constructor<? extends Builder<V>> adapterConstructor = adapterClass.getConstructor(new Class[] {XmlHttpResult.class});
					adapter = adapterConstructor.newInstance(httpResult);
					break;
				}
			}
			
			V result = adapter.build();
			
			onExecuteSuccess(context);
			Logger.verbose("AzureStrategy", "end(success)");
			return result;
		}
		catch (Exception e)
		{
			Method buildMethod = adapterClass.getMethod("build");
			@SuppressWarnings("unchecked")
				Class<V> entityClass = (Class<V>) buildMethod.getReturnType();
			Constructor<V> entityConstructor = entityClass.getConstructor(new Class[0]);
			V entity = entityConstructor.newInstance(new Object[0]);
			entity.setException(e);
			onExecuteFailure(context, e);
			Logger.verbose("AzureStrategy", "end(failure)");
			return entity;
		}
	}
	
	public AzureBlob executeGetBlob(AzureStrategyContext context) {
		try
		{
			Logger.verbose("AzureStrategy", context.getOperation().toString() + " begin");
			onBeginExecute(context);
			AzureOperation operation = context.getOperation();
			HttpMethod httpMethod = operation.getHttpMethod();
			String host = getHost(context);
			HttpUri path = getPath(context);
			AzureHttpHeader requestHeaders = getHeaders(context);
			
			HttpResult httpResult = Http.RestSSL(httpMethod, host, path.toString(), requestHeaders, null);
			
			if (context.getRange() != null) {
				String body = httpResult.getBody();
				int paddingIndex = body.indexOf('$');
				if (paddingIndex != -1)
					body = body.substring(0, paddingIndex);
				httpResult = new HttpResult(httpResult.getMethod(), httpResult.getStatusCode(), httpResult.getHeaders(), body);
			}
			
			BlobHeaderAdapter adapter = new BlobHeaderAdapter(httpResult);
			AzureBlob blob = adapter.build();
			onExecuteSuccess(context);
			Logger.verbose("AzureStrategy", "end(success)");
			return blob;
		}
		catch (Exception e)
		{
			AzureBlob response = new AzureBlob(null);
			response.setException(e);
			onExecuteFailure(context, e);
			Logger.verbose("AzureStrategy", "end(failure)");
			return response;
		}
	}
	
	public BlobOperationResponse executeBlobDataOperation(AzureStrategyContext context)
		throws Exception {
		
		try
		{
			Logger.verbose("AzureStrategy", context.getOperation().toString() + " begin");
			onBeginExecute(context);
			AzureOperation operation = context.getOperation();
			HttpMethod httpMethod = operation.getHttpMethod();
			String host = getHost(context);
			HttpUri path = getPath(context);
			AzureHttpHeader headers = getHeaders(context);
			BlobData blobData = context.getBlobData();
			String httpBody = (blobData != null) ? blobData.encode() : null;
			
			if (operation == AzureOperation.PutPage) {
				BlobRange range = context.getRange();
				httpBody = Util.padToLength(httpBody, range.getLength(), '$');
			}
			
			HttpResult httpResult = Http.RestSSL(httpMethod, host, path.toString(), headers, httpBody);
			BlobOperationResponseAdapter adapter = new BlobOperationResponseAdapter(httpResult);
			BlobOperationResponse result = adapter.build();
			onExecuteSuccess(context);
			Logger.verbose("AzureStrategy", "end(success)");
			return result;
		}
		catch (Exception e)
		{
			BlobOperationResponse result = new BlobOperationResponse();
			result.setException(e);
			onExecuteFailure(context, e);
			Logger.verbose("AzureStrategy", "end(failure)");
			return result;
		}
	}
	
	protected AzureStrategy(T token) {
		this.authenticationToken = token;
	}
	
	protected T authenticationToken;
	
	protected AzureHttpHeader getHeaders(AzureStrategyContext context) 
		throws Exception {
		
		AzureOperation operation = context.getOperation();
		HttpMethod httpMethod = operation.getHttpMethod();
		
		AzureHttpHeader headers = new AzureHttpHeader();
		addCommonHeaders(context, headers);

		if (httpMethod.hasContent())
			addContentHeaders(context, headers);
		
		addOperationHeaders(context, headers);
		
		return headers;
	}
	
	protected abstract void addCommonHeaders(AzureStrategyContext context, AzureHttpHeader headers) throws Exception;
	protected abstract void addContentHeaders(AzureStrategyContext context, AzureHttpHeader headers) throws Exception;
	protected abstract void addOperationHeaders(AzureStrategyContext context, AzureHttpHeader headers) throws Exception;
	protected abstract String getHost(AzureStrategyContext context) throws Exception;
	protected abstract HttpUri getPath(AzureStrategyContext context) throws Exception;
	
	protected void onBeginExecute(AzureStrategyContext context) throws Exception {}
	protected void onExecuteFailure(AzureStrategyContext context, Exception e) {}
	protected void onExecuteSuccess(AzureStrategyContext context) {}
}