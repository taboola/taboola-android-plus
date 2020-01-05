package com.example.homescreenexample;

import android.app.Application;

import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;

public class HomeScreenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Taboola plus initialization
        TaboolaPlus.init("sdk-tester-demo", "conf1",
                new TaboolaPlus.TaboolaPlusRetrievedCallback() {
                    @Override
                    public void onTaboolaPlusRetrieved(TaboolaPlus taboolaPlus) {
                    }
                });

//          Init TBHomeScreenNewsManager with applicationContext as soon as possible
        TBHomeScreenNewsManager.getInstance().init();
    }
}
