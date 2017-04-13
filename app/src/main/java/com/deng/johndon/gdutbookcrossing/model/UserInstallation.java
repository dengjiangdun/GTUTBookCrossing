package com.deng.johndon.gdutbookcrossing.model;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by DELL on 2017/4/11.
 */

public class UserInstallation extends BmobInstallation {
    private GDUTUser gdutUser;

    public GDUTUser getGdutUser() {
        return gdutUser;
    }

    public void setGdutUser(GDUTUser gdutUser) {
        this.gdutUser = gdutUser;
    }

    public UserInstallation(Context context) {
        super(context);
    }

}
