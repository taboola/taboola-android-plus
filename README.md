# Taboola Native Android SDK PLUS (TaboolaApi Plus)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
[![License](https://img.shields.io/badge/License%20-Taboola%20SDK%20License-blue.svg)](https://github.com/taboola/taboola-android/blob/master/LICENSE)

## Table Of Contents
1. [Getting Started](#1-getting-started)
2. [Example Apps](#2-example-apps)
3. [SDK Reference](#3-sdk-reference)
4. [Proguard](#4-proguard)
5. [License](#5-license)

## Basic concepts
Taboola SDK Plus allows you to display Taboola recommendations in a notification from your app.
You can browse through the sample app in this repository to see how
the TaboolaPlus can be used in an app.

## 1. Getting Started
The TaboolaPlus is based on [TaboolaApi](https://github.com/taboola/taboola-android-api) and if you are using TaboolaApi in your project, the same instance will be used

### 1.1 Minimum requirements

* Android version 5.0  (```android:minSdkVersion="21"```)

### 1.2 Incorporating the SDK

1. Add the library dependency to your project

  ```groovy
    implementation 'com.taboola:android-sdk-plus:0.6.+'
```
TaboolaPlus has the following dependencies (they are added automatically)
```
    implementation 'com.taboola:android-sdk:2.0.+@aar'
    implementation 'com.android.support:customtabs:26.1.0'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.squareup.picasso:picasso:2.5.2'
 ```

2. Include this line in your app’s `AndroidManifest.xml` to allow Internet access
 ```
   <uses-permission android:name="android.permission.INTERNET" />
 ```

> ## Notice
> We encourage developers to use the latest SDK version. In order to stay up-to-date we suggest subscribing to get github notifications whenever there is a new release. For more information check: https://help.github.com/articles/managing-notifications-for-pushes-to-a-repository/

### 1.3 Init TaboolaApi Plus

In your `Application` class.

```java
    public class MyApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            TaboolaPlus.getInstance()
                    .init(applicationContext, TABOOLA_CONFIG_JSON);
        }
    }
```
### 1.4 Controlling notifications
Anywhere in your application you can control notifications throgh
`TBNotificationManager` that can be obtained from `TaboolaPlus` class.
For notifications to be displayed they must be enabled via `TBNotificationManager#enable()`
and categories must be set via `TBNotificationManager#setCategories()` (in any order)

#### Setting category list for notification
```java
   TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .setCategories(categories);
```
#### Enabling notifications
```java
 TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .enable();
```
#### Disabling notifications
```java
 TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .disable();
```

### 1.5 Handling notification click event
Add intent filter in manifest to activity that will handle clicks.
```xml
    <!-- Handle Taboola Notification click -->
         <intent-filter>
             <action android:name="com.taboola.android.plus.notification.NOTIFICATION_CLICK_EVENT" />

             <category android:name="android.intent.category.DEFAULT" />
         </intent-filter>
```
#### 1.5.1 Handling notification click manually in activity.
On each item click, Intent will be sent with custom notification click action.
Extras from Intent will contain `TBPlacement` that contains `TBRecommendationItem`s (same as in `TaboolaApi`)
and index of an item that was clicked. For more info on how to work with this objects you can looks at `TaboolaApi` documentation
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleClickIntentIfNeeded(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleClickIntentIfNeeded(intent);
    }

    private void handleClickIntentIfNeeded(Intent intent) {
        Bundle extras = intent.getExtras();
        if ((intent.getAction() != null) &&
                intent.getAction().equals(TBNotificationManager.NOTIFICATION_CLICK_INTENT_ACTION) &&
                (extras != null)) {
            int itemIndex = extras.getInt(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_ITEM_INDEX);
            TBPlacement placement = extras.getParcelable(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_PLACEMENT);
            TBRecommendationItem item = placement.getItems().get(itemIndex);

            // todo custom click handling

            // for example this will trigger click handling from TaboolaApi
            // and if there is registered click listener in TaboolaApi it will be called
            item.handleClick(this);
        }
    }
```

#### 1.5.2 Handling notification click using TBNotificationManager
`TBNotificationManager.handleClickIntent()` will perform intent validation and call `TBRecommendationItem#handleClick()` (same as in code snippet above).
You propably would want to use it if you already have click handler set in the `TaboolaApi`, or default behaviour is ok for you (content will open in a Chome Tab).
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TBNotificationManager.handleClickIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TBNotificationManager.handleClickIntent(intent);
    }
```
## 2. Example App
This repository includes an example Android app which uses the `TaboolaPlus`.

## 3. SDK Reference
[TaboolaPlus Reference](doc/TaboolaPlus_reference.md)

## 4. ProGuard
TBD

## 5. License
This program is licensed under the Taboola, Inc. SDK License Agreement (the “License Agreement”).  By copying, using or redistributing this program, you agree to the terms of the License Agreement.  The full text of the license agreement can be found at [https://github.com/taboola/taboola-android/blob/master/LICENSE](https://github.com/taboola/taboola-android/blob/master/LICENSE).
Copyright 2017 Taboola, Inc.  All rights reserved.
