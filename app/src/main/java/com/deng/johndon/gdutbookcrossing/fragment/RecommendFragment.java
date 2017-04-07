package com.deng.johndon.gdutbookcrossing.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Ad;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.view.BookLoadMoreView;
import com.deng.johndon.gdutbookcrossing.view.CycleView;
import com.deng.johndon.gdutbookcrossing.view.DividerItemDecoration;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/2/16.
 */

public class RecommendFragment extends BaseFragment {
    private CycleView mCycleViewPictures;
    private PullToRefreshRecyclerView mPRRVBook;
    private List<Ad> mAdList;
    private List<Book> mListBook;
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

        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                
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
        mPRRVBook = (PullToRefreshRecyclerView) view.findViewById(R.id.pr_books);
        BookLoadMoreView bookLoadMoreView = new BookLoadMoreView(getActivity(),mPRRVBook.getRecyclerView());
        bookLoadMoreView.setLoadMorePadding(60);
        bookLoadMoreView.setLoadmoreString("Loading...");
        mPRRVBook.setLoadMoreFooter(bookLoadMoreView);
        mPRRVBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPRRVBook.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {

            }
        });

        mPRRVBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        mPRRVBook.getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));

        mPRRVBook.onFinishLoading(true,false);
    }
}
