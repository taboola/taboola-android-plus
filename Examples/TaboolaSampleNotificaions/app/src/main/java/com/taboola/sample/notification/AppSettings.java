package com.taboola.sample.notification;

import java.io.Serializable;
import java.util.List;

class AppSettings implements Serializable {

    private boolean allowNotifications;
    private List<Category> categories;

    AppSettings(boolean allowNotifications, List<Category> categories) {
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
