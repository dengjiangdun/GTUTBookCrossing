package com.deng.johndon.gdutbookcrossing.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by DELL on 2017/2/16.
 */

public class GDUTApplication extends Application {
    private static final String APP_ID="b73b2464582623573601490303bac5b5";
    private static GDUTApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,APP_ID);
        ImageLoaderConfiguration loaderConfiguration=new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480,800)
                .threadPoolSize(3)
                .memoryCacheSize(2*1024*1024)
                .diskCacheSize(5*1024*1024)
                .diskCacheFileCount(50)
                .imageDownloader(new BaseImageDownloader(this,5*1000,30*1000))
                .build();
        ImageLoader.getInstance().init(loaderConfiguration);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this);

    }

    public static GDUTApplication getInstance(){
        if (instance == null){
            instance = new GDUTApplication();
        }
        return instance;
    }

}
