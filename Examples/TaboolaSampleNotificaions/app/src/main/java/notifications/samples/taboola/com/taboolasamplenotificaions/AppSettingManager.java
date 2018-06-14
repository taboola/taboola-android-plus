package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AppSettingManager {

    private static final String APP_SETTINGS_KEY = "app_config";
    private static final String SHARED_PREF_NAME = "app_setting";

    public static AppSettings getAppSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String json = sharedPreferences.getString(APP_SETTINGS_KEY, null);
        if (json != null) {
            return new Gson().fromJson(json, AppSettings.class);
        } else {
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("general", "General", true));
            categories.add(new Category("sport", "Sport", true));
            categories.add(new Category("news", "News", true));
            categories.add(new Category("business", "Business", true));
            categories.add(new Category("technology", "Technology", false));
            categories.add(new Category("entertainment", "Entertainment", false));
            categories.add(new Category("science", "Science", true));

            AppSettings appSettings = new AppSettings(false, categories);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(APP_SETTINGS_KEY, new Gson().toJson(appSettings));
            editor.apply();

            return appSettings;
        }
    }

    public static void updateAppSettings(Context context, AppSettings appSettings) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_SETTINGS_KEY, new Gson().toJson(appSettings));
        editor.apply();
    }

    public static List<String> getSelectedCategoriesIds(List<Category> categories) {
        ArrayList<String> categoriesIds = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).isEnable()) {
                categoriesIds.add(categories.get(i).getId());
            }
        }
        return categoriesIds;
    }
}
