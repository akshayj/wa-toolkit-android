package com.windowsazure.samples.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.windowsazure.samples.Property;
import com.windowsazure.samples.PropertyCollection;
import com.windowsazure.samples.internal.table.IfMatch;
import com.windowsazure.samples.queue.AzureQueueMessage;
import com.windowsazure.samples.queue.AzureQueueMessageCollection;
import com.windowsazure.samples.table.AzureTableEntity;
import com.windowsazure.samples.table.AzureTableEntityCollection;
import com.windowsazure.samples.queue.AzureQueueManager;
import com.windowsazure.samples.table.AzureTableManager;
import com.windowsazure.samples.sample.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TwoLineListItem;

public class ModifyItemDisplay extends Activity implements OnItemClickListener, OnEditorActionListener, OnKeyListener
{

	static int MODIFY_ITEM_MODE_ADD = 1;
	static int MODIFY_ITEM_MODE_UPDATE = 2;
	static int MODIFY_ITEM_MODE_DELETE = 3;
	static int MODIFY_ITEM_SUBTYPE_CONTAINER = 1;
	static int MODIFY_ITEM_SUBTYPE_BLOB = 2;

	int mode;
	int listType;
	int selection;
	String listName;
	List<Map<String, String>> entry = new ArrayList<Map<String, String>>();
	EditText editField;
	String editLabel;
	String enteredValue;
	TwoLineListItem editedRow;
	int editedRowNum;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyitemdisplay);
        Button addUpdateButton = (Button)findViewById(R.id.itemAddUpdateButton);
        Button deleteButton = (Button)findViewById(R.id.itemDeleteButton);
        ListView listView = (ListView)findViewById(R.id.itemFieldListView);
        listView.setOnItemClickListener(this);
		addUpdateButton.setVisibility(View.VISIBLE);
		deleteButton.setVisibility(View.VISIBLE);
        Bundle optionSet = getIntent().getExtras();
        mode = optionSet.getInt("com.windowsazure.samples.sample.modifyitemdisplay.mode");
        listType = optionSet.getInt("com.windowsazure.samples.sample.modifyitemdisplay.type");
        selection = optionSet.getInt("com.windowsazure.samples.sample.modifyitemdisplay.selection");
        listName = optionSet.getString("com.windowsazure.samples.sample.modifyitemdisplay.listName");
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
        	if (mode == MODIFY_ITEM_MODE_ADD)
        	{
        		this.setTitle("Add Entity");
        		addUpdateButton.setText("Add");
        		deleteButton.setVisibility(View.INVISIBLE);
        		selection = 0;
        	}
        	else if (mode == MODIFY_ITEM_MODE_UPDATE)
        	{
        		this.setTitle("Update Entity");
        		addUpdateButton.setText("Update");
        	}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
        	if (mode == MODIFY_ITEM_MODE_ADD)
        	{
        		this.setTitle("Add Message");
        		addUpdateButton.setText("Add");
        		deleteButton.setVisibility(View.INVISIBLE);
        		selection = 0;
        	}
        	else if (mode == MODIFY_ITEM_MODE_UPDATE)
        	{
        		this.setTitle("Delete Message");
        		addUpdateButton.setVisibility(View.INVISIBLE);
        	}
        }
        addUpdateButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {updateItem();}
        	});
        deleteButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {deleteItem();}
        	});
    }

	public void onStart()
	{
		super.onStart();
        try
        {
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
        		AzureTableEntityCollection entities = new AzureTableManager(ProxySelector.credential).queryAllEntities(listName);
	        	Collection<AzureTableEntity> entityList = entities.getEntities();
	        	AzureTableEntity entity = (AzureTableEntity) entityList.toArray()[selection];
    			PropertyCollection properties = entity.getProperties();
        		Iterator<Property<?>> propertyIterator = properties.iterator();
        		while (propertyIterator.hasNext())
	       		{
        			Property<?> property = propertyIterator.next();
	    			Map<String, String> lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", property.getName());
		        	if (mode == MODIFY_ITEM_MODE_UPDATE)
		        	{
		        		lineItem.put("Value", (String)property.getValue().toString());
		        	}
		        	else
		        	{
		        		lineItem.put("Value", "");
		        	}
	       			entry.add(lineItem);
	       		}
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
				AzureQueueMessageCollection messages = new AzureQueueManager(ProxySelector.credential).peekMessages(listName, 32);
		        if (mode == MODIFY_ITEM_MODE_UPDATE)
		        {
		        	AzureQueueMessage message = (AzureQueueMessage) messages.getMessages().toArray()[selection];
	    			Map<String, String> lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Message ID");
		       		lineItem.put("Value", message.getMessageId());
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Insetion Time");
		       		if (message.getInsertionTime() != null)
		       		{
		       			lineItem.put("Value", message.getInsertionTime().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Expiration Time");
		       		if (message.getExpirationTime() != null)
		       		{
		       			lineItem.put("Value", message.getExpirationTime().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Pop Receipt");
		       		lineItem.put("Value", message.getPopReceipt());
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Time Next Visible");
		       		if (message.getTimeNextVisible() != null)
		       		{
		       			lineItem.put("Value", message.getTimeNextVisible().toString());
		       		}
	       			entry.add(lineItem);
	    			lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Message");
		       		lineItem.put("Value", message.getMessageText());
	       			entry.add(lineItem);
		        }
		        else
		        {
	    			Map<String, String> lineItem = new HashMap<String, String>();
		       		lineItem.put("Name", "Message Text");
		        	lineItem.put("Value", "");
	       			entry.add(lineItem);
		        }
	        }
	    	ListView table = (ListView)findViewById(R.id.itemFieldListView);
	    	SimpleAdapter adapter = new SimpleAdapter(this, entry, android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[]{ android.R.id.text1, android.R.id.text2 });
        	table.setAdapter(adapter);
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		String editLabelValue = "";
		editedRowNum = arg2;
		if (editedRow != null)
		{
			editedRow.removeView(editField);
			editField = null;
		}
    	editedRow = (TwoLineListItem)arg1;
    	editField = new EditText(this);
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
        	Map<String, String> rowData = entry.get(arg2);
        	editLabel = (String) rowData.get("Name") + ": ";
        	editLabelValue = rowData.get("Value");
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
        	editLabel = "Message Text: ";
        }
    	editField.setText(editLabel + editLabelValue);
    	editField.setOnEditorActionListener(this);
    	editField.setOnKeyListener(this);
    	editField.requestFocus();
    	editedRow.addView(editField);
    }

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
	{
		EditText textfield = (EditText)arg0;
		if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER && arg2.getAction() == KeyEvent.ACTION_DOWN)
		{
			Map<String, String> originalLineItem = entry.get(editedRowNum);
			Map<String, String> lineItem = new HashMap<String, String>();
			enteredValue = textfield.getText().toString().replaceFirst(editLabel.toString(), "").trim();
			editedRow.removeView(arg0);
			editedRow = null;
	        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
	        {
	        	lineItem.put("Name", originalLineItem.get("Name"));
	        }
	        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
	        {
	        	lineItem.put("Name", "Message Text");
	        }
        	lineItem.put("Value", enteredValue);
	        entry.set(editedRowNum, lineItem);
	    	ListView table = (ListView)findViewById(R.id.itemFieldListView);
	    	SimpleAdapter adapter = new SimpleAdapter(this, entry, android.R.layout.simple_list_item_2, new String[] {"Name", "Value"}, new int[]{ android.R.id.text1, android.R.id.text2 });
        	table.setAdapter(adapter);
		}
		return (false);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		EditText textfield = (EditText)v;
		// Don't let the user backspace over the label
		if (keyCode == KeyEvent.KEYCODE_DEL)
		{
			if (editLabel.toString().equalsIgnoreCase(textfield.getText().toString()))
			{
				return (true);
			}
		}
		return false;
	}

	private void updateItem()
	{
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
    		try
    		{
    			AzureTableManager tableWriter = new AzureTableManager(ProxySelector.credential);
    			if (mode == MODIFY_ITEM_MODE_ADD)
    			{
        			String partitionKey = entry.get(0).get("Value");
        			String rowKey = entry.get(1).get("Value");
        			PropertyCollection newProperties = new PropertyCollection(partitionKey, rowKey, new Date());
        			for (int propertyIndex=2;propertyIndex<entry.size();propertyIndex++)
        			{
		    			String propertyName = entry.get(propertyIndex).get("Name");
		    			String propertyValue = entry.get(propertyIndex).get("Value");
        				newProperties.add(Property.newProperty(propertyName, propertyValue));
        			}
        			tableWriter.insertEntity(null, new Date(), "Sample App", listName, newProperties);
        		}
    			else if (mode == MODIFY_ITEM_MODE_UPDATE)
    			{
					AzureTableEntityCollection entities = new AzureTableManager(ProxySelector.credential).queryAllEntities(listName);
		        	Collection<AzureTableEntity> entityList = entities.getEntities();
		        	AzureTableEntity entity = (AzureTableEntity) entityList.toArray()[selection];
        			PropertyCollection newProperties = new PropertyCollection(entity.getPartitionKey(), entity.getRowKey(), new Date());
        			for (int propertyIndex=0;propertyIndex<entry.size();propertyIndex++)
        			{
		    			Map<String, String> lineItem = entry.get(propertyIndex);
        				newProperties.add(Property.newProperty(lineItem.get("Name"), lineItem.get("Value")));
        			}
		        	tableWriter.updateEntity(entity.getTitle(), new Date(), "Sample App", entity.getTableName(), newProperties, IfMatch.WILD);
        		}
        	}
        	catch (Exception e)
        	{
               	System.out.println(e.toString());
    		}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
        	if (mode == MODIFY_ITEM_MODE_ADD)
        	{
        		try
        		{
        			AzureQueueManager queueWriter = new AzureQueueManager(ProxySelector.credential);
	    			Map<String, String> lineItem = entry.get(0);
        			queueWriter.putMessage(listName, lineItem.get("Value").toString(), 0);
        		}
        		catch (Exception e)
        		{
                	System.out.println(e.toString());
    			}
        	}
        }
        finish();
	}

    private void deleteItem()
	{
        if (listType == StorageTypeSelector.STORAGE_TYPE_TABLE)
        {
    		try
    		{
	        	AzureTableManager tableReaderWriter = new AzureTableManager(ProxySelector.credential);
				AzureTableEntityCollection entities = tableReaderWriter.queryAllEntities(listName);
	        	Collection<AzureTableEntity> entityList = entities.getEntities();
	        	AzureTableEntity entity = (AzureTableEntity) entityList.toArray()[selection];
	        	tableReaderWriter.deleteEntity(listName, entity.getPartitionKey(), entity.getRowKey(), IfMatch.WILD);
			}
    		catch (Exception e)
    		{
            	System.out.println(e.toString());
			}
        }
        else if (listType == StorageTypeSelector.STORAGE_TYPE_QUEUE)
        {
    		try
    		{
	        	AzureQueueManager queueReaderWriter = new AzureQueueManager(ProxySelector.credential);
				AzureQueueMessageCollection messages = queueReaderWriter.getMessages(listName, 32, null);
	        	AzureQueueMessage message = (AzureQueueMessage) messages.getMessages().toArray()[selection];
	        	queueReaderWriter.deleteMessage(listName, message.getMessageId(), message.getPopReceipt());
			}
    		catch (Exception e)
    		{
            	System.out.println(e.toString());
			}
        }
        finish();
	}

}
