package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SettingCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface SettingAdapterCallback {
        void onSelectedCategoryChange(@NonNull List<Category> categories);
    }

    private List<Category> categories;

    @NonNull
    private SettingAdapterCallback callback;

    public SettingCategoriesAdapter(@NonNull List<Category> categories, @NonNull SettingAdapterCallback callback) {
        this.callback = callback;
        this.categories = categories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new SettingCategoriesViewHolder(view, () -> callback.onSelectedCategoryChange(categories));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SettingCategoriesViewHolder) {
            ((SettingCategoriesViewHolder) holder).bind(categories.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
