package com.deng.johndon.gdutbookcrossing.model;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by DELL on 2017/3/4.
 */

public class Devices extends BmobInstallation {
    private GDUTUser gdutUser;

    public Devices(Context context) {
        super(context);
    }

    public GDUTUser getGdutUser() {
        return gdutUser;
    }

    public void setGdutUser(GDUTUser gdutUser) {
        this.gdutUser = gdutUser;
    }
}
