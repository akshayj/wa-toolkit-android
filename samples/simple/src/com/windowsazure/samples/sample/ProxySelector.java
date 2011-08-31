package com.windowsazure.samples.sample;

import com.windowsazure.samples.authentication.AuthenticationToken;
import com.windowsazure.samples.authentication.AuthenticationTokenFactory;
import com.windowsazure.samples.sample.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class ProxySelector extends Activity
{

	public static String ACCOUNT = "account";
	public static String ACCESS_KEY = "accesskey";
	public static AuthenticationToken credential;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button startButton = (Button)findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {start();}
        	});
    }

    private void start()
	{
        ToggleButton proxySwitch = (ToggleButton)findViewById(R.id.useProxySwitch);
        if (!proxySwitch.isChecked() && (ACCOUNT == "account" || ACCESS_KEY == "accesskey"))
        {
        	this.showDialog(1);
        }
        else
        {
	        if (proxySwitch.isChecked())
	        {
	        	Intent launchProxyLogin = new Intent(this, ProxyLogin.class);
	        	startActivity (launchProxyLogin);
	        }
	        else
	        {
	        	credential = AuthenticationTokenFactory.buildDirectConnectToken(ACCOUNT, ACCESS_KEY);
	        	Intent launchStorageTypeSelector = new Intent(this, StorageTypeSelector.class);
	        	startActivity (launchStorageTypeSelector);
	        }
        }
	}

    protected Dialog onCreateDialog(int id)
    {
    	AlertDialog errorOverlay = new AlertDialog.Builder(this).create();
    	errorOverlay.setTitle("Credentials Error");
    	errorOverlay.setMessage("Default values have not been changed. Please set your account and accessKey");
    	return errorOverlay;
    }

}