package com.demo.gcmClient.app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import static com.demo.gcmClient.app.RegistrationHelper.*;


public class RegistrationActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleCloudMessaging gcm;
    private String registrationId;
    private Button submitButton;
    private EditText sessionIdField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        sessionIdField = (EditText)findViewById(R.id.sessionId_field);
        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerInBackground(final Context context) {
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if(gcm == null) {
                        //get a GCM instance
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    //register with GCM
                    registrationId = gcm.register(SENDER_ID);
                    Log.i(TAG, String.format("Device registered with id: %s", registrationId));
                    //send to server
                    storeRegistrationId(registrationId,context);
                }
                catch(IOException err ) {
                    Log.e(TAG,err.getMessage(),err);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                //complete
                finish();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        registerInBackground(this);
    }
}
