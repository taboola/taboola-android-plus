package com.taboola.samples.endlessfeed;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.taboola.android.api.TBPublisherApi;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.SdkPlusInitCallback;
import com.taboola.android.plus.core.SdkPlusPublisherInfo;
import com.taboola.android.plus.core.TaboolaSdkPlus;

import static com.taboola.android.plus.core.PlusFeature.SCHEDULED_NOTIFICATIONS;
import static com.taboola.samples.endlessfeed.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.samples.endlessfeed.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.samples.endlessfeed.UtilsHelper.showToast;

public class SampleApplication extends Application {

    public static final String API_PUB_ID = "sdk-tester-demo";
    private static final String API_KEY = "30dfcf6b094361ccc367bbbef5973bdaa24dbcd6";
    public static final String PUB_NAME = "sdk-tester-plus";
    public static final String CONFIG_ID = "conf1";

    @Override
    public void onCreate() {
        super.onCreate();
        //init taboola API that will be used in FeedApiFragment
        // Required when using TaboolaApi (Native Android) integration
        final TBPublisherApi taboolaApi = TaboolaApi.getInstance(API_PUB_ID);
        taboolaApi.init(getApplicationContext(), API_KEY);

        //optional, use it only if you want to override the sdk api placeholder image.
        taboolaApi.setImagePlaceholder(ContextCompat.getDrawable(this, R.drawable.image_placeholder));

        //int sdk plus
        initSdkPlus(this);
    }


    private void initSdkPlus(final Context appContext) {
        SdkPlusPublisherInfo sdkPlusPublisherInfo = new SdkPlusPublisherInfo();
        sdkPlusPublisherInfo.setPublisherName(PUB_NAME);
        sdkPlusPublisherInfo.setConfigId(CONFIG_ID);
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

    //notify MainActivity that init is done
    private void notifySdkPlusInitFinished(PlusFeature plusFeature, Context appContext) {
        if (plusFeature == SCHEDULED_NOTIFICATIONS) {
            //notify sdk plus init is done
            Intent intent = new Intent(ACTION_FOR_INIT_FINISH);
            intent.putExtra(EXTRA_FOR_INIT_FINISH, SCHEDULED_NOTIFICATIONS);
            appContext.sendBroadcast(intent);
        }
    }

}
