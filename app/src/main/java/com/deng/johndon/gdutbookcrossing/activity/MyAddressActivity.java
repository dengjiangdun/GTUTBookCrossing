package com.deng.johndon.gdutbookcrossing.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.MyAddressAdapter;
import com.deng.johndon.gdutbookcrossing.model.Address;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/4/9.
 */

public class MyAddressActivity extends BaseActivity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvAdd;
    private SwipeRefreshLayout mSrl;
    private RecyclerView mRvAddress;

    private List<Address> mAddressList;
    private GDUTUser mGDUTUser;
    private int mPage = 0;
    private MyAddressAdapter mAddressAdpter;
    private Boolean noMore = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_address_layout;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mIvAdd = (ImageView) findViewById(R.id.iv_top_bar_right_icon);
        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl_address_layout);
        mRvAddress = (RecyclerView) findViewById(R.id.rv_my_address);
        mRvAddress.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvAddress.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void initData() {
        super.initData();
        mAddressList = new ArrayList<>();
        mAddressAdpter = new MyAddressAdapter(this,mAddressList);
        mRvAddress.setAdapter(mAddressAdpter);
        mGDUTUser = BmobUser.getCurrentUser(this,GDUTUser.class);
        getAddress();
    }

    private void getAddress(){
        BmobQuery<Address> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("gdutUser",mGDUTUser);
        bmobQuery.setLimit(5);
        bmobQuery.setSkip(mPage);
        mPage++;
        bmobQuery.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(final List<Address> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAddressList.clear();
                        mAddressList.addAll(list);
                        mAddressAdpter.notifyDataSetChanged();
                        mSrl.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }


    @Override
    protected void initListener() {
        super.initListener();
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddress();
                mPage = 0;
                noMore = false;

            }
        });

        mRvAddress.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAddressAdpter.getItemCount()) {
                    if (!noMore) {
                        mAddressAdpter.setLoadingMore();
                        loadMoreAddres();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                        findLastVisibleItemPosition();

            }
        });

    }

    private void loadMoreAddres(){
        BmobQuery<Address> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("gdutUser",mGDUTUser);
        bmobQuery.setLimit(5);
        bmobQuery.setSkip(mPage);
        mPage++;
        bmobQuery.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(final List<Address> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() != 5) {
                            noMore = true;
                            mAddressAdpter.addGoodItem(list);
                            mAddressAdpter.setNoMoreLoad();
                        } else {
                            mAddressAdpter.addGoodItem(list);
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

}
