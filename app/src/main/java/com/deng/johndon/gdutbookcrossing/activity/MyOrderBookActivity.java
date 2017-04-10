package com.deng.johndon.gdutbookcrossing.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.OrderBookAdapter;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/4/10.
 */

public class MyOrderBookActivity extends BaseActivity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private SwipeRefreshLayout mSrl;
    private RecyclerView mRvOrderBook;

    private OrderBookAdapter mOrderBookAdapter;
    private List<Book> mOrderBookList;
    private GDUTUser mGDUTUser;
    private int mPage;
    private Boolean noMore = false;
    private static final int AMOUNT_PER_PAGE = 5;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order_book_layout;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl_my_order_book_layout);
        mRvOrderBook = (RecyclerView) findViewById(R.id.rv_my_order_book);
        mRvOrderBook.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvOrderBook.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void initData() {
        super.initData();
        mOrderBookList = new ArrayList<>();
        mOrderBookAdapter = new OrderBookAdapter(this,mOrderBookList);
        mRvOrderBook.setAdapter(mOrderBookAdapter);
        mTvTitle.setText(getString(R.string.my_order_book_page));
        mGDUTUser = BmobUser.getCurrentUser(this,GDUTUser.class);
        getOrderBook();
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
                getOrderBook();
            }
        });

        mRvOrderBook.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                        findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == mOrderBookAdapter.getItemCount()){
                    if ( noMore != true ) {
                        getMoreOrderBook();
                        mOrderBookAdapter.setLoadingMore();
                    }
                }
            }
        });
    }

    private void getOrderBook(){
        mPage = 0;
        noMore = false;
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.setLimit(AMOUNT_PER_PAGE);
        bookBmobQuery.setSkip(mPage);
        mPage++;
        bookBmobQuery.addWhereEqualTo("buyer",mGDUTUser);
        bookBmobQuery.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOrderBookList.clear();
                        mOrderBookList.addAll(list);
                        mSrl.setRefreshing(false);
                        if (list.size() < AMOUNT_PER_PAGE){
                            mOrderBookAdapter.setNoMoreLoad();
                            noMore = true;
                            return;
                        }
                        mOrderBookAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.it_is_wrong)+s);
                    }
                });
            }
        });
    }

    private void getMoreOrderBook(){
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.setLimit(AMOUNT_PER_PAGE);
        bookBmobQuery.setSkip(mPage);
        mPage++;
        bookBmobQuery.addWhereEqualTo("buyer",mGDUTUser);
        bookBmobQuery.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() < AMOUNT_PER_PAGE){
                            mOrderBookList.addAll(list);
                            mOrderBookAdapter.setNoMoreLoad();
                            noMore = true;
                            return;
                        }
                        mOrderBookAdapter.addGoodItem(list);
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.it_is_wrong)+s);
                    }
                });
            }
        });
    }


}
