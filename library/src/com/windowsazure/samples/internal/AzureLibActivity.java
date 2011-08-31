package com.windowsazure.samples.internal;

import com.microsoft.cloud.android.R;

import android.app.Activity;
import android.os.Bundle;

public class AzureLibActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}