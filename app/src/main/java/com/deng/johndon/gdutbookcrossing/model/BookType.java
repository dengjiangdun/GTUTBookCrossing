package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/3/5.
 */

public class BookType extends BmobObject {
    private String name;
    private String avatar;
    private String character;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
