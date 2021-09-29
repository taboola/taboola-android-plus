package com.taboola.sample.notification;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


class SettingCategoriesViewHolder extends RecyclerView.ViewHolder {

    public interface SettingsHolderCallback {
        void onCategoryChanges();
    }

    private final CheckBox cbAllowCategory;

    @NonNull
    private final SettingsHolderCallback callback;

    @Nullable
    private Category category;

    public SettingCategoriesViewHolder(View itemView, @NonNull SettingsHolderCallback callback) {
        super(itemView);
        cbAllowCategory = itemView.findViewById(R.id.cb_allow);
        this.callback = callback;
    }

    public void bind(@NonNull Category category) {
        this.category = category;
        cbAllowCategory.setOnCheckedChangeListener(null);
        cbAllowCategory.setText(category.getName());
        cbAllowCategory.setChecked(category.isEnable());
        if (category.getName().equals("General")) {
            cbAllowCategory.setEnabled(false);
        }
        initOnChangeListener();
    }

    private void initOnChangeListener() {
        cbAllowCategory.setOnCheckedChangeListener((compoundButton, b) -> {
            if (category != null) {
                category.setEnable(b);
                callback.onCategoryChanges();
            }
        });
    }
}
