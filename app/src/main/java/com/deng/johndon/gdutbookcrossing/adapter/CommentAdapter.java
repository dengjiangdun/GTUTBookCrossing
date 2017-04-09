package com.deng.johndon.gdutbookcrossing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Comment;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.util.TimeUtil;
import com.deng.johndon.gdutbookcrossing.view.CircleImageView;

import java.util.List;

/**
 * Created by DELL on 2017/4/8.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mCommentList;
    private Context mContext;
    private AddCommentListener mAddCommentListener;
    private static final String USER_COMMENT = "%s：%s";

    public CommentAdapter(Context context,AddCommentListener addCommentListener,List<Comment> commentList){
        this.mContext = context;
        this.mAddCommentListener = addCommentListener;
        this.mCommentList =commentList;
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentViewHolder commentViewHolder = new CommentViewHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.comment_item_layout,parent,false));
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (position == mCommentList.size()) {
            holder.mCvUser.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_address));
            holder.mTvTime.setVisibility(View.INVISIBLE);
            holder.mTvComment.setText(mContext.getString(R.string.add_comment));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAddCommentListener.onClick();
                }
            });
        } else {
            Comment comment = mCommentList.get(position);
            GDUTUser gdutUser = comment.getGdutUser();
            if (gdutUser != null){
                Glide.with(mContext).load(gdutUser.getAvatar())
                        .into(holder.mCvUser);
                Log.d("TAGUSER", "onBindViewHolder: "+gdutUser.getObjectId()+gdutUser.getCreatedAt());
                holder.mTvComment.setText(String.format(USER_COMMENT,
                        gdutUser.getUsername(),comment.getContent()));
            }


            holder.mTvTime.setText(TimeUtil.timeAgo(comment.getTime()));
        }

        Log.d("TAG", "onBindViewHolder: "+position);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size() + 1;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView mCvUser;
        private TextView mTvComment;
        private TextView mTvTime;
        private View view;
        public CommentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mCvUser = (CircleImageView) itemView.findViewById(R.id.cv_user_avatar);
            mTvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    public interface AddCommentListener{
        public void onClick();
    }


}
