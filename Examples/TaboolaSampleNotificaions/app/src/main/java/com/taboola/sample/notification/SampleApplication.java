package com.taboola.sample.notification;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.SdkPlusInitCallback;
import com.taboola.android.plus.core.SdkPlusPublisherInfo;
import com.taboola.android.plus.core.TaboolaSdkPlus;

import static com.taboola.android.plus.core.PlusFeature.SCHEDULED_NOTIFICATIONS;
import static com.taboola.sample.notification.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.sample.notification.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.sample.notification.UtilsHelper.showToast;


public class SampleApplication extends Application {

    public static final String CONFIG_ID = "conf1";
    public static final String PUB_NAME = "sdk-tester-plus";

    @Override
    public void onCreate() {
        super.onCreate();

        //int sdk plus
        initSdkPlus(this);
    }


    private void initSdkPlus(final Context appContext) {
        SdkPlusPublisherInfo sdkPlusPublisherInfo = new SdkPlusPublisherInfo();
        sdkPlusPublisherInfo.setConfigId(CONFIG_ID);
        sdkPlusPublisherInfo.setPublisherName(PUB_NAME);
        sdkPlusPublisherInfo.setActiveFeatures(SCHEDULED_NOTIFICATIONS);
        SdkPlusInitCallback sdkPlusInitCallback = new SdkPlusInitCallback() {
            @Override
            public void onFeatureInitSuccessful(TaboolaSdkPlus taboolaSdkPlus, PlusFeature plusFeature) {
                showToast(appContext, plusFeature.name() + " initialized");
                notifySdkPlusInitFinished(plusFeature, appContext);
            }

            @Override
            public void onFeatureInitFailed(PlusFeature plusFeature, Throwable throwable) {
                final String text = plusFeature.name() + " init failed";
                showToast(appContext, text);
                // TODO: handle init failure in your code
            }
        };

        TaboolaSdkPlus.init(sdkPlusPublisherInfo, sdkPlusInitCallback);
    }


    //notify SettingActivity that init is done
    private void notifySdkPlusInitFinished(PlusFeature plusFeature, Context appContext) {
        if (plusFeature == SCHEDULED_NOTIFICATIONS) {
            //notify sdk plus init is done
            Intent intent = new Intent(ACTION_FOR_INIT_FINISH);
            intent.putExtra(EXTRA_FOR_INIT_FINISH, SCHEDULED_NOTIFICATIONS);
            appContext.sendBroadcast(intent);
        }
    }

}
