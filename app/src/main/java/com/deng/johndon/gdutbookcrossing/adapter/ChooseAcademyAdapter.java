package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Academy;

import java.util.List;

/**
 * Created by DELL on 2017/3/12.
 */

public class ChooseAcademyAdapter extends BaseAdapter{
    private Context mContext;
    private List<Academy> mAcademyList;

    public ChooseAcademyAdapter(Context context, List<Academy> list){
        mContext = context;
        mAcademyList = list;
    }


    @Override
    public int getCount() {
        return mAcademyList == null ? 0 : mAcademyList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAcademyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AcademyViewHolder academyViewHolder;
        if (view == null){
            academyViewHolder = new AcademyViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.academy_item_layout,null);
            academyViewHolder.mTv = (TextView) view.findViewById(R.id.tv_choose_academy_item);
            view.setTag(academyViewHolder);
        } else {
            academyViewHolder = (AcademyViewHolder) view.getTag();
        }
        academyViewHolder.mTv.setText(mAcademyList.get(i).getName());
        return view;
    }

    public class AcademyViewHolder{
        TextView mTv;
    }

}
