package com.taboola.sample.notification.push;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

class UtilsHelper {

    static final String ACTION_FOR_INIT_FINISH = "com.example.android.plus.PlusFeature";
    static final String EXTRA_FOR_INIT_FINISH = "PlusFeature";

    private final static Handler mainHandler = new Handler(Looper.getMainLooper());


    // Helper for showing toast messages on the screen
    static void showToast(final Context context, final CharSequence text) {
        mainHandler.post(() -> Toast.makeText(context, text, Toast.LENGTH_SHORT).show());
    }
}
