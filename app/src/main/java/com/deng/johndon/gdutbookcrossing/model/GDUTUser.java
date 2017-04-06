package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by DELL on 2017/2/16.
 */

public class GDUTUser extends BmobUser{
    private String gender;
    private String avatar;
    private Academy academy;

    public Academy getAcademy() {
        return academy;
    }

    public void setAcademy(Academy academy) {
        this.academy = academy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
