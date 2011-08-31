package com.windowsazure.samples;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

import com.windowsazure.samples.EdmType;
import com.windowsazure.samples.Property;
import com.windowsazure.samples.internal.util.Base64;
import com.windowsazure.samples.internal.util.Util;

public final class Property<T> {
	
	public static final EdmType DEFAULT_TYPE = EdmType.EdmString;
	public static final String PARTITION_KEY = "PartitionKey";
	public static final String ROW_KEY = "RowKey";
	public static final String TIMESTAMP = "Timestamp";
	public static final String TYPE = "type";
	
	public EdmType getEdmType() {
		return edmType;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public static Property<?> fromRepresentation(String name, EdmType edmType, String representation)
		throws Exception {
		
		switch (edmType) {
		case EdmBinary:
			byte[] bytes = Base64.decode(representation.getBytes());
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			return new Property<ByteBuffer>(name, buffer);
		case EdmBoolean:
			Boolean booleanValue = Boolean.parseBoolean(representation);
			return new Property<Boolean>(name, booleanValue);
		case EdmDateTime:
			Date date = Util.xmlStringToDate(representation);
			return new Property<Date>(name, date);
		case EdmDouble:
			Double doubleValue = Double.parseDouble(representation);
			return new Property<Double>(name, doubleValue);
		case EdmGuid:
			UUID guid = UUID.fromString(representation);
			return new Property<UUID>(name, guid);
		case EdmInt32:
			Integer int32 = Integer.parseInt(representation);
			return new Property<Integer>(name, int32);
		case EdmInt64:
			Long int64 = Long.parseLong(representation);
			return new Property<Long>(name, int64);
		case EdmString:
			return new Property<String>(name, representation);
		}
		
		throw new Exception("Unable to build Property");
	}
	
	public static Property<ByteBuffer> newProperty(String name, ByteBuffer buffer) {
		return new Property<ByteBuffer>(name, buffer);
	}
	
	public static Property<Boolean> newProperty(String name, Boolean booleanValue) {
		return new Property<Boolean>(name, booleanValue);
	}
	
	public static Property<Date> newProperty(String name, Date date) {
		return new Property<Date>(name, date);
	}
	
	public static Property<Double> newProperty(String name, Double doubleValue) {
		return new Property<Double>(name, doubleValue);
	}
	
	public static Property<UUID> newProperty(String name, UUID guid) {
		return new Property<UUID>(name, guid);
	}
	
	public static Property<Integer> newProperty(String name, Integer int32) {
		return new Property<Integer>(name, int32);
	}
	
	public static Property<Long> newProperty(String name, Long int64) {
		return new Property<Long>(name, int64);
	}
	
	public static Property<String> newProperty(String name, String string) {
		return new Property<String>(name, string);
	}
	
	private Property(String name, T value) {
		this.name = name;
		this.value = value;
		
		if (value instanceof ByteBuffer) {
			this.edmType = EdmType.EdmBinary;
			this.representation = new String(Base64.encode(((ByteBuffer) value).array()));
			return;
		}

		if (value instanceof Boolean) {
			this.edmType = EdmType.EdmBoolean;
			this.representation = value.toString();
			return;
		}
		
		if (value instanceof Date) {
			this.edmType = EdmType.EdmDateTime;
			this.representation = Util.dateToXmlStringWithoutTZ((Date) value);
			return;
		}
		
		if (value instanceof Double) {
			this.edmType = EdmType.EdmDouble;
			this.representation = value.toString();
			return;
		}
		
		if (value instanceof UUID) {
			this.edmType = EdmType.EdmGuid;
			this.representation = value.toString();
			return;
		}
		
		if (value instanceof Integer) {
			this.edmType = EdmType.EdmInt32;
			this.representation = value.toString();
			return;
		}
		
		if (value instanceof Long) {
			this.edmType = EdmType.EdmInt64;
			this.representation = value.toString();
			return;
		}
		
		if (value instanceof String) {
			this.edmType = EdmType.EdmString;
			this.representation = (String) value;
			return;
		}
		
		this.edmType = EdmType.EdmUnsupported;
		this.representation = value.toString();
	}
	
	private EdmType edmType;
	private String name;
	private String representation;
	private T value;
}
