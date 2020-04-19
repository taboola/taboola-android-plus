package com.taboola.samples.endlessfeed;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.taboola.android.api.TaboolaApi;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init taboola API
        // Required when using TaboolaApi (Native Android) integration
        TaboolaApi.getInstance().init(getApplicationContext(), "sdk-tester-demo", "30dfcf6b094361ccc367bbbef5973bdaa24dbcd6");

        final Drawable placeHolder = ContextCompat.getDrawable(this, R.drawable.image_placeholder);
        TaboolaApi.getInstance().setImagePlaceholder(placeHolder);
    }
}
