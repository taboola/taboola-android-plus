package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.notification.TBNotificationManager;

import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView categoriesHeader;
    private Switch switchAllowNotification;
    RecyclerView recyclerViewCategories;

    private AppSettings appSettings;
    TBNotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        switchAllowNotification = findViewById(R.id.switch_allow_notifications);
        categoriesHeader = findViewById(R.id.tv_categories_header);
        recyclerViewCategories = findViewById(R.id.rec_view);

        appSettings = AppSettingManager.getAppSettings(this);

        initTaboolaSdkPlus();
        initNotificationClickHandling();
        
        if (savedInstanceState == null) {
            TBNotificationManager.handleClick(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TBNotificationManager.handleClick(intent, this);
    }

    private void initNotificationClickHandling() {
        TaboolaApi.getInstance().setOnClickListener((placementName, itemId, clickUrl, isOrganic) -> {
            if (isOrganic) {
                //TODO your click implementation
                return false;
            } else {
                return true;
            }
        });
    }

    private void initTaboolaSdkPlus() {
        TaboolaPlus.init("taboola-reader-app", "conf1",
                taboolaPlus -> {
                    Log.d(TAG, "onTaboolaPlusRetrieved()");
                    notificationManager = taboolaPlus.getNotificationManager();

                    setLastUsedSettings();

                    initSettingsLayout();
                },
                throwable -> {
                    Log.e(TAG, "initTaboolaSdkPLus: " + throwable.getMessage(), throwable);
                    // todo handle init fail
                });
    }

    private void setLastUsedSettings() {
        List<String> selectedCategoriesIds = AppSettingManager
                .getSelectedCategoriesIds(appSettings.getCategories());
        notificationManager.setCategories(selectedCategoriesIds);
        if (appSettings.isAllowNotifications()) {
            notificationManager.enable();
        } else {
            notificationManager.disable();
        }
    }

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
}
