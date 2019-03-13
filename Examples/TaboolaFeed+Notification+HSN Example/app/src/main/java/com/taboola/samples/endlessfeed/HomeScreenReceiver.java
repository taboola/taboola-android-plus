package com.taboola.samples.endlessfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;


public class HomeScreenReceiver extends BroadcastReceiver {

    public static final String HOME_SCREEN_KEY = "home_screen_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        // check if should open custom url by HSN
        if (intent.hasExtra(TBHomeScreenNewsManager.HOME_SCREEN_URL_TO_OPEN)) {
            //launch your screen with url to open
            MainActivity.launchWithUrl(context, HOME_SCREEN_KEY,
                    intent.getStringExtra(TBHomeScreenNewsManager.HOME_SCREEN_URL_TO_OPEN));
        } else {
            //launch your screen
            MainActivity.launch(context, HOME_SCREEN_KEY);
        }
    }
}
