package com.demo.gcmClient.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.demo.gcmClient.services.GcmIntentService;

/**
 * Broadcast event receiver for subscribing to GCM messages
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Received broadcast intent: " + intent.toString());
        ComponentName comp =  new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context,intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }
}
