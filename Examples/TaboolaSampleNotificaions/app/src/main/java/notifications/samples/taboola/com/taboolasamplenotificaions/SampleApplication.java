package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.app.Application;
import android.util.Log;

import com.taboola.android.plus.TaboolaPlus;

public class SampleApplication extends Application {
    private static final String TAG = SampleApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initTaboolaSdkPLus();
    }

    private void initTaboolaSdkPLus() {
        TaboolaPlus.init(getApplicationContext(), "taboola-reader-app", "conf1");
        TaboolaPlus.getInitializedInstance(
                taboolaPlus -> taboolaPlus.getNotificationManager()
                        .setCategories(AppSettingManager.getSelectedCategories(AppSettingManager.getAppSettings(SampleApplication.this).getCategories()))
                        .enable(),
                throwable -> Log.e(TAG, "initTaboolaSdkPLus: " + throwable.getMessage(), throwable));
    }

}
