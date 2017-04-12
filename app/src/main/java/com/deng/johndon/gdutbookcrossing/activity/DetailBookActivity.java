package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.adapter.CommentAdapter;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.model.Comment;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by DELL on 2017/4/8.
 */

public class DetailBookActivity extends BaseActivity implements CommentAdapter.AddCommentListener {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvBookAvater;
    private TextView mTvBookName;
    private TextView mTvBookISBN;
    private TextView mTvBookDetail;
    private RecyclerView mRvComment;
    private TextView mTvBookPrice;
    private TextView mTvOrderBook;

    private static final String KEY_GET_BOOK_OBJECTID = "book_object_id";
    private static final String KEY_GET_BOOK_NUM = "book_object_num";
    private static final String STRING_FORMAT_BOOK_NAME = "书名: %s";
    private static final String STRING_FORMAT_BOOK_ISBN = "ISBN: %s";
    private static final String KEG_GET_COMMENT = "comment";
    private static final int GET_COMMENT_REQUEST = 0x11;
    private CommentAdapter mCommentAdapter;
    private String TAG = "book_detail";
    private Book mBook;
    private List<Comment> mCommentList;
    private GDUTUser mGDUTUser;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_book;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mIvBack = (ImageView) findViewById(R.id.iv_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_bar_title);
        mIvBookAvater = (ImageView) findViewById(R.id.iv_book_pic);
        mTvBookName = (TextView) findViewById(R.id.tv_book_name);
        mTvBookISBN = (TextView) findViewById(R.id.tv_book_isbn);
        mTvBookDetail = (TextView) findViewById(R.id.tv_book_detail);
        mRvComment = (RecyclerView) findViewById(R.id.rv_comment);
        mTvBookPrice = (TextView) findViewById(R.id.tv_book_price);
        mTvOrderBook = (TextView) findViewById(R.id.tv_order_book);
    }

    @Override
    protected void initData() {
        super.initData();
        mBook = new Book();
        String objectId = getIntent().getStringExtra(KEY_GET_BOOK_OBJECTID);
        BmobQuery<Book> bookBmobQuery =  new BmobQuery<>();
        mBook.setObjectId(objectId);
        bookBmobQuery.include("owner");
        bookBmobQuery.addWhereEqualTo("objectId",objectId);
        bookBmobQuery.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(final List<Book> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size()==1) {
                            mBook = list.get(0);
                            initBook();
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        mCommentList = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(this,this,mCommentList);
        mRvComment.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRvComment.setLayoutManager(fullyLinearLayoutManager);
        mTvTitle.setText(getString(R.string.book_detal_title));
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.include("gdutUser[username|avatar]");
        commentBmobQuery.addWhereEqualTo("book",new BmobPointer(mBook));
        commentBmobQuery.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(final List<Comment> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size() >0) {
                            mCommentList.addAll(list);
                        }
                        Log.d("TAG", "run: "+list.size());
                        mRvComment.setAdapter(mCommentAdapter);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        mGDUTUser = BmobUser.getCurrentUser(this,GDUTUser.class);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailBookActivity.this.finish();
            }
        });
        mTvOrderBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBookActivity.this);
                builder.setTitle("确定预定？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        updateBook();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }

        });
    }

    private void updateBook(){
        String objectId = mBook.getOwner().getObjectId();
        if (checkLogin()) {
            if (objectId != null && objectId != mGDUTUser.getObjectId()){
                mBook.setState("1");
                mBook.setBuyer(mGDUTUser);
                updateBook();
            } else {
                showShortToast("不能购买自己的书");
            }

        }
        mBook.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("预定成功");
                    }
                });
            }

            @Override
            public void onFailure(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("预定失败：原因"+s);
                    }
                });
            }
        });
    }


    private void initBook() {
        int num = mBook.getHasRead() + 1;
        mBook.setHasRead(num);
        mBook.update(this);
        initBookView();
    }

    private void initBookView() {
        Glide.with(this).load(mBook.getAvatar()).into(mIvBookAvater);
        mTvBookName.setText(String.format(STRING_FORMAT_BOOK_NAME,mBook.getName()));
        mTvBookISBN.setText(String.format(STRING_FORMAT_BOOK_ISBN,mBook.getISBN()));
        mTvBookDetail.setText(mBook.getDescription());
        mTvBookPrice.setText("价格："+mBook.getPrice()+"元");
        if (mBook.getState().equals("1")){
            mTvOrderBook.setText("已经被预定");
            mTvOrderBook.setClickable(false);
        } else if (mBook.getState().equals("2")){
            mTvOrderBook.setText("已经被购买");
            mTvOrderBook.setClickable(false);
        } else {

        }
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(this,AddCommentActivity.class);
        startActivityForResult(intent,GET_COMMENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GET_COMMENT_REQUEST) {
                Comment comment = new Comment();
                String strComment = data.getStringExtra(KEG_GET_COMMENT);
                comment.setGdutUser(BmobUser.getCurrentUser(this, GDUTUser.class));
                comment.setTime(String.valueOf(System.currentTimeMillis()));
                comment.setBook(mBook);
                comment.setContent(strComment);
                mCommentList.add(comment);
                mCommentAdapter.notifyDataSetChanged();
                comment.save(this);
            }
        }
    }
}
