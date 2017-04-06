package com.deng.johndon.gdutbookcrossing.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;

/**
 * Created by DELL on 2016/12/8.
 */

public class GDUTProgressDialog extends ProgressDialog {
    private static GDUTProgressDialog myProgressDialog;
    private Context mContext;
    private TextView mTvMessage;
    private ImageView mIv;
    public GDUTProgressDialog(Context context) {
        super(context,R.style.NewDialog);
        this.mContext=context;
    }

    public GDUTProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         setContentView(R.layout.progress_layout);
        myProgressDialog=new GDUTProgressDialog(mContext);
        mTvMessage= (TextView) findViewById(R.id.tv_message);
        mIv= (ImageView) findViewById(R.id.iv_progress);
        AnimationDrawable animationDrawable= (AnimationDrawable) mIv.getBackground();
        animationDrawable.start();
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mTvMessage!=null){
            mTvMessage.setText(message);
        }
    }

    public static GDUTProgressDialog show(Context context) {
        //myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.show();
        return myProgressDialog;
    }

    public static void dismissDialog() {
        myProgressDialog.dismiss();
    }
}
