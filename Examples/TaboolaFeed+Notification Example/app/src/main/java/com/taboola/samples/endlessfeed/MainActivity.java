package com.taboola.samples.endlessfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.taboola.android.plus.core.PlusFeature;
import com.taboola.android.plus.core.TBLScheduledManager;
import com.taboola.android.plus.core.TaboolaSdkPlus;
import com.taboola.android.plus.shared.TBLNotificationManager;
import com.taboola.android.utils.Logger;

import java.util.Collections;

import static com.taboola.samples.endlessfeed.UtilsHelper.ACTION_FOR_INIT_FINISH;
import static com.taboola.samples.endlessfeed.UtilsHelper.EXTRA_FOR_INIT_FINISH;
import static com.taboola.samples.endlessfeed.UtilsHelper.showToast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CAT_GENERAL = "general";

    private TBLScheduledManager scheduledNotificationManager;
    private FloatingActionButton allowNotificationSwitch;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter pagerAdapter;

    /**
     * this receiver is used to receive intents fired by SampleApplication,
     * onReceive will be called sdk plus init finished
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && TextUtils.equals(ACTION_FOR_INIT_FINISH, intent.getAction())) {
                PlusFeature plusFeature = (PlusFeature) intent.getSerializableExtra(EXTRA_FOR_INIT_FINISH);
                Logger.d(TAG, "PlusFeature FINISH " + plusFeature.name());

                //when getting this action from SampleApplication
                // that's mean that init of ScheduledNotification finished.
                if (plusFeature == PlusFeature.SCHEDULED_NOTIFICATIONS) {
                    scheduledNotificationManager = TaboolaSdkPlus.getScheduledNotificationManager();
                    if (scheduledNotificationManager != null) {
                        //init of Scheduled Notification finished successfully
                        onScheduledNotificationInitFinished();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Avoid handling click again when activity is recreated
            TBLNotificationManager.handleClick(getIntent(), this);
        }

        initLayout();

        scheduledNotificationManager = TaboolaSdkPlus.getScheduledNotificationManager();
        //if manager is null mean the init still in progress and you should wait for onReceive in the receiver above
        if (scheduledNotificationManager != null) {
            //ScheduledNotification is already initialized
            onScheduledNotificationInitFinished();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handling notification click
        TBLNotificationManager.handleClick(intent, this);
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
        allowNotificationSwitch = findViewById(R.id.setting_fab);
        allowNotificationSwitch.setVisibility(View.GONE);

        allowNotificationSwitch.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                //enable Scheduled Notification
                scheduledNotificationManager.enable();
                showToast(this, "Notification enabled");
            } else {
                //disable Scheduled Notification
                scheduledNotificationManager.disable();
                showToast(this, "Notification disabled");
            }
        });


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getItemCount());


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getFragmentTitle(position))
        ).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_FOR_INIT_FINISH));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }


    private void onScheduledNotificationInitFinished() {

         /* in order to start displaying notifications,
         notifications must be enabled via the TBLScheduledManager#enable()
         and the Taboola News categories must be set
         via the TBLScheduledManager#setCategories() method */

        /* As a Taboola News publisher, you can choose to allow your user to customize
         the recommendations displayed on the notification by choosing content categories
         (News, Sports, Tech, etc.)
          In order to show a mix of all content categories, set the "general" category. */
        scheduledNotificationManager.setCategories(Collections.singletonList(CAT_GENERAL));

        //display switch button to disable or enable the scheduled notifications
        allowNotificationSwitch.setVisibility(View.VISIBLE);
        allowNotificationSwitch.setSelected(scheduledNotificationManager.isEnabled());
    }

}
