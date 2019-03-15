package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

public class SettingCategoriesViewHolder extends RecyclerView.ViewHolder {

    public interface SettingsHolderCallback {
        void onCategoryChanges();
    }

    private CheckBox cbAllowCategory;

    @NonNull
    private SettingsHolderCallback callback;

    @Nullable
    private Category category;

    public SettingCategoriesViewHolder(View itemView, @NonNull SettingsHolderCallback callback) {
        super(itemView);
        cbAllowCategory=itemView.findViewById(R.id.cb_allow);
        this.callback = callback;
    }

    public void bind(@NonNull Category category) {
        this.category = category;
        cbAllowCategory.setOnCheckedChangeListener(null);
        cbAllowCategory.setText(category.getName());
        cbAllowCategory.setChecked(category.isEnable());
        if(category.getName().equals("General")){
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
