package com.taboola.samples.endlessfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager;
import com.taboola.android.plus.notification.TBNotificationManager;
import com.taboola.samples.endlessfeed.sampleUtil.DemoItem;
import com.taboola.samples.endlessfeed.sampleUtil.EndlessScrollListener;
import com.taboola.samples.endlessfeed.sampleUtil.FeedAdapter;
import com.taboola.samples.endlessfeed.sampleUtil.UrlOpenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager.HOME_SCREEN_PLACEMENT_TO_OPEN;
import static com.taboola.android.plus.homeScreenNews.TBHomeScreenNewsManager.HOME_SCREEN_URL_TO_OPEN;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Snackbar snackbar;
    private RecyclerView.Adapter mAdapter;
    private TBPlacement mPlacement;
    private final List<Object> mData = new ArrayList<>();
    private String placementName = "list_item";

    public static void launch(Context context, String homeScreenKey, Bundle extras) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(homeScreenKey, homeScreenKey);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Avoid handling click again when activity is recreated
            TBNotificationManager.handleClick(getIntent(), this);
        }

        handleHnsIntent(getIntent().getExtras());
        initLayout();
        fetchTaboolaRecommendations();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handling notification click
        TBNotificationManager.handleClick(intent, this);
    }

    private void handleHnsIntent(Bundle extras) {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(SettingsActivity.PREFS_NAME,
                MODE_PRIVATE);
        if (getIntent().hasExtra(HomeScreenReceiver.HOME_SCREEN_KEY)) {
            // If Home Screen News opened successfully notify the SDK+
            TBHomeScreenNewsManager.getInstance().reportHomeScreenOpened();

            String urlToOpen = extras.getString(HOME_SCREEN_URL_TO_OPEN);
            String placementToOpen = extras.getString(HOME_SCREEN_PLACEMENT_TO_OPEN);

            if (TextUtils.isEmpty(urlToOpen) && TextUtils.isEmpty(placementToOpen)) {
                // just open your HSN activity
                return;
            }

            if (pref.getBoolean(SettingsActivity.SHOULD_HANDLE_URL, false)) { // can be disabled by sample app
                if (!TextUtils.isEmpty(urlToOpen)) {
                    // handle url opening your way
                    UrlOpenUtil.openUrlInTabsOrBrowser(getApplicationContext(), urlToOpen);
                }
            }

            if (!TextUtils.isEmpty(placementToOpen)) {
                // TODO IMPORTANT: if placement isn't empty you must use it on HSN for correct reporting
                placementName = placementToOpen;
            }
        }
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
        FloatingActionButton settingsFab = findViewById(R.id.setting_fab);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.launch(MainActivity.this);
            }
        });
        RecyclerView mRecyclerView = findViewById(R.id.main_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        EndlessScrollListener scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextRecommendationsBatch();
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(scrollListener);
        mAdapter = new FeedAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        snackbar = Snackbar.make(mRecyclerView, "Waiting for network", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public void onAttributionClick(View view) {
        TaboolaApi.getInstance().handleAttributionClick(this);
    }

    // region feed related logic

    private void onRecommendationsFetched(TBPlacement placement) {
        placement.prefetchThumbnails();
        mPlacement = placement;
        List<Object> mergedFeedItems = mergeFeedItems(getDemoData(), placement.getItems());

        int currentSize = mAdapter.getItemCount();
        mData.addAll(mergedFeedItems);
        mAdapter.notifyItemRangeInserted(currentSize, mergedFeedItems.size());
    }

    private List<Object> mergeFeedItems(List<DemoItem> demoItems,
                                        List<TBRecommendationItem> recommendationItems) {
        int size = demoItems.size() + recommendationItems.size();
        List<Object> mergedFeedItems = new ArrayList<>(size);
        mergedFeedItems.addAll(demoItems);

        // insert taboola ad every 4 elements
        int nextInsertionIndex = 4;
        for (TBRecommendationItem item : recommendationItems) {
            mergedFeedItems.add(nextInsertionIndex, item);
            nextInsertionIndex += 5;
        }

        return mergedFeedItems;
    }

    private List<DemoItem> getDemoData() {
        List<DemoItem> list = new ArrayList<>(16);
        Resources res = getResources();
        for (int i = 0; i < 4; i++) {
            list.add(new DemoItem(R.drawable.image_demo, res.getString(R.string.lorem_ipsum1)));
            list.add(new DemoItem(R.drawable.image_demo, res.getString(R.string.lorem_ipsum2)));
            list.add(new DemoItem(R.drawable.image_demo, res.getString(R.string.lorem_ipsum3)));
            list.add(new DemoItem(R.drawable.image_demo, res.getString(R.string.lorem_ipsum4)));
        }
        return list;
    }

    private void fetchTaboolaRecommendations() {
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        TBPlacementRequest placementRequest = new TBPlacementRequest(placementName, 4)
                .setThumbnailSize(screenSize.x / 2, (screenSize.y / 6)); // ThumbnailSize is optional

        TBRecommendationsRequest request = new TBRecommendationsRequest("http://example.com", "text");
        request.addPlacementRequest(placementRequest);

        TaboolaApi.getInstance().fetchRecommendations(request, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                Toast.makeText(MainActivity.this, "Fetch failed: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
                snackbar.dismiss();
            }

            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                MainActivity.this.onRecommendationsFetched(response.getPlacementsMap().get(placementName));
                snackbar.dismiss();
            }
        });
    }

    private void loadNextRecommendationsBatch() {
        TaboolaApi.getInstance().getNextBatchForPlacement(mPlacement, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                TBPlacement placement = response.getPlacementsMap().values().iterator().next(); // there will be only one placement
                MainActivity.this.onRecommendationsFetched(placement);
            }

            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                Toast.makeText(MainActivity.this, "Fetch failed: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // endregion
}
