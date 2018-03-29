package notifications.samples.taboola.com.taboolasamplenotificaions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Switch;

import com.google.gson.Gson;
import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.plus.TaboolaPlus;
import com.taboola.android.plus.notification.TBNotificationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();
    private static final String APP_SETTINGS_KEY = "app_config";

    Switch switchAllowNotification;
    RecyclerView recyclerViewCategories;

    private SettingCategoriesAdapter adapter;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        appSettings = getAppSettings();
        initOnSwitchCheckedChangesListener();

        initTaboolaSdkPLus();
        initRecView();

        handleClickIntentIfNeeded(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleClickIntentIfNeeded(intent);
    }

    private void initTaboolaSdkPLus() {
        TaboolaPlus.getInstance()
                .init(getApplicationContext(), addPlacementToCategoryMapping(appSettings.getConfig()))
                .getNotificationManager()
                .setCategories(getSelectedCategories(appSettings.getCategories()));
    }

    private void initOnSwitchCheckedChangesListener() {
        switchAllowNotification.setChecked(appSettings.isAllowNotifications());
        switchAllowNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                TaboolaPlus.getInstance()
                        .getNotificationManager()
                        .enable();
            } else {
                TaboolaPlus.getInstance()
                        .getNotificationManager()
                        .disable();
            }
            appSettings.setAllowNotifications(isChecked);
            updateAppSettings();
        });
    }

    private void initRecView() {
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingCategoriesAdapter(appSettings.getCategories(), categories -> {
            TaboolaPlus.getInstance()
                    .getNotificationManager()
                    .setCategories(getSelectedCategories(categories));

            appSettings.setCategories(categories);
            updateAppSettings();
        });
        recyclerViewCategories.setAdapter(adapter);
    }

    private void initViews() {
        switchAllowNotification = findViewById(R.id.switch_allow_notifications);
        recyclerViewCategories = findViewById(R.id.rec_view);
    }

    private List<String> getSelectedCategories(List<Category> categories) {
        ArrayList<String> categoriesIds = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).isEnable()) {
                categoriesIds.add(categories.get(i).getId());
            }
        }
        return categoriesIds;
    }

    private AppSettings getAppSettings() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String json = sharedPreferences.getString(APP_SETTINGS_KEY, null);
        if (json != null) {
            return new Gson().fromJson(json, AppSettings.class);
        } else {
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("sport", "Sport", true));
            categories.add(new Category("news", "News", false));
            categories.add(new Category("business", "Business", true));
            categories.add(new Category("technology", "Technology", false));
            categories.add(new Category("entertainment", "Entertainment", false));
            categories.add(new Category("science", "Science", true));

            String CONFIG = "{   \"TaboolaConfig\": {     \"ContentManagerConfig\": {       \"cacheExpirationTimeMs\": 60000,       \"sdkConfig\": {         \"apiKey\": \"de8cfcf10cdc8337b1582adc99cfd75559f1fd65\",         \"publisher\": \"taboola-reader-app\"       },       \"placementMap\": {         \"main_feed\": {           \"categoryToPlacement\": {             \"sport\": \"AC-main-sports\",             \"news\": \"AC-main-news\",             \"business\": \"AC-main-business\",             \"tech\": \"AC-main-tech\",             \"science\": \"AC-main-science\",             \"entertainment\": \"AC-main-entertainment\",             \"music\": \"AC-main-music\"           }         },         \"category_feed\": {           \"categoryToPlacement\": {             \"sport\": \"AC-feed-sports\",             \"news\": \"AC-feed-news\",             \"business\": \"AC-feed-business\",             \"tech\": \"AC-feed-tech\",             \"science\": \"AC-feed-science\",             \"entertainment\": \"AC-feed-entertainment\",             \"music\": \"AC-feed-music\"           }         },         \"notification\": {           \"categoryToPlacement\": {             \"sport\": \"AC-notification-sports\",             \"news\": \"AC-notification-news\",             \"business\": \"AC-notification-business\",             \"tech\": \"AC-notification-tech\",             \"science\": \"AC-notification-science\",             \"entertainment\": \"AC-notification-entertainment\",             \"music\": \"AC-notification-music\"           }         }       }     },     \"NotificationsManagerConfig\": {       \"refreshInterval\": 3,       \"itemsBatchSize\": 10,       \"switchContentInterval\": 1,       \"notificationCategories\": [         \"news\",         \"entertainment\",         \"tech\"       ]     }   } }";

            AppSettings appSettings = new AppSettings(false, categories, CONFIG);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(APP_SETTINGS_KEY, new Gson().toJson(appSettings));
            editor.apply();

            return appSettings;
        }
    }

    private void updateAppSettings() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_SETTINGS_KEY, new Gson().toJson(appSettings));
        editor.apply();
    }

    private static String addPlacementToCategoryMapping(String taboolaConfig) {
        try {
            JSONObject taboolaConfigJson = new JSONObject(taboolaConfig);
            final JSONObject placementsMap = taboolaConfigJson.getJSONObject("TaboolaConfig")
                    .getJSONObject("ContentManagerConfig")
                    .getJSONObject("placementMap");

            final Iterator<String> placementNamesIterator = placementsMap.keys();
            while (placementNamesIterator.hasNext()) {
                JSONObject placementRootNode = placementsMap.getJSONObject(placementNamesIterator.next());
                JSONObject categoryToPlacement = placementRootNode.getJSONObject("categoryToPlacement");
                placementRootNode.put("placementToCategory", generatePlacementToCategoryMapping(categoryToPlacement));
            }

            return taboolaConfigJson.toString();
        } catch (JSONException e) {
            Log.e(TAG, "TBContentManager: invalid configJson", e);
            e.printStackTrace();
            return "";
        }
    }

    private static JSONObject generatePlacementToCategoryMapping(JSONObject categoryToPlacement) throws JSONException {
        final JSONObject placementToCategory = new JSONObject();

        final Iterator<String> categoryNamesIterator = categoryToPlacement.keys();
        while (categoryNamesIterator.hasNext()) {
            String categoryName = categoryNamesIterator.next();
            String categoryPlacement = categoryToPlacement.getString(categoryName);
            placementToCategory.put(categoryPlacement, categoryName);
        }
        return placementToCategory;
    }

    private void handleClickIntentIfNeeded(Intent intent) {
        Bundle extras = intent.getExtras();
        if ((intent.getAction() != null) &&
                intent.getAction().equals(TBNotificationManager.NOTIFICATION_CLICK_INTENT_ACTION) &&
                (extras != null)) {
            TBPlacement placement = extras.getParcelable(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_PLACEMENT);
            int itemIndex = extras.getInt(TBNotificationManager.NOTIFICATION_CLICK_INTENT_EXTRA_ITEM_INDEX);

            TBRecommendationItem item = placement.getItems().get(itemIndex);
            item.handleClick(this);
        }
    }
}
