package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.ChooseAcademyAdapter;
import com.deng.johndon.gdutbookcrossing.model.Academy;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/3/12.
 */

public class ChooseAcademyActivity extends BaseActivity {
    private ListView mLvAcademy;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private List<Academy> mAcademyList;

    private static final String KEY_GET_OBJECTID = "objectid";
    private static final String KEY_GET_ACADEMY = "academy";

    private ChooseAcademyAdapter mChooseAcademyAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.choose_academy_activity;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mLvAcademy = (ListView) findViewById(R.id.lv_academy);
        mTvTitle.setText(getString(R.string.choose_academy_title));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseAcademyActivity.this.finish();
            }
        });

        mLvAcademy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChooseAcademyActivity.this,SignInActivity.class);
                intent.putExtra(KEY_GET_OBJECTID,mAcademyList.get(i).getObjectId());
                intent.putExtra(KEY_GET_ACADEMY,mAcademyList.get(i).getName());
                setResult(Activity.RESULT_OK,intent);
                ChooseAcademyActivity.this.finish();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        BmobQuery<Academy> query = new BmobQuery<>();
        showProgressDialog();
        query.findObjects(this, new FindListener<Academy>() {
            @Override
            public void onSuccess(final List<Academy> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       setAcademyData( list);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void setAcademyData(List<Academy> list){
        //disProgressDialog();
        mAcademyList = list;
        mChooseAcademyAdapter = new ChooseAcademyAdapter(this,list);
        mLvAcademy.setAdapter(mChooseAcademyAdapter);
    }

}
