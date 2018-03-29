package notifications.samples.taboola.com.taboolasamplenotificaions;

import java.io.Serializable;
import java.util.List;

public class AppSettings implements Serializable {

    private boolean allowNotifications;
    private List<Category> categories;
    private String config;

    public AppSettings(boolean allowNotifications, List<Category> categories, String config) {
        this.allowNotifications = allowNotifications;
        this.categories = categories;
        this.config = config;
    }

    public boolean isAllowNotifications() {
        return allowNotifications;
    }

    public void setAllowNotifications(boolean allowNotifications) {
        this.allowNotifications = allowNotifications;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
