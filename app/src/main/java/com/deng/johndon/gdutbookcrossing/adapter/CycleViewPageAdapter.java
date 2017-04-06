package com.deng.johndon.gdutbookcrossing.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by DELL on 2017/2/18.
 */

public class CycleViewPageAdapter extends PagerAdapter {
    private List<ImageView> mIvList;
    public CycleViewPageAdapter(List<ImageView> imageViews){
        this.mIvList = imageViews;
    }


    @Override
    public int getCount() {
        return mIvList==null?0:mIvList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mIvList.get(position));
        return mIvList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
