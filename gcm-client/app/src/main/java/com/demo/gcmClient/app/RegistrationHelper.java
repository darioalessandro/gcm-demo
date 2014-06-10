package com.demo.gcmClient.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
* Created by hyleung on 2014-06-10.
*/
class RegistrationHelper {
    public static final String SENDER_ID = "4524831114";
    final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "GCM_DEMO";
    public static final String PROPERTY_REG_ID = "GCM_REGISTRATION_ID";
    public static final String PROPERTY_APP_VERSION = "APP_VERSION";

    static SharedPreferences getGcmSharedPreferences(Context context) {
        return context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }
     static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        }
        catch(PackageManager.NameNotFoundException nameNotFound) {
            throw new RuntimeException("Could not find package name",nameNotFound);
        }
    }
     static void storeRegistrationId(String registrationId, Context context) {
        final SharedPreferences preferences = getGcmSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.putInt(PROPERTY_APP_VERSION, getAppVersion(context));
        editor.commit();
    }
     static String getRegistrationId(Context context) {
        final SharedPreferences preferences = getGcmSharedPreferences(context);
        String registrationId = preferences.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found");
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

     static void reset(Context context) {
        SharedPreferences preferences = getGcmSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
         boolean result = editor.commit();
         Log.i(TAG,String.format("Reset completed: %b",result));
    }

}
