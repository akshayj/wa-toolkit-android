package com.windowsazure.samples.internal.mock;

import java.util.Date;
import java.util.Vector;

public class MockTable {
	public String authorName;
	public String id;
	public String tableName;
	public String title;
	public Date updated;
	
	Vector<MockTableEntity> entities = new Vector<MockTableEntity>();
}
