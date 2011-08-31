package com.windowsazure.samples;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.Property;


public final class PropertyCollection implements Iterable<Property<?>> {

	public PropertyCollection() {
		properties = new Vector<Property<?>>();
	}
	
	public PropertyCollection(String partitionKey, String rowKey) {
		this(partitionKey, rowKey, new Date());
	}
	
	public PropertyCollection(String partitionKey, String rowKey, Date timestamp) {
		properties = new Vector<Property<?>>();
		setPartitionKey(partitionKey);
		setRowKey(rowKey);
		setTimestamp(timestamp);
	}
	
	public void add(Property<?> newProperty) {
		Property<?> property = getProperty(newProperty.getName());
		if (property != null)
			properties.remove(property);
		properties.add(newProperty);
	}
	
	public int getCount() {
		return properties.size();
	}
	
	public String getPartitionKey() {
		return this.<String>getProperty(Property.PARTITION_KEY).getValue();
	}
	
	public Property<?> getProperty(int index) {
		return properties.get(index);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Property<T> getProperty(String name) {
		for (Property<?> property : properties) {
			if (property.getName().equals(name)) {
				return (Property<T>) property;
			}
		}
		return null;
	}
	
	public String getRowKey() {
		return this.<String>getProperty(Property.ROW_KEY).getValue();
	}
	
	public Date getTimestamp() {
		return this.<Date>getProperty(Property.TIMESTAMP).getValue();
	}
	
	@Override
	public Iterator<Property<?>> iterator() {
		return properties.iterator();
	}
	
	public void setPartitionKey(String partitionKey) {
		add(Property.newProperty(Property.PARTITION_KEY, partitionKey));
	}
	
	public void setRowKey(String rowKey) {
		add(Property.newProperty(Property.ROW_KEY, rowKey));
	}
	
	public void setTimestamp(Date date) {
		add(Property.newProperty(Property.TIMESTAMP, date));
	}
	
	private Vector<Property<?>> properties;
}
