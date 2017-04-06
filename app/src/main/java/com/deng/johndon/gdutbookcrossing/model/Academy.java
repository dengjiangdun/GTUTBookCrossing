package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/3/4.
 */

public class Academy extends BmobObject {
    private String name;
    private String description;
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
