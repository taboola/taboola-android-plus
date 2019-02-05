package com.taboola.samples.endlessfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class HomeScreenReceiver extends BroadcastReceiver {

    public static final String HOME_SCREEN_KEY = "home_screen_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        //launch your screen
        MainActivity.launch(context, HOME_SCREEN_KEY);
    }
}
