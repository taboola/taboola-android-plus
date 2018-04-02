package notifications.samples.taboola.com.taboolasamplenotificaions;

import java.io.Serializable;
import java.util.List;

public class AppSettings implements Serializable {

    private boolean allowNotifications;
    private List<Category> categories;

    public AppSettings(boolean allowNotifications, List<Category> categories) {
        this.allowNotifications = allowNotifications;
        this.categories = categories;
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
}
