package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.SdkPlusInitCallback;
import com.taboola.android.plus.core.SdkPlusPublisherInfo;
import com.taboola.android.plus.core.TaboolaSdkPlus;
import com.taboola.android.utils.Logger;

import static com.taboola.android.plus.core.PlusFeature.SCHEDULED_NOTIFICATIONS;

public class SampleApplication extends Application {

    private static final String TAG = "SampleApplication";
    static final String ACTION_FOR_INIT_FINISH = "com.example.android.plus.PlusFeature";
    static final String EXTRA_FOR_INIT_FINISH = "PlusFeature";

    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setLogLevel(Logger.DEBUG);
        initSdkPlus(this);
    }

    private void initSdkPlus(Context appContext) {
        SdkPlusPublisherInfo sdkPlusPublisherInfo = new SdkPlusPublisherInfo();
        sdkPlusPublisherInfo.setConfigId("conf1");
        sdkPlusPublisherInfo.setPublisherName("sdk-tester-plus");
        sdkPlusPublisherInfo.setActiveFeatures(SCHEDULED_NOTIFICATIONS);
        SdkPlusInitCallback sdkPlusInitCallback = new SdkPlusInitCallback() {
            @Override
            public void onFeatureInitSuccessful(TaboolaSdkPlus taboolaSdkPlus, PlusFeature plusFeature) {
                showToast(appContext, plusFeature.name() + " initialized");
                if (plusFeature == SCHEDULED_NOTIFICATIONS) {
                    //notify sdk plus init is done
                    Intent intent = new Intent(ACTION_FOR_INIT_FINISH);
                    intent.putExtra(EXTRA_FOR_INIT_FINISH, SCHEDULED_NOTIFICATIONS);
                    appContext.sendBroadcast(intent);
                }
            }

            @Override
            public void onFeatureInitFailed(PlusFeature plusFeature, Throwable throwable) {
                final String text = plusFeature.name() + " init failed";
                showToast(appContext, text);
                // TODO: handle int failure in your code
            }
        };

        TaboolaSdkPlus.init(sdkPlusPublisherInfo, sdkPlusInitCallback);
    }

    // Helper for showing toast messages on the screen
    private void showToast(final Context appContext, final CharSequence text) {
        Logger.d(TAG, text.toString());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
