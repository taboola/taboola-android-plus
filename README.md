# Taboola Recommendations Notification SDK (TaboolaPlus)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
[![License](https://img.shields.io/badge/License%20-Taboola%20SDK%20License-blue.svg)](https://github.com/taboola/taboola-android/blob/master/LICENSE)
[![Version](https://img.shields.io/badge/Version%20-0.6.0-yellow.svg)](https://github.com/taboola/taboola-android/blob/master/LICENSE)

## Table Of Contents
1. [Getting Started](#1-getting-started)
2. [Example Apps](#2-example-app)
3. [SDK Reference](#3-sdk-reference)
4. [License](#4-license)

## Introduction
Taboola SDK Plus allows you to display Taboola recommendations in an Android notification from your app.
You can browse through the sample app in this repository to see how
the TaboolaPlus can be used in an app.

### Main features

* Show Taboola recommendations as a notification, with a polished UI
<img src="https://github.com/taboola/taboola-android-plus/raw/master/images/notification_small.png" width="500" height="whatever">

* Users can expand the notification for a larger, more detailed view
<img src="https://github.com/taboola/taboola-android-plus/raw/master/images/notification_expanded.png" width="500" height="whatever">

* No special Android permissions required

* Show recommendations from different content categories or choose a single "general" category to show the best and most interesting content for your users.

* Users can scroll through the recommendations directly from the notification

* Recommendations automatically update periodically (configurable)

* Minimal network usage, can be configured to fetch new content when required or only over Wifi

* Minimal device resource consumption (battery, CPU)

* Tapping a notification navigates to your own app

* Based on Taboola SDK, see [Taboola SDK documentation](https://github.com/taboola/taboola-android-api) for more details


## 1. Getting Started

### 1.1 Minimum requirements


* Android version 5.0  (```android:minSdkVersion="21"```)


### 1.2 Incorporating the SDK


* Add the library dependency to your project gradle file

  ```groovy
    implementation 'com.taboola:android-sdk-plus:0.6.+'
    ```
       
 TaboolaPlus has the following dependencies (added automatically)

  ```
 implementation 'com.taboola:android-sdk:2.0.+@aar'
 implementation 'com.android.support:customtabs:26.1.0'
 implementation 'com.squareup.retrofit2:retrofit:2.3.0'
 implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
 implementation 'com.squareup.picasso:picasso:2.5.2'
  ```
 
* Include this line in your app’s `AndroidManifest.xml` to allow Internet access
 ```xml
 <uses-permission android:name="android.permission.INTERNET" />
 ```

> ## Notice
> We encourage developers to use the latest SDK version. In order to stay up-to-date we suggest subscribing to get github notifications whenever there is a new release. For more information check: https://help.github.com/articles/managing-notifications-for-pushes-to-a-repository/


### 1.3 Init TaboolaPlus
Add the following initialization code your `Application` class. It's important to perform this initialization as early as possible during your app lifecyle, to make sure TaboolaPlus will be ready for action when you need it.

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
 
 ```TABOOLA_CONFIG_JSON``` is a JSON string, which contains your specific Taboola configuration. 
 This JSON will be provided by your Taboola account manager. Please consult your account manager before making any changes in this JSON configuration.
 
 **Example config JSON:**
  
  ```java
{
	"apiKey": "api-key",
	"publisher": "publisher-id",
	"configSetId": "config-set-id"
}
  ```
 
### 1.4 Controlling notifications
In order to start displyaing notifications, notifications should be enabled via `TBNotificationManager#enable()` that can be obtained from `TaboolaPlus` class, and categories must be set via `TBNotificationManager#setCategories()`.

>##Notice
>Please consider the best location within your app code to enabled notifications, in order to make sure that notifications appear as soon as possible after device reboot.

Once the notifications were enabled, they will continue to show and be updated periodically even if your app is not running.

#### Enabling notifications
```java
 TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .enable();
```
#### Disabling notifications
You may choose to allow your user to disable the notification from a UI, such as your app settings screen.
Run this code to disable notifications:

```java
 TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .disable();
```

#### Setting categories for the notification
As an app developer, you can choose to allow your user to customize the recommendations displayed on the notification by choosing content categories (News, Sports, Tech etc..)
In order to show a mix of all content categories, set the "general" category.

**Consult your Taboola account manager in order to enable category customiztion for your app**

```java
ArrayList<Category> categories = new ArrayList<>();
categories.add(new Category("general", "General", true));
TaboolaPlus.getInstance().getNotificationManager().setCategories(categories);
```


### 1.5 Handling notification click event
Add the folowing intent filter in your app manifest to the activity that will handle clicks.

```xml
    <!-- Handle Taboola Notification click -->
         <intent-filter>
             <action android:name="com.taboola.android.plus.notification.NOTIFICATION_CLICK_EVENT" />

             <category android:name="android.intent.category.DEFAULT" />
         </intent-filter>
```

#### 1.5.1 Handling notification click manually in activity.
Tapping a notification will fire the ```com.taboola.android.plus.notification.NOTIFICATION_CLICK_EVENT``` intent,
with the relevant data added as extras on the Intent object.
Extras will contain `TBPlacement` that contains a list of `TBRecommendationItem` (same as in `TaboolaApi`)
and the index of the item which was clicked. For more details on these objects please refer to `TaboolaApi` documentation

`TBNotificationManager.handleClickIntent()` will perform intent validation and execute `TBRecommendationItem#handleClick()` to trigger the recommendation click flow.  see [Taboola SDK documentation](https://github.com/taboola/taboola-android-api) for more details about setting a click callback, or intercepting clicks.

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

The recommended next steps would be:

1. Review the sample app carefully
2. Replace the sample configuration in the sample app with the configuration provided by your Taboola account manager, to see how your content will look like in your app.
3. Integrate TaboolaPlus into your app 

## 3. SDK Reference
TBD

## 4. License
This program is licensed under the Taboola, Inc. SDK License Agreement (the “License Agreement”).  By copying, using or redistributing this program, you agree to the terms of the License Agreement.  The full text of the license agreement can be found at [https://github.com/taboola/taboola-android/blob/master/LICENSE](https://github.com/taboola/taboola-android/blob/master/LICENSE).
Copyright 2017 Taboola, Inc.  All rights reserved.
