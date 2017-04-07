package com.deng.johndon.gdutbookcrossing.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.BookAdapter;
import com.deng.johndon.gdutbookcrossing.model.Ad;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.view.BookLoadMoreView;
import com.deng.johndon.gdutbookcrossing.view.CycleView;
import com.deng.johndon.gdutbookcrossing.view.DividerItemDecoration;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/2/16.
 */

public class RecommendFragment extends BaseFragment implements BookAdapter.OnClickBookItemListener{
    private CycleView mCycleViewPictures;
    private PullToRefreshRecyclerView mPRRVBook;
    private List<Ad> mAdList;
    private List<Book> mListBook;
    private BookAdapter mBookAdapter;
    String TAG = "BookAdapter";
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
        mBookAdapter = new BookAdapter(getActivity(),this,mListBook);
        mPRRVBook.getRecyclerView().setAdapter(mBookAdapter);
        reFresh();
    }

    private void reFresh(){
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                Activity activity = getActivity();
                if (activity != null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListBook.clear();
                            mListBook.addAll(list);
                            mBookAdapter.notifyDataSetChanged();
                            Log.d(TAG, "run: "+list.size());
                            mPRRVBook.setOnRefreshComplete();
                            mPRRVBook.onFinishLoading(true,false);


                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    @Override
    protected void initlistener() {
        BookLoadMoreView bookLoadMoreView = new BookLoadMoreView(getActivity(),mPRRVBook.getRecyclerView());
        bookLoadMoreView.setLoadMorePadding(100);
        bookLoadMoreView.setLoadmoreString("Loading...");
        mPRRVBook.removeHeader();
        mPRRVBook.setSwipeEnable(true);
        mPRRVBook.setLoadMoreFooter(bookLoadMoreView);
        mPRRVBook.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mPRRVBook.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {

            }
        });

        mPRRVBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFresh();
            }
        });

        mPRRVBook.setLoadmoreString("loading...");
        mPRRVBook.onFinishLoading(true,false);
    }

    @Override
    protected void initView(View view) {
        mCycleViewPictures = (CycleView) view.findViewById(R.id.cv_picture);
        mPRRVBook = (PullToRefreshRecyclerView) view.findViewById(R.id.pr_books);

    }

    @Override
    public void clickItemPosition(int position) {

    }
}
