package com.taboola.sample.notification.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.TBLPushManager;
import com.taboola.android.plus.core.TBLScheduledManager;
import com.taboola.android.plus.core.TaboolaSdkPlus;
import com.taboola.android.plus.shared.TBLNotificationManager;
import com.taboola.android.utils.Logger;

import java.util.Collections;

import static com.taboola.sample.notification.push.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.sample.notification.push.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.sample.notification.push.UtilsHelper.showToast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CAT_GENERAL = "general";

    private TBLScheduledManager scheduledNotificationManager;
    private TBLPushManager pushNotificationManager;

    private FloatingActionButton allowNotificationSwitch;


    /**
     * this receiver is used to receive intents fired by SampleApplication,
     * onReceive will be called sdk plus init finished
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && TextUtils.equals(ACTION_FOR_INIT_FINISH, intent.getAction())) {
                PlusFeature plusFeature = (PlusFeature) intent.getSerializableExtra(EXTRA_FOR_INIT_FINISH);
                Logger.d(TAG, "PlusFeature FINISH " + plusFeature.name());

                //when getting this action from SampleApplication
                // that's mean that init of ScheduledNotification finished.
                if (plusFeature == PlusFeature.SCHEDULED_NOTIFICATIONS) {
                    scheduledNotificationManager = TaboolaSdkPlus.getScheduledNotificationManager();
                    if (scheduledNotificationManager != null) {
                        //init of Scheduled Notification finished successfully
                        onScheduledNotificationInitFinished();
                    }
                }
                //when getting this action from SampleApplication
                // that's mean that init of PushNotification finished.
                else if (plusFeature == PlusFeature.PUSH_NOTIFICATIONS) {
                    pushNotificationManager = TaboolaSdkPlus.getPushNotificationManager();
                    if (pushNotificationManager != null) {
                        //init of Push Notification finished successfully
                        onPushNotificationInitFinished();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Avoid handling click again when activity is recreated
            TBLNotificationManager.handleClick(getIntent(), this);
        }


        initLayout();

        scheduledNotificationManager = TaboolaSdkPlus.getScheduledNotificationManager();
        //if manager is null mean the init still in progress and you should wait for onReceive in the receiver above
        if (scheduledNotificationManager != null) {
            //ScheduledNotification is already initialized
            onScheduledNotificationInitFinished();
        }

        pushNotificationManager = TaboolaSdkPlus.getPushNotificationManager();
        if (pushNotificationManager != null) {
            //PushNotification is already initialized
            onPushNotificationInitFinished();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handling notification click
        TBLNotificationManager.handleClick(intent, this);
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
        allowNotificationSwitch = findViewById(R.id.allow_notification_switch);
        allowNotificationSwitch.setVisibility(View.GONE);

        allowNotificationSwitch.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                //enable Scheduled Notification
                scheduledNotificationManager.enable();
                showToast(this, "Notification enabled");
            } else {
                //disable Scheduled Notification
                scheduledNotificationManager.disable();
                showToast(this, "Notification disabled");
            }
        });

        //adding SDK feed fragment
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ScrollViewFeedFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_FOR_INIT_FINISH));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private void onPushNotificationInitFinished() {
        //optional code, change push notifications app name label
        pushNotificationManager.setNotificationAppNameLabel("Breaking News");
    }


    private void onScheduledNotificationInitFinished() {

         /* in order to start displaying notifications,
         notifications must be enabled via the TBLScheduledManager#enable()
         and the Taboola News categories must be set
         via the TBLScheduledManager#setCategories() method */

        /* As a Taboola News publisher, you can choose to allow your user to customize
         the recommendations displayed on the notification by choosing content categories
         (News, Sports, Tech, etc.)
          In order to show a mix of all content categories, set the "general" category. */
        scheduledNotificationManager.setCategories(Collections.singletonList(CAT_GENERAL));

        //display switch button to disable or enable the scheduled notifications
        allowNotificationSwitch.setVisibility(View.VISIBLE);
        allowNotificationSwitch.setSelected(scheduledNotificationManager.isEnabled());
    }

}
