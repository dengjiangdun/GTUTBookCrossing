package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.BookTypeAdapter;
import com.deng.johndon.gdutbookcrossing.model.BookType;
import com.deng.johndon.gdutbookcrossing.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/3/30.
 */

public class ChooseBookTypeActivity extends BaseActivity implements BookTypeAdapter.OnGetBookTypeListner {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private RecyclerView mRvBookType;

    private List<BookType> mBookTypeList;
    private BookTypeAdapter mBookTypeAdapter;
    private final static String KEY_GET_TYPE_ID = "get_type_id";
    private final static String KEY_GET_TYPE_NAME = "get_type_name";
    @Override
    protected int getLayoutId() {
        return R.layout.choose_book_type_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mRvBookType = (RecyclerView) findViewById(R.id.rv_book_type);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitle.setText(getResources().getString(R.string.choose_book_type));
        mRvBookType.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBookType.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        mRvBookType.addItemDecoration(dividerItemDecoration);
        mBookTypeList = new ArrayList<>();
        mBookTypeAdapter = new BookTypeAdapter(this,this,mBookTypeList);
        mRvBookType.setAdapter(mBookTypeAdapter);
        getBookTypes();

    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    private void getBookTypes() {
        showProgressDialog();
        BmobQuery<BookType> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<BookType>() {
            @Override
            public void onSuccess(final List<BookType> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBookTypeList.clear();
                        mBookTypeList.addAll(list);
                        mBookTypeAdapter.notifyDataSetChanged();
                        disProgressDialog();
                        Log.d("TAG", "run:done "+list.size());
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        Log.d("TAG", "run:fail ");
                    }
                });

            }
        });
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.putExtra(KEY_GET_TYPE_ID,mBookTypeList.get(position).getObjectId());
        intent.putExtra(KEY_GET_TYPE_NAME,mBookTypeList.get(position).getName());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
