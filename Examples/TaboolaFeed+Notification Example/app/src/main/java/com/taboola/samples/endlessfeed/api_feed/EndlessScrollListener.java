package com.taboola.samples.endlessfeed.api_feed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the data set after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    private final LinearLayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition;
        int totalItemCount = mLayoutManager.getItemCount();

        lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            // Sets the starting page index
            int startingPageIndex = 0;
            this.currentPage = startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the data set count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        int visibleThreshold = 5;
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            if (totalItemCount != 1) { // Remove this line if you don't push an article as the first item in the recyclerview!
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}

