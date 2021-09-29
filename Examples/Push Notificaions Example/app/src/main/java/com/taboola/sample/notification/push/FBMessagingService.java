package com.taboola.sample.notification.push;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taboola.android.plus.core.TBLPushManager;

/**
 * Base class for receiving messages from Firebase Cloud Messaging.
 * Extending this class is required to be able to handle downstream messages
 * . It also provides functionality to automatically display notifications,
 * and has methods that are invoked to give the status of upstream messages.
 * <p>
 * Override base class methods to handle any events required by the application.
 * All methods are invoked on a background thread,
 * and may be called when the app is in the background or not open.
 * <p>
 * for more info please read
 * https://firebase.google.com/docs/reference/android/com/google/firebase/messaging/FirebaseMessagingService
 */
public class FBMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FBMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the FCM RemoteMessage is not empty and sent by Taboola,
        boolean isTaboolaMessage = TBLPushManager.isTaboolaMessage(remoteMessage);
        if (isTaboolaMessage) {
            // pass the remoteMessage to TBLPushManager so the sdk+ can handle it.
            TBLPushManager.handleTaboolaPush(remoteMessage);
        } else {
            // this is not Taboola push, and you should handle it
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "onNewToken() called");
        super.onNewToken(token);
        //this method must be called so every time onNewToken called, the push manger will update current user token.
        TBLPushManager.onNewFirebaseToken(token);
    }
}
