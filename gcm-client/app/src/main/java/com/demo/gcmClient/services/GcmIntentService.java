package com.demo.gcmClient.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
        Log.i(TAG, "Received GCM event: " + intent.toString());
    }
}
