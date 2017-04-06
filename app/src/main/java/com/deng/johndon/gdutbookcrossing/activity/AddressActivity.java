package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.AddressAdapter;
import com.deng.johndon.gdutbookcrossing.model.Address;
import com.deng.johndon.gdutbookcrossing.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/3/19.
 */

public class AddressActivity extends BaseActivity implements AddressAdapter.GetAddressItemListener{
    private TextView mTvTitle;
    private RecyclerView mRvAddress;
    private ImageView mIvAddAddress;

    private List<Address> mAddressList;
    private AddressAdapter mAddressAdapter;
    private static final String KEY_ADDRESS_OBJECTID = "address_object_id";
    private static final String KEY_ADDRESS_NAME = "address_name";

    @Override
    protected int getLayoutId() {
        return R.layout.address_activity_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mRvAddress = (RecyclerView) findViewById(R.id.rv_addresses);
        mIvAddAddress = (ImageView) findViewById(R.id.iv_top_bar_right_icon);
        mIvAddAddress.setVisibility(View.VISIBLE);
        mIvAddAddress.setImageResource(R.drawable.add_address);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressActivity.this.finish();
            }
        });

        mRvAddress.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvAddress.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        mRvAddress.addItemDecoration(dividerItemDecoration);
        mIvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTitle.setText(getString(R.string.choose_a_address));
        mAddressList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(this,this,mAddressList);
        mRvAddress.setAdapter(mAddressAdapter);
        getAddresses();
    }


    private void getAddresses() {
        showProgressDialog();
        BmobQuery<Address> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(final List<Address> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAddressList.clear();
                        mAddressList.addAll(list);
                        mAddressAdapter.notifyDataSetChanged();
                        Log.d("TAG", "run: "+mAddressList.size());
                        disProgressDialog();

                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        showShortToast(getString(R.string.is_something_error)+s+i);
                    }
                });

            }
        });
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    public void getAddressOnClickItem(int position) {
        Intent intent = new Intent();
        Address address = mAddressList.get(position);
        intent.putExtra(KEY_ADDRESS_OBJECTID,address.getObjectId());
        intent.putExtra(KEY_ADDRESS_NAME,address.getPlace());
        Log.d("TAG", "onActivityResult:address "+address.getPlace()+address.getObjectId());
        setResult(Activity.RESULT_OK,intent);
        AddressActivity.this.finish();

    }
}
