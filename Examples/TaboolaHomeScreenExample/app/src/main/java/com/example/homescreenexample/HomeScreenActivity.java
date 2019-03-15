package com.example.homescreenexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;

import static com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager.HOME_SCREEN_PLACEMENT_TO_OPEN;
import static com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager.HOME_SCREEN_URL_TO_OPEN;

public class HomeScreenActivity extends AppCompatActivity {

    public static void launch(Context context, Bundle extras) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);
        handleHsnIntentExtras(getIntent().getExtras());

        // If Home Screen News opened successfully send "HomeScreenDisplayed"
        TBHomeScreenNewsManager.getInstance().reportHomeScreenOpened();
    }

    void handleHsnIntentExtras(Bundle extras) {
        String urlToOpen = extras.getString(HOME_SCREEN_URL_TO_OPEN);
        String placementToOpen = extras.getString(HOME_SCREEN_PLACEMENT_TO_OPEN);
        if (TextUtils.isEmpty(urlToOpen) && TextUtils.isEmpty(placementToOpen)) {
            // just open your HSN activity
            return;
        }

        if (!TextUtils.isEmpty(urlToOpen)){
            // TODO handle url opening your way
            UrlOpenUtil.openUrlInTabsOrBrowser(getApplicationContext(), urlToOpen);
        }

        if (!TextUtils.isEmpty(placementToOpen)){
            // TODO IMPORTANT: if placement isn't empty you must use it on HSN for correct reporting
        }
    }
}
