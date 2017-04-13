package com.deng.johndon.gdutbookcrossing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.fragment.BaseFragment;
import com.deng.johndon.gdutbookcrossing.fragment.BookFragment;
import com.deng.johndon.gdutbookcrossing.fragment.MessageFragment;
import com.deng.johndon.gdutbookcrossing.fragment.RecommendFragment;
import com.deng.johndon.gdutbookcrossing.fragment.UserFragment;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.model.UserInstallation;
import com.deng.johndon.gdutbookcrossing.view.CycleView;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    private CycleView mCvPictures;
    private TextView[] mTv = new TextView[4];
    private ImageView mIv[] = new ImageView[4];
    private int currentIndex=0;
    private int lastIndex=0;
    private BaseFragment[] mGDUTFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_bar_back).setVisibility(View.INVISIBLE);
        mTv[0] = (TextView) findViewById(R.id.tv_recommend);
        mTv[1] = (TextView) findViewById(R.id.tv_goods);
        mTv[2] = (TextView) findViewById(R.id.tv_message);
        mTv[3] = (TextView) findViewById(R.id.tv_profile);
        mIv[0] = (ImageView) findViewById(R.id.iv_recommend);
        mIv[1] = (ImageView) findViewById(R.id.iv_goods);
        mIv[2] = (ImageView) findViewById(R.id.iv_message);
        mIv[3] = (ImageView) findViewById(R.id.iv_profile);
        mIv[0].setSelected(true);
        mTv[0].setTextColor(getResources().getColor(R.color.main_color));
        for (int i = 1;i<4;++i){
            mIv[i].setSelected(false);
            mTv[i].setTextColor(0xFF999999);
        }
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {
        mGDUTFragment = new BaseFragment[4];
        mGDUTFragment[0] = new RecommendFragment();
        mGDUTFragment[1] = new BookFragment();
        mGDUTFragment[2] = new MessageFragment();
        mGDUTFragment[3] = new UserFragment();
        getFragmentManager().beginTransaction().
                add(R.id.rl_container,mGDUTFragment[0]).
                add(R.id.rl_container,mGDUTFragment[1]).
                add(R.id.rl_container,mGDUTFragment[2]).
                add(R.id.rl_container,mGDUTFragment[3]).
                hide(mGDUTFragment[1]).
                hide(mGDUTFragment[2]).
                hide(mGDUTFragment[3]).commit();
        saveInstalltion();
    }

    public void onTabClicked(View view){
        lastIndex=currentIndex;
        switch (view.getId()){


            case R.id.rl_recommend:{
                currentIndex =0;
                break;
            }

            case  R.id.rl_good:{
                currentIndex =1;
                break;
            }
            case R.id.rl_add_good:{
                if (checkLogin()) {
                    Intent intent = new Intent(MainActivity.this,AddBookActivity.class);
                    startActivity(intent);
                } else {
                    goToLogin();
                }
                break;
            }

            case R.id.rl_message:{
                currentIndex =2;
                break;
            }

            case R.id.rl_user:{
                currentIndex =3;
                break;
            }
        }
        getFragmentManager().beginTransaction().
                hide(mGDUTFragment[lastIndex]).
                show(mGDUTFragment[currentIndex]).commit();
        mIv[lastIndex].setSelected(false);
        mTv[lastIndex].setTextColor(0xFF999999);
        mIv[currentIndex].setSelected(true);
        mTv[currentIndex].setTextColor(getResources().getColor(R.color.main_color));
        GDUTUser gdutUser = new GDUTUser();
    }

    private void  saveInstalltion(){
        final GDUTUser gdutUser = BmobUser.getCurrentUser(this,GDUTUser.class);
        if (gdutUser != null){
            gdutUser.setInstallation(BmobInstallation.getCurrentInstallation(this).getInstallationId());
            gdutUser.update(this);
        }
    }


}