package com.demo.gcmClient.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.demo.gcmClient.receivers.GcmBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Intent service for doing whatever processing is necessary when a GCM message comes in.
 * When the GcmBroadcastReceiver receives a message, it will hand off to this intent service to
 * perform whatever processing is necessary.
 *
 */
public class GcmIntentService extends IntentService {
    public static final String SERVICE_NAME = "GCM_INTENT_SERVICE";
    private static final String TAG = SERVICE_NAME;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public GcmIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if(!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Received MESSAGE_TYPE_MESSAGE event: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.i(TAG, "Received MESSAGE_TYPE_SEND_ERROR event: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.i(TAG, "Received MESSAGE_TYPE_DELETED event: " + extras.toString());
            }
            else {
                Log.i(TAG, String.format("Received %s event: %s" ,messageType,  extras.toString()));
            }
        }
        //let the wakeful receiver know we're done and release the wake lock
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
