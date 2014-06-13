package com.demo.gcmClient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends ActionBarActivity {


    private GoogleCloudMessaging gcm;
    private String registrationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkPlayServices()) {
            //continue with the setup
            gcm = GoogleCloudMessaging.getInstance(this);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(checkPlayServices()) {
            registrationId = RegistrationHelper.getRegistrationId(this);
            if(registrationId.isEmpty()) {
                //present the registration screen
                Intent intent = new Intent(this,RegistrationActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_reset) {
            RegistrationHelper.reset(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.d("GCM_DEMO",String.format("PlayServices availability check: %d",resultCode));
        if(resultCode!= ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this, RegistrationHelper.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Log.e(RegistrationHelper.TAG,"This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }


}
