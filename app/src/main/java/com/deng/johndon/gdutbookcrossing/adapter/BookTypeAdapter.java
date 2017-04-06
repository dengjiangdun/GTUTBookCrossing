package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.BookType;

import java.util.List;

/**
 * Created by DELL on 2017/3/30.
 */

public class BookTypeAdapter extends RecyclerView.Adapter<BookTypeAdapter.BookTypeHolder>{
    private List<BookType> mBookTypeList;
    private Context mContext;
    private OnGetBookTypeListner mOnGetBookTypeListner;

    public BookTypeAdapter(Context context,OnGetBookTypeListner onGetBookTypeListner, List<BookType> bookTypeList) {
        this.mContext = context;
        this.mBookTypeList = bookTypeList;
        this.mOnGetBookTypeListner = onGetBookTypeListner;
    }

    @Override
    public BookTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookTypeHolder bookTypeHolder = new BookTypeHolder(LayoutInflater.from(mContext).
                inflate(R.layout.book_type_item_layout,parent,false));
        return bookTypeHolder;
    }

    @Override
    public void onBindViewHolder(BookTypeHolder holder, final int position) {
        holder.mTvType.setText(mBookTypeList.get(position).getName());
        Log.d("TAG", "onBindViewHolder: "+mBookTypeList.size());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnGetBookTypeListner.onClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBookTypeList == null ? 0 : mBookTypeList.size();
    }

    public class BookTypeHolder extends RecyclerView.ViewHolder{
        TextView mTvType;
        View parent;
        public BookTypeHolder(View itemView) {
            super(itemView);
            parent = itemView;
            mTvType = (TextView) itemView.findViewById(R.id.tv_book_type);
        }
    }

    public interface OnGetBookTypeListner{
        public void onClickItem(int position);
    }


}
