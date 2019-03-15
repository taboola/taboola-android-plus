package com.example.homescreenexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import java.util.List;

public class UrlOpenUtil {

    public static boolean areChromeCustomTabsSupported(Context context) {
        Intent serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        serviceIntent.setPackage("com.android.chrome");
        List resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);

        try {
            Class.forName("android.support.customtabs.CustomTabsService");
        } catch (ClassNotFoundException var6) {
            Log.d("ContentValues", "areChromeCustomTabsSupported :: ChromeCustomTabs not included in hosting app");
            return false;
        }

        return resolveInfos != null && !resolveInfos.isEmpty();
    }

    public static void openUrlInTabsOrBrowser(Context context, String url) {
        try {
            if (areChromeCustomTabsSupported(context)) {
                Log.d("ContentValues", "openChromeTab :: opening ad in a ChromeTab");
                CustomTabsIntent customTabsIntent = (new CustomTabsIntent.Builder()).build();
                handleNonActivityContext(context, customTabsIntent.intent);
                customTabsIntent.launchUrl(context, Uri.parse(url));
            } else {
                Log.d("ContentValues", "openNativeBrowser :: opening add");
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                handleNonActivityContext(context, intent);
                context.startActivity(intent);
            }
        } catch (Exception var3) {
            Log.e("ContentValues", "openUrlInTabsOrBrowser :: failed to open url " + var3.toString());
        }

    }

    @MainThread
    public static void openUrlInApp(@NonNull Context context, @NonNull String url, @NonNull String packageName) {
        Uri uri = Uri.parse(url);
        CustomTabsIntent customTabsIntent = (new CustomTabsIntent.Builder()).build();
        Intent intent = customTabsIntent.intent;
        intent.setPackage(packageName);
        intent.setData(uri);
        handleNonActivityContext(context, intent);
        PackageManager pm = context.getPackageManager();
        if (pm.resolveActivity(intent, 0) == null) {
            intent.setPackage((String) null);
        }
        customTabsIntent.launchUrl(context, uri);
    }

    private static void handleNonActivityContext(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

    }
}
