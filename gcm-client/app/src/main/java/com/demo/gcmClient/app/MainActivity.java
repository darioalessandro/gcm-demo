package com.demo.gcmClient.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String SENDER_ID = "4524831114";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "GCM_DEMO";
    public static final String PROPERTY_REG_ID = "GCM_REGISTRATION_ID";
    public static final String PROPERTY_APP_VERSION = "APP_VERSION";
    private GoogleCloudMessaging gcm;
    private String registrationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkPlayServices()) {
            //continue with the setup
            gcm = GoogleCloudMessaging.getInstance(this);
            registrationId = getRegistrationId(this);
            if(registrationId.isEmpty()) {
                //register in the background
                registerInBackground(this);
            }
        }
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
                    Log.i(TAG,String.format("Device registered with id: %s",registrationId));
                    //send to server
                    storeRegistrationId(registrationId,context);
                }
                catch(IOException err ) {
                    Log.e(TAG,err.getMessage(),err);
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
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
            reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.d("GCM_DEMO",String.format("PlayServices availability check: %d",resultCode));
        if(resultCode!= ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Log.e(TAG,"This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences preferences = getGcmSharedPreferences();
        String registrationId = preferences.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()) {
            Log.i(TAG,"Registration not found");
            return "";
        }
        int registeredVersion = preferences.getInt(PROPERTY_APP_VERSION,Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion!=currentVersion) {
            Log.i(TAG,String.format("Registered app version (%d), doesn't match the current app version (%d",registeredVersion,currentVersion));
            return "";
        }
        return registrationId;
    }

    private void storeRegistrationId(String registrationId, Context context) {
        final SharedPreferences preferences = getGcmSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.putInt(PROPERTY_APP_VERSION, getAppVersion(context));
        editor.commit();
    }


    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        }
        catch(PackageManager.NameNotFoundException nameNotFound) {
            throw new RuntimeException("Could not find package name",nameNotFound);
        }
    }

    private SharedPreferences getGcmSharedPreferences() {
        return getSharedPreferences(MainActivity.class.getSimpleName(), MODE_PRIVATE);
    }

    private void reset() {
        SharedPreferences preferences = getGcmSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.apply();
        Log.i(TAG,"Reset completed");
    }


}
