# Taboola Recommendations Notification SDK (TaboolaPlus)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
[![License](https://img.shields.io/badge/License%20-Taboola%20SDK%20License-blue.svg)](https://github.com/taboola/taboola-android/blob/master/LICENSE)
[![Version](https://api.bintray.com/packages/taboola-com/taboola-android-sdk/android-sdk-plus/images/download.svg) ](https://bintray.com/taboola-com/taboola-android-sdk/android-sdk-plus/_latestVersion)
## Table Of Contents
1. [Getting Started](#1-getting-started)
2. [Example Apps](#2-example-app)
3. [SDK Reference](#3-sdk-reference)
4. [Home Screen News](#4-home-Screen-news)
5. [Proguard](#5-proguard)
6. [License](#6-license)

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


Add the library dependency to your project gradle file

```groovy
    implementation 'com.taboola:android-sdk-plus:1.0.4'
```

 TaboolaPlus has the following dependencies (added automatically by gradle)

```groovy
    api('com.taboola:android-sdk:2.1.0') {
        exclude group: 'com.android.support', module: 'customtabs'
        exclude group: 'com.android.support', module: 'support-v4'
    }
    compileOnly 'com.android.support:customtabs:27.1.1'
    compileOnly 'com.squareup.retrofit2:retrofit:2.3.0'
    compileOnly 'com.squareup.retrofit2:converter-gson:2.3.0'
    compileOnly 'com.squareup.picasso:picasso:2.5.2'
```

> ## Notice
> We encourage developers to use the latest SDK version. In order to stay up-to-date we suggest subscribing to get github notifications whenever there is a new release. For more information check: https://help.github.com/articles/managing-notifications-for-pushes-to-a-repository/


### 1.3 Init TaboolaPlus
Use the following code to initialize `TaboolaPlus`.

```java
TaboolaPlus.init("<publisher-name>", "<config-id>",
        new TaboolaPlus.TaboolaPlusRetrievedCallback() { // use lambdas if your language level allows it
            @Override
            public void onTaboolaPlusRetrieved(TaboolaPlus taboolaPlus) {
                TBNotificationManager tbNotificationManager = taboolaPlus.getNotificationManager();
                // todo save tbNotificationManager (or taboolaPlus) object to use it later
                // ideally, you should pass it to your dependency injection framework,
                // but you can use any location on your preference, for example Application class
            }
        }, new TaboolaPlus.TaboolaPlusRetrieveFailedCallback() {
            @Override
            public void onTaboolaPlusRetrieveFailed(Throwable throwable) {
                Log.e(TAG, "TaboolaPlus init failed");
                // todo handle error
            }
        });


```
 
 `<publisher-name>` and `<config-id>` strings will be provided by your Taboola account manager.
 
> ## Notice
> Please make sure to initilize the SDK before invoking any `getInstance` function anywhere in your code

### 1.4 Controlling notifications

#### Enabling notifications
In order to start displaying notifications, notifications must be enabled via `TBNotificationManager#enable()` that can be obtained from `TaboolaPlus` object.

Once the notifications are enabled, they will continue to show and be updated periodically even if your app is not running.

```java
    tbNotificationManager.enable();
```
#### Disabling notifications
You may choose to allow the user to disable the notification from UI, such as your app settings screen.
Run this code to disable notifications:

```java
    tbNotificationManager.disable();
```

#### Setting categories for the notification
Categories must be set via `TBNotificationManager#setCategories()`.
As an app developer, you can choose to allow your user to customize the recommendations displayed on the notification by choosing content categories (News, Sports, Tech etc..)
In order to show a mix of all content categories, set the "general" category.

**Consult your Taboola account manager in order to enable category customiztion for your app**

```java
    ArrayList<String> categories = new ArrayList<>();
    categories.add("general");
    tbNotificationManager.setCategories(categories);
```

#### Optional customisations
`TBNotificationManager` has a set for methods to customise notifications:

* `setNotificationIcon(int iconId)` - Sets an icon to be displayed as an application icon in the notification. If not set, application's icon will be used.
* `setApplicationName(String applicationName)` - Sets an application name to be displayed in the notification.If not set in code, the actual app name will be used. (Note: Application name can be overriden in remote config file)
* `setIsWifiOnlyMode(boolean isWifiOnlyMode)` - If enabled, new content will only be loaded over Wifi

### 1.5 Handling notification click event
Add the folowing intent filter in your app manifest to the activity that will handle clicks.

```xml
    <!-- Handle Taboola Notification click -->
         <intent-filter>
             <action android:name="com.taboola.android.plus.notification.NOTIFICATION_CLICK_EVENT" />

             <category android:name="android.intent.category.DEFAULT" />
         </intent-filter>
```

#### 1.5.1 Handling notification click in activity.
Tapping a notification will fire the ```com.taboola.android.plus.notification.NOTIFICATION_CLICK_EVENT``` intent,
with the relevant data added as extras on the Intent object.
Extras will contain `TBPlacement` that contains a list of `TBRecommendationItem` (same as in `TaboolaApi`)
and the index of the item which was clicked. For more details on these objects please refer to `TaboolaApi` documentation

`TBNotificationManager.handleClickIntent()` will perform intent validation and execute `TBRecommendationItem#handleClick()` to trigger the recommendation click flow.  see [Taboola SDK documentation](https://github.com/taboola/taboola-android-api#19-intercepting-recommendation-clicks) for more details about setting a click callback, or intercepting clicks.

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) { // avoid handling click again when activity is recreated
             TBNotificationManager.handleClick(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
         TBNotificationManager.handleClick(intent, this);
    }
```

### 1.6 Restart App on phone reboot
To be able launch app on phone boot completed, we recommend you to implement these 3 simple steps.

1. Add permission to manifest
```xml
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

2. Create broadcast receiver that will start the app when the message is received
```java
    public class PhoneBootedReceiver extends BroadcastReceiver {
       @Override
       public void onReceive(Context context, Intent intent) {
            startApp();
       }
    
       private void startApp(){
           //launch you app
       }
    }
```

3. Register broadcast receiver in the manifest file with filter to listen to the phone reboot
```xml
    <receiver android:name=".PhoneBootedReceiver">
       <intent-filter>
           <action android:name="android.intent.action.BOOT_COMPLETED" />
       </intent-filter>
    </receiver>
```


## 2. Example App
This repository includes an example Android app which uses the `TaboolaPlus`. 

The recommended next steps would be:

1. Review the sample app carefully
2. Replace the sample configuration in the sample app with the configuration provided by your Taboola account manager, to see how your content will look like in your app.
3. Integrate TaboolaPlus into your app 

## 3. SDK Reference
TBD

## 4. Home Screen News
### 4.1 Init Home Screen News manager
You have to init Home Screen News manager in order to use this feature.
(It is inited independently from TaboolaPlus)
```java
 TBHomeScreenNewsManager.getInstance().init();
```

### 4.2 Implement home screen reciever
You have to implement broadcast reciever in order to know when you should run your Home Screen News activity (action = `com.taboola.android.plus.homeScreen.OPEN_HOME_SCREEN`).
``` java
public class HomeScreenNewsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            //run home screen activity
             Intent hsnIntent = new Intent(context, HomeScreenActivity.class);
             hsnIntent.putExtras(intent.getExtras());
             hsnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             context.startActivity(hsnIntent);
    }
}
```
Add to manifest in application scope
```xml
        <receiver
            android:name=".HomeScreenNewsReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="com.taboola.android.plus.homeScreen.OPEN_HOME_SCREEN" />
            </intent-filter>
        </receiver>
```
#### 4.2.1 Report home screen opened
You have to report when you will open home screen activity.
```java
public class HomeScreenNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_news);
       
        TBHomeScreenNewsManager.getInstance().reportHomeScreenOpened();  // If home screen opened successfully
        handleHsnIntentExtras(getIntent().getExtras());
    }
}
````
### 4.3 Handle HSN extras
You also have an ability to send a custom URL by the HSN to open it.
If HSN was triggered and you were using placements on this screen to get data you must update placement for correct reporting.
``` java
  void handleHsnIntentExtras(Bundle extras) {
          String urlToOpen = extras.getString(HOME_SCREEN_URL_TO_OPEN);
          String placementToOpen = extras.getString(HOME_SCREEN_PLACEMENT_TO_OPEN);
          if (TextUtils.isEmpty(urlToOpen) && TextUtils.isEmpty(placementToOpen)) {
              // just open your HSN activity
              return;
          }
  
          if (!TextUtils.isEmpty(urlToOpen)){
              // TODO handle url opening your way
          }
  
          if (!TextUtils.isEmpty(placementToOpen)){
              // TODO IMPORTANT: if placement isn't empty you must use it on HSN for correct reporting
          }
      }
```
### 4.4 Enable/disable home screen
In order to enable/disable home screen you have to call:
```java
        //to enable
        TBHomeScreenNewsManager.getInstance().setHomeScreenEnabled(true);
        //to disable
        TBHomeScreenNewsManager.getInstance().setHomeScreenEnabled(false);
```
### 4.5 Check if home screen initialized
Check if TBHomeScreenNewsManager initialized, true - initialized, false - not initialized.
```java
TBHomeScreenNewsManager.getInstance().isInitialized();
```

### 4.6 Check if home screen enabled
Check if home screen enabled, true - enabled, false - not enabled
```java
TBHomeScreenNewsManager.getInstance().isHomeScreenEnabled();
```


## 5. ProGuard
You can find proguard rules for Taboola sdk plus in [proguard-taboola-api.pro](https://github.com/taboola/taboola-android-api/blob/master/Examples/Article-Page-4-Items-Bottom/app/proguard-taboola-api.pro) file.

If you are also using TaboolaWidget, you can find proguard rules for Taboola Widget in [proguard-taboola-widget.pro](https://github.com/taboola/taboola-android/blob/master/app/proguard-taboola-widget.pro) file.

The file contains instructions to the rules which you should use depending on which parts of the SDK you are using (you should comment/uncomment which you need).

## 6. License
This program is licensed under the Taboola, Inc. SDK License Agreement (the “License Agreement”).  By copying, using or redistributing this program, you agree with the terms of the License Agreement.  The full text of the license agreement can be found at [https://github.com/taboola/taboola-android/blob/master/LICENSE](https://github.com/taboola/taboola-android/blob/master/LICENSE).
Copyright 2017 Taboola, Inc.  All rights reserved.
