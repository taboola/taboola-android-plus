package com.taboola.samples.endlessfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.notification.TBNotificationManager;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    public static final String PREFS_NAME = "PREFS_NAME";

    private TBNotificationManager notificationManager;
    private Switch allowNotificationSwitch;

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initTaboolaPlus();
    }

    private void initLayout() {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_settings);
        allowNotificationSwitch = findViewById(R.id.notification_switch);


        allowNotificationSwitch.setEnabled(false);
        allowNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationManager.enable();
                } else {
                    notificationManager.disable();
                }
            }
        });
    }


    private void initTaboolaPlus() {
        // Init Taboola plus and Notification manager. After that set list of chosen categories to it.
        final ArrayList<String> categories = new ArrayList<>();
        TaboolaPlus.init("sdk-tester-demo", "conf1",
                new TaboolaPlus.TaboolaPlusRetrievedCallback() {
                    @Override
                    public void onTaboolaPlusRetrieved(TaboolaPlus taboolaPlus) {
                        notificationManager = taboolaPlus.getNotificationManager();
                        categories.add("general");
                        notificationManager.setCategories(categories);
                        allowNotificationSwitch.setEnabled(true);
                        allowNotificationSwitch.setChecked(notificationManager.isEnabled());

                    }
                }, new TaboolaPlus.TaboolaPlusRetrieveFailedCallback() {
                    @Override
                    public void onTaboolaPlusRetrieveFailed(Throwable throwable) {
                        Log.e(TAG, "TaboolaPlus init failed");
                    }
                });
    }
}
