package com.deng.johndon.gdutbookcrossing.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Address;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.util.CheckUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DELL on 2017/3/26.
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtAddress;
    private EditText mEtPhone;
    @Override
    protected int getLayoutId() {
        return R.layout.add_address_activity_layout;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mEtAddress = (EditText) findViewById(R.id.et_add_address);
        mEtPhone = (EditText) findViewById(R.id.et_add_phone);
        mTvTitle.setText(getString(R.string.add_address));
        mIvBack.setOnClickListener(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:{
                AddAddressActivity.this.finish();
                break;
            }

            case R.id.btn_save:{
                String address = mEtAddress.getText().toString();
                String phone = mEtPhone.getText().toString();
                if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(phone)){
                    if (CheckUtil.checkIsPhoneNumber(phone)){
                        saveAddress(address,phone);
                    } else {
                        showShortToast(getString(R.string.phone_error));
                    }
                } else {
                    showShortToast(getString(R.string.address_phone_is_empty));
                }
                break;
            }
        }
    }

    private void saveAddress(String address,String phone){
        Address addressObject = new Address();
        addressObject.setPlace(address);
        addressObject.setPhone(phone);
        addressObject.setGdutUser(BmobUser.getCurrentUser(this, GDUTUser.class));
        addressObject.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.save_done));
                    }
                });

            }

            @Override
            public void onFailure(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.is_something_error)+s+i);
                    }
                });
            }
        });
    }

}
