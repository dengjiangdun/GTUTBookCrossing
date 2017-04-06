package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Address;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by DELL on 2017/3/24.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder>{
    private Context mContext;
    private List<Address> mAddressList;
    private GetAddressItemListener mGetAddressItemListener;

    public AddressAdapter(Context context,GetAddressItemListener getAddressItemListener,List<Address> addressList) {
        this.mContext = context;
        this.mAddressList = addressList;
        this.mGetAddressItemListener = getAddressItemListener;
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressHolder addressHolder = new AddressHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.address_item_layout,parent,false));
        return addressHolder;
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, final int position) {
        holder.tvAddress.setText(mAddressList.get(position).getPlace());
        holder.tvPhone.setText(mAddressList.get(position).getPhone());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetAddressItemListener.getAddressOnClickItem(position);
            }
        });
        Log.d("TAG", "onBindViewHolder: "+position+"size"+mAddressList.size());

    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "onBindViewHoldergetItemCount: "+mAddressList.size());
        return mAddressList.size() ;
    }

    public class AddressHolder extends RecyclerView.ViewHolder{
        private TextView tvAddress;
        private TextView tvPhone;
        private View view;
        public AddressHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);

        }
    }

    public interface GetAddressItemListener{
        public void getAddressOnClickItem( int  position );
    }


}
