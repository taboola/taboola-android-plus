package com.example.homescreenexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;

public class HomeScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            //launch your HSN activity
            HomeScreenActivity.launch(context, intent.getExtras());
    }
}
