package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;

/**
 * Created by DELL on 2017/4/8.
 */

public class AddCommentActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtInput;
    private Button mBtnSubmit;

    private static final String KEG_GET_COMMENT = "comment";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_comment_layout;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mEtInput = (EditText) findViewById(R.id.et_add_comment);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitle.setText("增加评论");
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back: {
                finish();
                break;
            }

            case R.id.btn_submit:{
                String comment = mEtInput.getText().toString();
                if (!TextUtils.isEmpty(comment)){
                    Intent intent = new Intent();
                    intent.putExtra(KEG_GET_COMMENT,comment);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                } else {
                    showShortToast(getString(R.string.comment_not_empty));
                }
                break;
            }
        }
    }
}
