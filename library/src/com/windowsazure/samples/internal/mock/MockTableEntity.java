package com.windowsazure.samples.internal.mock;

import java.util.Collection;
import java.util.Date;

import com.windowsazure.samples.Property;

public class MockTableEntity {
	public String authorName;
	public String id;
	public Collection<Property<?>> properties;
	public String title;
	public Date updated;
}
