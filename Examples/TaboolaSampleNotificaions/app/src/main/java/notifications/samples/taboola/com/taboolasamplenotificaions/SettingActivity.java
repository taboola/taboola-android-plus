package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;

import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBRecommendationItem;
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
        handleClickIntentIfNeeded(getIntent());
    }

    private void initOnSwitchCheckedChangesListener() {
        switchAllowNotification.setChecked(appSettings.isAllowNotifications());
        switchAllowNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                TaboolaPlus.getInstance()
                        .getNotificationManager()
                        .enable();
            } else {
                TaboolaPlus.getInstance()
                        .getNotificationManager()
                        .disable();
            }
            appSettings.setAllowNotifications(isChecked);
            AppSettingManager.updateAppSettings(this, appSettings);
        });
    }

    private void initRecView() {
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingCategoriesAdapter(appSettings.getCategories(), categories -> {
            TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .setCategories(AppSettingManager.getSelectedCategories(categories));

            appSettings.setCategories(categories);
            AppSettingManager.updateAppSettings(SettingActivity.this, appSettings);
        });
        recyclerViewCategories.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleClickIntentIfNeeded(intent);
    }

    private void handleClickIntentIfNeeded(Intent intent) {
        Bundle extras = intent.getExtras();
        if ((intent.getAction() != null) &&
                intent.getAction().equals(TBNotificationManager.NOTIFICATION_CLICK_INTENT_ACTION) &&
                (extras != null)) {
            TBPlacement placement = extras.getParcelable(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_PLACEMENT);
            int itemIndex = extras.getInt(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_ITEM_INDEX);

            TBRecommendationItem item = placement.getItems().get(itemIndex);
            item.handleClick(this);
        }
    }
}
