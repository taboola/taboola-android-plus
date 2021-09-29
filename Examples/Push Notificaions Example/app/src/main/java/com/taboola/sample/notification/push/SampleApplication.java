package com.taboola.sample.notification.push;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.SdkPlusInitCallback;
import com.taboola.android.plus.core.SdkPlusPublisherInfo;
import com.taboola.android.plus.core.TaboolaSdkPlus;

import static com.taboola.android.plus.core.PlusFeature.PUSH_NOTIFICATIONS;
import static com.taboola.android.plus.core.PlusFeature.SCHEDULED_NOTIFICATIONS;
import static com.taboola.sample.notification.push.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.sample.notification.push.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.sample.notification.push.UtilsHelper.showToast;


public class SampleApplication extends Application {

    public static final String CONFIG_ID = "conf1";
    public static final String PUBLISHER_NAME = "sdk-tester-plus";

    @Override
    public void onCreate() {
        super.onCreate();

        /* SDK Plus requires the app to use Firebase.
         * Please make sure to integrate it according to the official FCM documentation
         * https://firebase.google.com/docs/android/setup */
        // FirebaseApp.initializeApp(this); this line should be uncommented in your app,
        // this will not work as you are missing google-services.json in this app.

        //init sdk plus
        initSdkPlus(this);
    }


    private void initSdkPlus(final Context appContext) {
        SdkPlusPublisherInfo sdkPlusPublisherInfo = new SdkPlusPublisherInfo();
        sdkPlusPublisherInfo.setConfigId(CONFIG_ID);
        sdkPlusPublisherInfo.setPublisherName(PUBLISHER_NAME);
        sdkPlusPublisherInfo.setActiveFeatures(PUSH_NOTIFICATIONS, SCHEDULED_NOTIFICATIONS);
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
            //notify sdk plus feature init is done, will be called separately for push and scheduled notifications
            Intent intent = new Intent(ACTION_FOR_INIT_FINISH);
            intent.putExtra(EXTRA_FOR_INIT_FINISH, plusFeature);
            appContext.sendBroadcast(intent);
    }

}
