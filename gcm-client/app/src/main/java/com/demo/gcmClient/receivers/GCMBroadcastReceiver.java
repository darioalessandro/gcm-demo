package com.demo.gcmClient.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Broadcast event receiver for subscribing to GCM messages
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Received GCM event");
    }
}
