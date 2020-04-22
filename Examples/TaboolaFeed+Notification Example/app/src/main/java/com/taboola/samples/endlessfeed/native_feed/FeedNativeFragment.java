package com.taboola.samples.endlessfeed.native_feed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.utils.SdkDetailsHelper;
import com.taboola.samples.endlessfeed.R;

import java.util.HashMap;


public class FeedNativeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_native, container, false);
        TaboolaWidget taboolaWidgetBottom = view.findViewById(R.id.taboola_widget_below_article);
        taboolaWidgetBottom
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setInterceptScroll(true);

        taboolaWidgetBottom.getLayoutParams().height = SdkDetailsHelper.getDisplayHeight(taboolaWidgetBottom.getContext()) * 2;
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("enableHorizontalScroll", "true");
        taboolaWidgetBottom.setExtraProperties(optionalPageCommands);
        taboolaWidgetBottom.fetchContent();
        return view;
    }

}
