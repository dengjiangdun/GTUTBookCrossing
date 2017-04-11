package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Book;

import java.util.List;

/**
 * Created by DELL on 2017/4/9.
 */

public class HotBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Book> mBookList;

    private final static int TYPE_ITEM=0;
    private final static int TYPE_MORE=1;

    private final static int PULL_UP_LOAD_MORE=1;
    private final static int LOADING_MORE=2;
    private final static int NO_MORE_LOAD=3;
    private int mState = 1;
    public HotBookAdapter(Context context, List<Book> bookList) {
        mContext = context;
        mBookList = bookList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("TAG", "onCreateViewHolder: "+viewType);
        if (viewType == TYPE_ITEM){
            ReleaseViewHolder releaseViewHolder =new ReleaseViewHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.book_item_layout,parent,false));
            return releaseViewHolder;
        } else if (viewType == TYPE_MORE){
            FootHolder footHolder = new FootHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.foot_view_layout,parent,false));
            return footHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReleaseViewHolder) {
            ReleaseViewHolder releaseViewHolder = (ReleaseViewHolder) holder;
             Book book = mBookList.get(position);
            releaseViewHolder.tv.setText(book.getName());
            Glide.with(mContext).load(book.getAvatar()).override(200,100).into(releaseViewHolder.iv);
        }else if (holder instanceof FootHolder) {
            FootHolder footHolder = (FootHolder) holder;
            switch (mState){
                case PULL_UP_LOAD_MORE:
                    footHolder.mTv.setText("上拉加载更多");
                    footHolder.mPb.setVisibility(View.INVISIBLE);
                    break;
                case LOADING_MORE:
                    footHolder.mTv.setText("正在加载更多");
                    footHolder.mPb.setVisibility(View.VISIBLE);
                    break;
                case NO_MORE_LOAD:
                    footHolder.mTv.setText("没有更多加载");
                    footHolder.mPb.setVisibility(View.INVISIBLE);
                    break;
            }
        }


    }

    public void setState(int mState) {
        this.mState = mState;
        notifyDataSetChanged();
    }
    public void setLoadingMore(){
        mState= 2;
        notifyDataSetChanged();
    }

    public void addGoodItem(List<Book> books) {
        mBookList.addAll(books);
        mState = 1;
        notifyDataSetChanged();
    }

    public void setNoMoreLoad(){
        mState = 3;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()){
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: "+mBookList.size());
        return  mBookList.size()+1;
    }



    public  class FootHolder extends RecyclerView.ViewHolder{
        TextView mTv;
        ProgressBar mPb;
        public FootHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.tv_load_tip);
            mPb = (ProgressBar) itemView.findViewById(R.id.pb_load_tip);
        }
    }


    public class ReleaseViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        View view;
        public ReleaseViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            iv = (ImageView) itemView.findViewById(R.id.iv_book_item);
            tv = (TextView) itemView.findViewById(R.id.tv_book_item);
        }
    }

}
