package com.deng.johndon.gdutbookcrossing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DELL on 2017/3/16.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtUserName;
    private EditText mEtUserPassword;
    private TextView mTvGoToSign;
    private Button mBtnLogin;

    private GDUTUser mGDUTUser;

    @Override
    protected int getLayoutId() {
        return R.layout.user_login_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mEtUserName = (EditText) findViewById(R.id.et_login_user_name);
        mEtUserPassword = (EditText) findViewById(R.id.et_login_user_password);
        mTvGoToSign = (TextView) findViewById(R.id.tv_go_to_sign);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(this);
        mTvGoToSign.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitle.setText(getString(R.string.login_account));
        mGDUTUser = new GDUTUser();
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_go_to_sign:{
                Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.btn_login:{
                String userName = mEtUserName.getText().toString();
                String password = mEtUserPassword.getText().toString();
                if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)) {
                    showShortToast(getString(R.string.name_password_shoud_not_empte));
                } else {
                    mGDUTUser.setUsername(userName);
                    mGDUTUser.setPassword(password);
                    userLogin();
                }
                break;
            }
            case R.id.iv_bar_back:{
                finish();
                break;
            }

        }

    }

    private void userLogin() {
        mGDUTUser.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.login_successfully));
                        BmobUser.getCurrentUser(LoginActivity.this);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("message"+s+"code"+i);
                    }
                });
            }
        });
    }
}
