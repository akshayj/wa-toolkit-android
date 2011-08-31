package com.windowsazure.samples.sample;

import com.windowsazure.samples.authentication.AuthenticationTokenFactory;
import com.windowsazure.samples.sample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProxyLogin extends Activity
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proxylogin);
        Button loginButton = (Button)findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener( ) {
        	public void onClick(View view) {login();}
        	});
    }

    private void login()
	{
    	EditText proxyField = (EditText)findViewById(R.id.proxyURLField);
    	EditText usernameField = (EditText)findViewById(R.id.usernameField);
    	EditText passwordField = (EditText)findViewById(R.id.passwordField);
    	try
    	{
			ProxySelector.credential = AuthenticationTokenFactory.buildProxyToken(proxyField.getText().toString(), usernameField.getText().toString(), passwordField.getText().toString());
		}
    	catch (Exception e)
    	{
        	System.out.println(e.toString());
		}
    	Intent launchStorageTypeSelector = new Intent(this, StorageTypeSelector.class);
    	startActivity (launchStorageTypeSelector);
	}
}
