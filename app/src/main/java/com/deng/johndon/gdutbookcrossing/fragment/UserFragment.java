package com.deng.johndon.gdutbookcrossing.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.activity.MyAddressActivity;
import com.deng.johndon.gdutbookcrossing.activity.ReleaseBookActivity;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.view.CircleImageView;

import cn.bmob.v3.BmobUser;

/**
 * Created by DELL on 2017/2/25.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener{
    private CircleImageView mCvUserIcon;
    private TextView mTvUserName;
    private TextView mTvLoginOut;
    private TextView mTvRelease;
    private TextView mTvOrder;
    private TextView mTvAddress;

    private GDUTUser mGDUTUser;
    private final static String NAME_FORMAT = "用户名：%s";
    @Override
    protected int getLayoutId() {
        return R.layout.user_fragment_layout;
    }

    @Override
    protected void initData() {
        mGDUTUser = BmobUser.getCurrentUser(getActivity(),GDUTUser.class);
        if (mGDUTUser != null){
            if (!TextUtils.isEmpty(mGDUTUser.getAvatar())){
                Glide.with(this).load(mGDUTUser.getAvatar()).into(mCvUserIcon);
            }
            mTvUserName.setText(String.format(NAME_FORMAT,mGDUTUser.getUsername()));
            mTvLoginOut.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void initlistener() {
        mCvUserIcon.setOnClickListener(this);
        mTvLoginOut.setOnClickListener(this);
        mTvRelease.setOnClickListener(this);
        mTvOrder.setOnClickListener(this);
        mTvAddress.setOnClickListener(this);
    }

    @Override
    protected void initView(View view) {
        mCvUserIcon = (CircleImageView) view.findViewById(R.id.cv_user_icon);
        mTvUserName = (TextView) view.findViewById(R.id.tv_username);
        mTvLoginOut = (TextView) view.findViewById(R.id.tv_login_out);
        mTvRelease = (TextView) view.findViewById(R.id.tv_my_release_book);
        mTvOrder = (TextView) view.findViewById(R.id.tv_my_order_book);
        mTvAddress = (TextView) view.findViewById(R.id.tv_address);
    }

    @Override
    public void onClick(View view) {
        if ( !checkLogin()){
            goToLogin();
            return;
        }
        switch (view.getId()){
            case R.id.cv_user_icon:{
                break;
            }

            case R.id.tv_login_out:{
                break;
            }

            case R.id.tv_my_release_book:{
                Intent intent = new Intent(getActivity(), ReleaseBookActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_my_order_book:{
                break;
            }

            case R.id.tv_address:{
                Intent intent = new Intent(getActivity(), MyAddressActivity.class);
                getActivity().startActivity(intent);
                break;
            }

            default:break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
