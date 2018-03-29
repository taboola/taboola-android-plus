package notifications.samples.taboola.com.taboolasamplenotificaions;

import java.io.Serializable;

public class Category implements Serializable{

    private String id;
    private String name;
    private boolean isEnable;

    public Category(String id, String name, boolean isEnable) {
        this.id = id;
        this.name = name;
        this.isEnable = isEnable;
    }

    public Category() {

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
