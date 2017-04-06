package com.deng.johndon.gdutbookcrossing.fragment;

import android.view.View;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Ad;
import com.deng.johndon.gdutbookcrossing.view.CycleView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/2/16.
 */

public class RecommendFragment extends BaseFragment {
    private CycleView mCycleViewPictures;
    private List<Ad> mAdList;
    @Override
    protected int getLayoutId() {
        return R.layout.recommend_fragment_layout;
    }

    @Override
    protected void initData() {
        BmobQuery<Ad> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("isUse","1");
        bmobQuery.findObjects(getActivity(), new FindListener<Ad>() {
            @Override
            public void onSuccess(final List<Ad> list) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCycleViewPictures.setImageURL(list);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    protected void initlistener() {

    }

    @Override
    protected void initView(View view) {
        mCycleViewPictures = (CycleView) view.findViewById(R.id.cv_picture);

    }
}
