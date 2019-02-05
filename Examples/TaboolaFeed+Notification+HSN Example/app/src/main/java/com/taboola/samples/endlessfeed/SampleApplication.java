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
        TaboolaApi.getInstance().init(getApplicationContext(), "betterbytheminute-app",
                "4f1e315900f2cab825a6683d265cef18cc33cd27");
        final Drawable placeHolder = ContextCompat.getDrawable(this, R.drawable.image_placeholder);
        TaboolaApi.getInstance().setImagePlaceholder(placeHolder);
    }
}
