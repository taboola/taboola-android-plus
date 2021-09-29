package com.taboola.sample.notification;

import java.io.Serializable;

class Category implements Serializable{

    private String id;
    private String name;
    private boolean isEnable;

    public Category(String id, String name, boolean isEnable) {
        this.id = id;
        this.name = name;
        this.isEnable = isEnable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
