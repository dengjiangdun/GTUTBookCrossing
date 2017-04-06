package com.deng.johndon.gdutbookcrossing.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.CycleViewPageAdapter;
import com.deng.johndon.gdutbookcrossing.model.Ad;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DELL on 2017/2/18.
 */

public class CycleView extends FrameLayout{

    private int count;
    private Context mContext;
    private List<ImageView> mIvPictures;
    private List<ImageView> mIvDot;
    private ViewPager mVp;
    private LinearLayout line;
    private boolean isAutoPlay;
    private int currentItem = 0;
    private Timer mTimer =new Timer();
    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mIvDot.get(currentItem).setImageResource(R.drawable.icon_point);
            currentItem = (currentItem+1)%count;
            mIvDot.get(currentItem).setImageResource(R.drawable.icon_point_pre);
            mVp.setCurrentItem(currentItem);
        }
    };

    public CycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CycleView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init(){
        initData();

    }

    private void initData(){
        mIvPictures = new ArrayList<>();
        mIvDot = new ArrayList<>();
    }

    public void setImageURL(List<Ad> imges ){
        initLayout();
        initNet(imges);
        showTime();
        setPageChangeListner();
    }

    private void initLayout(){
        mIvPictures.clear();
        View view = LayoutInflater.from(mContext).inflate(R.layout.cycle_layout,this,true);
        mVp = (ViewPager) view.findViewById(R.id.vp_cycle);
        line = (LinearLayout) view.findViewById(R.id.ll_dot);
        line.removeAllViews();
    }

    private void setPageChangeListner(){
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIvDot.get(currentItem).setImageResource(R.drawable.icon_point);
                currentItem=position;
                mIvDot.get(currentItem).setImageResource(R.drawable.icon_point_pre);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initNet(List<Ad> images){
        count = images.size();
        for (int i=0;i<count;++i){
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams layoutParams =new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            line.addView(imageView,layoutParams);
            imageView.setImageResource(R.drawable.icon_point);
            mIvDot.add(imageView);
        }
        mIvDot.get(0).setBackgroundResource(R.drawable.icon_point_pre);
        for (int i=0;i<count;++i){
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(images.get(i).getUrl(),imageView);
            mIvPictures.add(imageView);
        }
    }

    private void showTime(){
        mVp.setAdapter(new CycleViewPageAdapter(mIvPictures));
        mVp.setCurrentItem(0);
        currentItem = 0;
        playAuto();

    }

    private void playAuto(){
        isAutoPlay =true;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(new Message());
            }
        },3000,3000);
    }





}
