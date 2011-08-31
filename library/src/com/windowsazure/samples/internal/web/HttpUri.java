package com.windowsazure.samples.internal.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Vector;

import com.windowsazure.samples.internal.util.Pair;
import com.windowsazure.samples.internal.util.Util;

public class HttpUri {
	
	public HttpUri(String path) {
		this.path = path;
		parameters = new Vector<Pair<String, String>>();
		
		int parameterIndex = Util.isStringNullOrEmpty(path) ? -1 : path.indexOf('?');
		if (parameterIndex != -1) {
			String parameterString = path.substring(parameterIndex + 1);
			this.path = path.substring(0, parameterIndex);
			String[] parameters = parameterString.split("&");
			for (String parameter : parameters) {
				String[] parts = parameter.split("=");
				addParameterWithoutEncoding(parts[0].trim(), parts[1].trim());
			}
		}
	}
	
	public void addParameterWithEncoding(String name, String value)
		throws 	UnsupportedEncodingException {
		
		addParameterWithoutEncoding(URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
	}
	
	public void addParameterWithoutEncoding(String name, String value) {
		for (Pair<String, String> item : parameters) {
			if (item.getFirst().equalsIgnoreCase(name)) {
				item.setSecond(value);
				return;
			}
		}
		
		parameters.add(new Pair<String, String>(name, value));
	}
	
	public void appendPath(String subpath) {
		path += subpath;
	}
	
	public String parametersToString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append('?');
		for (Pair<String, String> parameter : parameters) {
			sb.append(parameter.getFirst());
			sb.append('=');
			sb.append(parameter.getSecond());
			sb.append('&');
		}
		sb.deleteCharAt(sb.length() - 1);	// Delete the trailing ampersand.
		return sb.toString();
	}
	
	public Pair<String, String> getParameter(String name) {
		for (Pair<String, String> parameter : parameters) {
			if (parameter.getFirst().contains(name))
				return parameter;
		}
		return null;
	}
	
	public Collection<Pair<String, String>> getParameters() {
		return parameters;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return path + parametersToString();
	}
	
	private Vector<Pair<String, String>> parameters;
	private String path;
}
