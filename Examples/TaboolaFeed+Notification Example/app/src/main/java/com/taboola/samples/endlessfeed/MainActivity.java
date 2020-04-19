package com.taboola.samples.endlessfeed;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.plus.notification.TBNotificationManager;
import com.taboola.samples.endlessfeed.sampleUtil.DemoItem;
import com.taboola.samples.endlessfeed.sampleUtil.EndlessScrollListener;
import com.taboola.samples.endlessfeed.sampleUtil.FeedAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Snackbar snackbar;
    private RecyclerView.Adapter mAdapter;
    private TBPlacement mPlacement;
    private final List<Object> mData = new ArrayList<>();
    private String placementName = "list_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Avoid handling click again when activity is recreated
            TBNotificationManager.handleClick(getIntent(), this);
        }

        initLayout();
        fetchTaboolaRecommendations();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handling notification click
        TBNotificationManager.handleClick(intent, this);
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
            list.add(new DemoItem(R.mipmap.ic_launcher, res.getString(R.string.lorem_ipsum1)));
            list.add(new DemoItem(R.mipmap.ic_launcher_round, res.getString(R.string.lorem_ipsum2)));
            list.add(new DemoItem(android.R.mipmap.sym_def_app_icon, res.getString(R.string.lorem_ipsum3)));
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
