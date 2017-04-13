package com.deng.johndon.gdutbookcrossing.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Book;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/2/25.
 */

public class BookFragment extends BaseFragment {
    private TextView mTvSearch;
    private SwipeRefreshLayout mSrlBook;
    private RecyclerView mRvBook;

    private List<Book> mBookList;
    private boolean noMore = false;
    private int mPage = 0;
    private static final  int AMOUT_PER_PAGE = 5;
    @Override
    protected int getLayoutId() {
        return R.layout.book_fragment_layout;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initlistener() {

    }

    @Override
    protected void initView(View view) {
        mTvSearch = (TextView) view.findViewById(R.id.tv_search_book);
        mSrlBook = (SwipeRefreshLayout) view.findViewById(R.id.srl_book);
        mRvBook = (RecyclerView) view.findViewById(R.id.rv_book);
    }

    private void getDataBook() {
        mPage = 0;
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.order("-createdAt");
        bookBmobQuery.setLimit(AMOUT_PER_PAGE);
        bookBmobQuery.setLimit(mPage);
        mPage++;
        bookBmobQuery.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
