package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.app.Application;

import com.taboola.android.plus.TaboolaPlus;

public class SampleApplication extends Application {
    private final String CONFIG = "{\"TaboolaConfig\":{\"ContentManagerConfig\":{\"cacheExpirationTimeMs\":3600000,\"sdkConfig\":{\"apiKey\":\"de8cfcf10cdc8337b1582adc99cfd75559f1fd65\",\"publisher\":\"taboola-reader-app\"},\"placementMap\":{\"main_feed\":{\"categoryToPlacement\":{\"sport\":\"AC-main-sports\",\"news\":\"AC-main-news\",\"business\":\"AC-main-business\",\"tech\":\"AC-main-tech\",\"science\":\"AC-main-science\",\"entertainment\":\"AC-main-entertainment\",\"music\":\"AC-main-music\"}},\"category_feed\":{\"categoryToPlacement\":{\"sport\":\"AC-feed-sports\",\"news\":\"AC-feed-news\",\"business\":\"AC-feed-business\",\"tech\":\"AC-feed-tech\",\"science\":\"AC-feed-science\",\"entertainment\":\"AC-feed-entertainment\",\"music\":\"AC-feed-music\"}},\"notification\":{\"categoryToPlacement\":{\"sport\":\"AC-notification-sports\",\"news\":\"AC-notification-news\",\"business\":\"AC-notification-business\",\"tech\":\"AC-notification-tech\",\"science\":\"AC-notification-science\",\"entertainment\":\"AC-notification-entertainment\",\"music\":\"AC-notification-music\"}}}},\"NotificationsManagerConfig\":{\"itemsBatchSize\":1,\"refreshIntervalMs\":900000,\"switchContentIntervalMs\":180000,\"notificationCategories\":[\"news\",\"entertainment\",\"tech\",\"sport\",\"business\",\"science\",\"music\"]}}}";

    @Override
    public void onCreate() {
        super.onCreate();
        initTaboolaSdkPLus();
    }

    private void initTaboolaSdkPLus() {
        TaboolaPlus.getInstance()
                .init(getApplicationContext(), CONFIG)
                .getNotificationManager()
                .setCategories(AppSettingManager.getSelectedCategories(AppSettingManager.getAppSettings(this).getCategories()));
    }

}
