package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.TBLScheduledManager;
import com.taboola.android.plus.core.TaboolaSdkPlus;
import com.taboola.android.plus.shared.TBLNotificationManager;
import com.taboola.android.utils.Logger;

import java.util.List;

import static notifications.samples.taboola.com.taboolasamplenotificaions.SampleApplication.ACTION_FOR_INIT_FINISH;
import static notifications.samples.taboola.com.taboolasamplenotificaions.SampleApplication.EXTRA_FOR_INIT_FINISH;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView categoriesHeader;
    private Switch switchAllowNotification;
    RecyclerView recyclerViewCategories;

    private AppSettings appSettings;
    private TBLScheduledManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        switchAllowNotification = findViewById(R.id.switch_allow_notifications);
        categoriesHeader = findViewById(R.id.tv_categories_header);
        recyclerViewCategories = findViewById(R.id.rec_view);
        appSettings = AppSettingManager.getAppSettings(this);

        if (savedInstanceState == null) {
            TBLNotificationManager.handleClick(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TBLNotificationManager.handleClick(intent, this);
    }

    /**
     * will be called when init of Scheduled Notification finished successfully.
     */
    private void onScheduledNotificationInitFinished() {
        initNotificationClickHandling();

        //update notifications categorises
        List<String> selectedCategoriesIds = AppSettingManager
                .getSelectedCategoriesIds(appSettings.getCategories());
        notificationManager.setCategories(selectedCategoriesIds);

        //use custom name for notifications, this is optional
        notificationManager.setNotificationAppNameLabel("SdkPlusTest");

        //using this code you can enable or disable notifications
        if (appSettings.isAllowNotifications()) {
            notificationManager.enable();
        } else {
            notificationManager.disable();
        }
        initSettingsLayout();
    }

    //this receiver is used to receive intents fired by SampleApplication,
    // onReceive will be called sdk plus init finished
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlusFeature plusFeature = (PlusFeature) intent.getSerializableExtra(EXTRA_FOR_INIT_FINISH);
            Logger.d(TAG, "PlusFeature FINISH " + plusFeature.name());

            //when getting this action from SampleApplication
            // that's mean that init of ScheduledNotification finished.
            if (plusFeature == PlusFeature.SCHEDULED_NOTIFICATIONS) {
                notificationManager = TaboolaSdkPlus.getScheduledNotificationManager();
                final boolean isInit = notificationManager != null
                        && notificationManager.isInitialized();
                if (isInit) {
                    //init of Scheduled Notification finished successfully
                    onScheduledNotificationInitFinished();
                }
            }
        }
    };

    private void initSettingsLayout() {
        categoriesHeader.setVisibility(View.VISIBLE);

        // init switch
        switchAllowNotification.setEnabled(true);
        switchAllowNotification.setChecked(appSettings.isAllowNotifications());

        switchAllowNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationManager.enable();
            } else {
                notificationManager.disable();
            }

            appSettings.setAllowNotifications(isChecked);
            AppSettingManager.updateAppSettings(this, appSettings);
        });

        // init categories list
        SettingCategoriesAdapter adapter = new SettingCategoriesAdapter(appSettings.getCategories(), categories -> {
            notificationManager.setCategories(AppSettingManager.getSelectedCategoriesIds(categories));

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
}
