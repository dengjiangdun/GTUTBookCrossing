package com.deng.johndon.gdutbookcrossing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.activity.BaseActivity;
import com.deng.johndon.gdutbookcrossing.activity.DetailBookActivity;
import com.deng.johndon.gdutbookcrossing.activity.MainActivity;
import com.deng.johndon.gdutbookcrossing.adapter.BookAdapter;
import com.deng.johndon.gdutbookcrossing.adapter.HotBookAdapter;
import com.deng.johndon.gdutbookcrossing.model.Ad;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.view.CycleView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/2/16.
 */

public class RecommendFragment extends BaseFragment implements BookAdapter.OnClickBookItemListener{
    private CycleView mCycleViewPictures;
    private SwipeRefreshLayout mSrlHotBook;
    private RecyclerView mRvHotBook;
    private List<Ad> mAdList;
    private List<Book> mListBook;
    private HotBookAdapter mHotBookAdapter;

    private int mPage = 0;
    private Boolean noMore;
    private static final int AMOUNT_PER_PAGE = 5;
    private static final String KEY_GET_BOOK_OBJECTID = "book_object_id";
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
                Activity activity = getActivity();
                if (activity != null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCycleViewPictures.setImageURL(list);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        mListBook = new ArrayList<>();
        mHotBookAdapter = new HotBookAdapter(getActivity(),mListBook);
        mRvHotBook.setAdapter(mHotBookAdapter);
        getHotBook();
    }



    @Override
    protected void initlistener() {
        mSrlHotBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHotBook();
                mHotBookAdapter.setLoadingMore();
            }
        });

        mRvHotBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (lastVisibleItem + 1 == mHotBookAdapter.getItemCount()){
                    if (noMore == false){
                        loadNoreHotBook();
                        mHotBookAdapter.setLoadingMore();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastVisibleItem = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            }
        });

    }

    @Override
    protected void initView(View view) {
        mCycleViewPictures = (CycleView) view.findViewById(R.id.cv_picture);
        mSrlHotBook = (SwipeRefreshLayout) view.findViewById(R.id.srl_hot_book);
        mRvHotBook = (RecyclerView) view.findViewById(R.id.rv_hot_book);
        mRvHotBook.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mRvHotBook.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void clickItemPosition(int position) {
        Intent intent = new Intent(getActivity(), DetailBookActivity.class);
        intent.putExtra(KEY_GET_BOOK_OBJECTID,mListBook.get(position).getObjectId());
        getActivity().startActivity(intent);
    }

    private void  getHotBook(){
        noMore = false;
        mPage = 0;
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.setLimit(AMOUNT_PER_PAGE);
        bookBmobQuery.setSkip(mPage);
        mPage++;
        bookBmobQuery.order("-hasRead");
        bookBmobQuery.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListBook.clear();
                            mHotBookAdapter.addGoodItem(list);
                            mSrlHotBook.setRefreshing(false);
                            if (list.size() < 5){
                                mHotBookAdapter.setNoMoreLoad();
                                noMore = true;
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(int i, final String s) {
                final Activity activity = getActivity();
                if (activity != null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,activity.getString(R.string.is_something_error)+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    private void loadNoreHotBook(){
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.setLimit(AMOUNT_PER_PAGE);
        bookBmobQuery.setSkip(mPage);
        mPage++;
        bookBmobQuery.order("-hasRead");
        bookBmobQuery.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                Activity activity = getActivity();
                if (activity != null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListBook.addAll(list);
                            if (mListBook.size() == 20 || list.size()<5) {
                                noMore = true;
                                mHotBookAdapter.setNoMoreLoad();
                            } else {
                                mHotBookAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
