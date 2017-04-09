package com.deng.johndon.gdutbookcrossing.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.ReleaseBookAdapter;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/4/9.
 */

public class ReleaseBookActivity extends BaseActivity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ReleaseBookAdapter mReleaseBookAdapter;
    private RecyclerView mRvBook;
    private SwipeRefreshLayout mSrl;

    private int mPage = 0;
    private boolean noMore = false;
    private List<Book> mBookList;
    private GDUTUser mGDUTBook;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_activity;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl_layout);
        mRvBook = (RecyclerView) findViewById(R.id.rv_book);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFresh();
            }
        });

        mRvBook.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == mReleaseBookAdapter.getItemCount()){
                    if ( !noMore){
                        mReleaseBookAdapter.setLoadingMore();
                        loadMoreBook();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).
                        findLastVisibleItemPosition();

            }
        });

    }

    private void  loadMoreBook() {
        mPage++;
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.addWhereEqualTo("owner",mGDUTBook);
        bookBmobQuery.include("buyer[username]");
        bookBmobQuery.setLimit(5);
        bookBmobQuery.setSkip(mPage);
        bookBmobQuery.findObjects(ReleaseBookActivity.this, new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                      runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null&& list.size() != 5){
                            mReleaseBookAdapter.addGoodItem(list);
                            mReleaseBookAdapter.setNoMoreLoad();
                            noMore = true;
                        }else {
                            mReleaseBookAdapter.addGoodItem(list);
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void reFresh(){
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.addWhereEqualTo("owner",mGDUTBook);
        bookBmobQuery.include("buyer[username]");
        bookBmobQuery.order("createdAt");
        bookBmobQuery.setLimit(5);
        bookBmobQuery.setSkip(0);
        mPage = 0;
        showProgressDialog();
        noMore = false;
        bookBmobQuery.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        mSrl.setRefreshing(false);
                        mBookList.clear();
                        mBookList.addAll(list);
                        mReleaseBookAdapter.notifyDataSetChanged();
                        Log.d("TAG", "run: "+list.size());
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        showShortToast("出错："+s);
                    }
                });
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        mTvTitle.setText(getString(R.string.my_release_title));
        mGDUTBook = BmobUser.getCurrentUser(this,GDUTUser.class);
        mBookList = new ArrayList<>();
        mReleaseBookAdapter = new ReleaseBookAdapter(this,mBookList);
        mRvBook.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBook.setLayoutManager(linearLayoutManager);
        mRvBook.setAdapter(mReleaseBookAdapter);
       reFresh();
    }
}
