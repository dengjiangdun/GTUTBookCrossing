package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.view.GDUTProgressDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by DELL on 2017/2/16.
 */

public abstract class BaseActivity extends Activity {
    private GDUTProgressDialog mGDUTProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onCreateGDUT(savedInstanceState);
        mGDUTProgressDialog = new GDUTProgressDialog(this);
        initView();
        initData();
        initListener();
    }

    protected void initView(){}

    protected void initListener(){}

    protected abstract int getLayoutId();


    protected abstract void onCreateGDUT(Bundle savedInstanceState);

    protected void showProgressDialog(){
        if (!mGDUTProgressDialog.isShowing()){
            mGDUTProgressDialog.show();
        }
    }

    protected void showProgressDialog(String message){
        if (!TextUtils.isEmpty(message)){
            mGDUTProgressDialog.setMessage(message);
        }
        if (!mGDUTProgressDialog.isShowing()){
            mGDUTProgressDialog.show();
        }
    }

    protected void disProgressDialog(){

        if (mGDUTProgressDialog.isShowing()){
            mGDUTProgressDialog.dismiss();
        }

    }

    protected boolean checkLogin(){
        GDUTUser gdutUser = BmobUser.getCurrentUser(this,GDUTUser.class);
        return gdutUser != null;
    }

    protected void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    protected void showShortToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    protected void initData(){}

}
