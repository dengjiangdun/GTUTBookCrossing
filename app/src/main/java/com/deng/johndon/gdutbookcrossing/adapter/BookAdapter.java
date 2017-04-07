package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Book;

import java.util.List;

/**
 * Created by johndon on 4/7/17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> mListBook;
    private Context mContext;
    private OnClickBookItemListener mOnClickBookItemListener;
    String TAG = "BookAdapter";
    public BookAdapter(Context context,OnClickBookItemListener onClickBookItemListener,List<Book> bookList) {
        this.mContext = context;
        this.mListBook = bookList;
        this.mOnClickBookItemListener = onClickBookItemListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookViewHolder bookViewHolder = new BookViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book_item_layout,parent,false));
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.tv.setText(mListBook.get(position).getName());
        Glide.with(mContext).load(mListBook.get(position).getAvatar()).override(200,100).into(holder.iv);
        Log.d(TAG, "url"+mListBook.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return mListBook == null ? 0 : mListBook.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        public BookViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_book_item);
            tv = (TextView) itemView.findViewById(R.id.tv_book_item);
        }
    }


    public interface OnClickBookItemListener{
        public void clickItemPosition(int position);
    }


}
