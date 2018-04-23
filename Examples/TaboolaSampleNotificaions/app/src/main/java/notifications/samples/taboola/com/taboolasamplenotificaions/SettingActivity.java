package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;

import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.notification.TBNotificationManager;

public class SettingActivity extends AppCompatActivity {

    Switch switchAllowNotification;
    RecyclerView recyclerViewCategories;

    private SettingCategoriesAdapter adapter;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        switchAllowNotification = findViewById(R.id.switch_allow_notifications);
        recyclerViewCategories = findViewById(R.id.rec_view);

        appSettings = AppSettingManager.getAppSettings(this);

        initOnSwitchCheckedChangesListener();
        initRecView();

        if (savedInstanceState == null) {
            TBNotificationManager.handleClick(getIntent(), this);
        }
    }

    private void initOnSwitchCheckedChangesListener() {
        switchAllowNotification.setChecked(appSettings.isAllowNotifications());

        switchAllowNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            TaboolaPlus.getInitializedInstance(taboolaPlus -> {
                if (isChecked) {
                    taboolaPlus.getNotificationManager().enable();
                } else {
                    taboolaPlus.getNotificationManager().disable();
                }
            });

            appSettings.setAllowNotifications(isChecked);
            AppSettingManager.updateAppSettings(this, appSettings);
        });
    }

    private void initRecView() {
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SettingCategoriesAdapter(appSettings.getCategories(), categories -> {
            TaboolaPlus.getInitializedInstance(taboolaPlus -> taboolaPlus.getNotificationManager()
                    .setCategories(AppSettingManager.getSelectedCategoriesIds(categories)));

            appSettings.setCategories(categories);
            AppSettingManager.updateAppSettings(SettingActivity.this, appSettings);
        });
        recyclerViewCategories.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TBNotificationManager.handleClick(intent, this);
    }
}
