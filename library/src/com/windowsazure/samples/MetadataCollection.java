package com.windowsazure.samples;

import java.util.Iterator;
import java.util.Vector;

import com.windowsazure.samples.Metadata;


public final class MetadataCollection implements Iterable<Metadata> {

	public MetadataCollection() {
		collection = new Vector<Metadata>();
	}
	
	public void add(String name, String value) {
		add(Metadata.buildValid(name, value));
	}
	
	public void add(Metadata metadata) {
		collection.add(metadata);
	}
	
	public int getCount() {
		return collection.size();
	}
	
	public Metadata getMetadata(String name) {
		for (Metadata item : collection) {
			if (item.getName().equals(name))
				return item;
		}
		return null;
	}
	
	@Override
	public Iterator<Metadata> iterator() {
		return collection.iterator();
	}
	
	private Vector<Metadata> collection;
}
