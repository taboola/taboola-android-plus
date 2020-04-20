package com.taboola.samples.endlessfeed.api_feed;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taboola.android.api.TBImageView;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBTextView;
import com.taboola.samples.endlessfeed.R;

import java.util.List;

class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private final int TYPE_HEADER = 0;
    private final int TYPE_TABOOLA = 1;
    private final int TYPE_ARTICLE = 2;

    private final List<Object> mData;

    private final OnAttributionClick mAttributionClickCallback;

    FeedAdapter(List<Object> data, OnAttributionClick attributionClickCallback) {
        mData = data;
        mAttributionClickCallback = attributionClickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_ARTICLE: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_article_item, parent, false);
                return new ArticleItemViewHolder(linearLayout);
            }
            case TYPE_HEADER: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_header_item, parent, false);
                return new HeaderItemViewHolder(linearLayout);
            }
            case TYPE_TABOOLA: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_taboola_item, parent, false);
                return new TaboolaItemViewHolder(linearLayout);
            }
            default: {
                throw new IllegalStateException("Unknown view type: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case TYPE_ARTICLE: {
                ArticleItemViewHolder articleHolder = (ArticleItemViewHolder) holder;
                String article = (String) mData.get(position);
                articleHolder.mArticleText.setText(article);
                break;
            }

            case TYPE_HEADER: {
                HeaderItemViewHolder headerHolder = (HeaderItemViewHolder) holder;
                headerHolder.attributionView.setOnClickListener(this);
                HeaderItemViewHolder demoHolder = (HeaderItemViewHolder) holder;
                HeaderItem dm = (HeaderItem) mData.get(position);
                demoHolder.mImageView.setImageResource(dm.getImageResourceId());
                demoHolder.mTextView.setText(dm.getText());
                break;
            }

            case TYPE_TABOOLA: {
                TBRecommendationItem item = (TBRecommendationItem) mData.get(position);
                LinearLayout adContainer = ((TaboolaItemViewHolder) holder).mAdContainer;
                Context ctx = adContainer.getContext();

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TBImageView thumbnailView = item.getThumbnailView(ctx);
                TBTextView titleView = item.getTitleView(ctx);
                TBTextView brandingView = item.getBrandingView(ctx);
                TBTextView descriptionView = item.getDescriptionView(ctx);
                adContainer.addView(thumbnailView);
                adContainer.addView(titleView, layoutParams);

                if (brandingView != null) {
                    adContainer.addView(brandingView, layoutParams);
                }

                if (descriptionView != null) {
                    adContainer.addView(descriptionView, layoutParams);
                }

                break;
            }

            default: {
                throw new IllegalStateException("Unknown view type: " + holder.getItemViewType());
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof TaboolaItemViewHolder) {
            ((TaboolaItemViewHolder) holder).mAdContainer.removeAllViews();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof HeaderItem) {
            return TYPE_HEADER;
        } else if (mData.get(position) instanceof TBRecommendationItem) {
            return TYPE_TABOOLA;
        } else if (mData.get(position) instanceof String) {
            return TYPE_ARTICLE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        mAttributionClickCallback.onAttributionClick();
    }


    static class HeaderItemViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout attributionView;
        final ImageView mImageView;
        final TextView mTextView;

        HeaderItemViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mImageView = linearLayout.findViewById(R.id.ic_attribution);
            mTextView = linearLayout.findViewById(R.id.text_attribution);
            attributionView = linearLayout;
        }
    }

    static class TaboolaItemViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout mAdContainer;

        TaboolaItemViewHolder(LinearLayout adContainer) {
            super(adContainer);
            mAdContainer = adContainer;
        }
    }

    static class ArticleItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mArticleText;

        ArticleItemViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mArticleText = linearLayout.findViewById(R.id.text_article);
        }
    }

    static class HeaderItem {
        private Integer mImageResourceId;
        private String mText;

        public HeaderItem(Integer imageResourceId, String text) {
            mImageResourceId = imageResourceId;
            mText = text;
        }

        Integer getImageResourceId() {
            return mImageResourceId;
        }

        public void setImageResourceId(Integer imageResourceId) {
            mImageResourceId = imageResourceId;
        }

        String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }
    }

}