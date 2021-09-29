package com.taboola.samples.endlessfeed;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.taboola.samples.endlessfeed.api_feed.FeedApiFragment;
import com.taboola.samples.endlessfeed.native_feed.FeedNativeFragment;

class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    private static final int FEED_NATIVE = 0;
    private static final int FEED_API = 1;
    private static final int NUM_PAGES = 2;
    private final Context context;

    ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
        context = fa.getApplicationContext();
    }

    @Override
    @NonNull
    public Fragment createFragment(int position) {
        if (position == FEED_API) {
            return new FeedApiFragment();
        } else if (position == FEED_NATIVE) {
            return new FeedNativeFragment();
        }

        return new FeedApiFragment();
    }

    @Nullable
    String getFragmentTitle(int position) {
        if (position == FEED_API) {
            return context.getString(R.string.api_feed_title);
        } else if (position == FEED_NATIVE) {
            return context.getString(R.string.sdk_feed);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }

}