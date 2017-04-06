package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/3/4.
 */

public class Address extends BmobObject {
    private String place;
    private String phone;
    private GDUTUser gdutUser;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GDUTUser getGdutUser() {
        return gdutUser;
    }

    public void setGdutUser(GDUTUser gdutUser) {
        this.gdutUser = gdutUser;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
