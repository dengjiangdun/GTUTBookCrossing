package com.deng.johndon.gdutbookcrossing.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deng.johndon.gdutbookcrossing.activity.LoginActivity;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by DELL on 2017/2/25.
 */

abstract  public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        initView(view);
        initlistener();
        initData();
        return view;
    }

    abstract protected int getLayoutId();

    abstract protected void initData();
    abstract protected void initlistener();

    abstract protected void initView(View view);

    protected boolean checkLogin(){
        GDUTUser gdutUser = BmobUser.getCurrentUser(getActivity(),GDUTUser.class);
       // goToLogin();
        return gdutUser != null;
    }

    protected void goToLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
    }


}
