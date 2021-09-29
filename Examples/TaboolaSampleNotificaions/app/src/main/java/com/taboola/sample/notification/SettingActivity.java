package com.taboola.sample.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.TBLScheduledManager;
import com.taboola.android.plus.core.TaboolaSdkPlus;
import com.taboola.android.plus.shared.TBLNotificationManager;
import com.taboola.android.utils.Logger;

import java.util.List;

import static com.taboola.sample.notification.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.sample.notification.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.sample.notification.UtilsHelper.showToast;


public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView categoriesHeader;
    private Switch switchAllowNotification;
    private RecyclerView recyclerViewCategories;

    private AppSettings appSettings;
    private TBLScheduledManager scheduledNotificationManager;

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
    }

    private void initLayout() {
        setContentView(R.layout.activity_setting);
        switchAllowNotification = findViewById(R.id.switch_allow_notifications);
        categoriesHeader = findViewById(R.id.tv_categories_header);
        recyclerViewCategories = findViewById(R.id.rec_view);
        appSettings = AppSettingManager.getAppSettings(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handling notification click
        TBLNotificationManager.handleClick(intent, this);
    }


    private void initSettingsLayout() {
        categoriesHeader.setVisibility(View.VISIBLE);

        // init switch
        switchAllowNotification.setEnabled(true);
        switchAllowNotification.setChecked(appSettings.isAllowNotifications());

        switchAllowNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                scheduledNotificationManager.enable();
            } else {
                scheduledNotificationManager.disable();
            }

            appSettings.setAllowNotifications(isChecked);
            AppSettingManager.updateAppSettings(this, appSettings);
        });

        // init categories list
        SettingCategoriesAdapter adapter = new SettingCategoriesAdapter(appSettings.getCategories(), categories -> {
            scheduledNotificationManager.setCategories(AppSettingManager.getSelectedCategoriesIds(categories));

            appSettings.setCategories(categories);
            AppSettingManager.updateAppSettings(SettingActivity.this, appSettings);
        });
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setAdapter(adapter);
    }

    private void initNotificationClickHandling() {
        TaboolaApi.getInstance().setOnClickListener((placementName, itemId, clickUrl, isOrganic) -> {
            //TODO your click implementation
            return !isOrganic;
        });
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

    /**
     * will be called when init of Scheduled Notification finished successfully.
     */
    private void onScheduledNotificationInitFinished() {
        initNotificationClickHandling();

        //update notifications categorises
        List<String> selectedCategoriesIds = AppSettingManager
                .getSelectedCategoriesIds(appSettings.getCategories());
        scheduledNotificationManager.setCategories(selectedCategoriesIds);

        //use custom name for notifications, this is optional
        scheduledNotificationManager.setNotificationAppNameLabel("SdkPlusTest");

        //using this code you can enable or disable notifications
        if (appSettings.isAllowNotifications()) {
            scheduledNotificationManager.enable();
            showToast(this, "Notification enabled");
        } else {
            scheduledNotificationManager.disable();
            showToast(this, "Notification disabled");
        }
        initSettingsLayout();
    }
}
