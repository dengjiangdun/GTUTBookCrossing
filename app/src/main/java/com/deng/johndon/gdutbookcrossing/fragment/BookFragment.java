package com.deng.johndon.gdutbookcrossing.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Book;

import java.util.List;

/**
 * Created by DELL on 2017/2/25.
 */

public class BookFragment extends BaseFragment {
    private TextView mTvSearch;
    private SwipeRefreshLayout mSrlBook;
    private RecyclerView mRvBook;

    private List<Book> mBookList;

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
}
