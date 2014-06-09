package com.demo.gcmClient.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Intent service for doing whatever processing is necessary when a GCM message comes in.
 * When the GcmBroadcastReceiver receives a message, it will hand off to this intent service to
 * perform whatever processing is necessary.
 *
 */
public class GcmIntentService extends IntentService {
    public static final String SERVICE_NAME = "GCM_INTENT_SERVICE";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public GcmIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
