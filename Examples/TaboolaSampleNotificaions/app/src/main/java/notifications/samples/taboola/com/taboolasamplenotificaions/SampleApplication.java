package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.app.Application;
import android.util.Log;

import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.notification.TBNotificationManager;

import java.util.List;

public class SampleApplication extends Application {
    private static final String TAG = SampleApplication.class.getSimpleName();

    private AppSettings appSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        appSettings = AppSettingManager.getAppSettings(this);
        initTaboolaSdkPLus();
    }

    private void initTaboolaSdkPLus() {
        TaboolaPlus.init(getApplicationContext(), "taboola-reader-app", "conf1");
        TaboolaPlus.getInitializedInstance(taboolaPlus -> {
                    TBNotificationManager notificationManager = taboolaPlus.getNotificationManager();
                    List<String> selectedCategoriesIds = AppSettingManager
                            .getSelectedCategoriesIds(appSettings.getCategories());

                    notificationManager.setCategories(selectedCategoriesIds);
                    if (appSettings.isAllowNotifications()) {
                        notificationManager.enable();
                    } else {
                        notificationManager.disable();
                    }
                },
                throwable -> Log.e(TAG, "initTaboolaSdkPLus: " + throwable.getMessage(), throwable));

        TaboolaApi.getInstance().setOnClickListener((placementName, itemId, clickUrl, isOrganic) -> {
            if (isOrganic) {
                //TODO your click implementation
                return false;
            } else {
                return true;
            }
        });
    }

}
