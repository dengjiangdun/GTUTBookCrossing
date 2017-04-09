package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Academy;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.jar.Manifest;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by DELL on 2017/3/5.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView mCiAvatar;
    private EditText mEtUserName;
    private EditText mEtUserPassword;
    private EditText mEtUserPhone;
    private EditText mEtUserEmail;
    private TextView mTvUserGender;
    private TextView mTvUserAcademy;
    private Button mBtnUserSign;


    private List<Academy> mAcademyList;
    private static final int GET_AVATAR_REQUEST_CODE = 0x1111;
    private static final int PHOTORESOULT = 0x1112;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private File tempFile;
    private String headLocalFilePath ;
    private Uri imageUri;// 图片本地路径
    private static final int REQUEST_ACADEMY_CODE = 0x1113;
    private  static String[] GENDER_LIST = new String[]{"male","female"};
    private int index = -1;
    private BmobFile mBmobFile;
    private boolean isUpload = false;
    private GDUTUser mGDUTUser;
    private static final String KEY_GET_OBJECTID = "objectid";
    private static final String KEY_GET_ACADEMY = "academy";


    @Override
    protected int getLayoutId() {
        return R.layout.sign_in_activity_layout;
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        mCiAvatar = (CircleImageView) findViewById(R.id.ci_user_avatar);
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtUserPassword = (EditText) findViewById(R.id.et_user_password);
        mEtUserPhone = (EditText) findViewById(R.id.et_user_phone);
        mEtUserEmail = (EditText) findViewById(R.id.et_user_email);
        mTvUserAcademy = (TextView) findViewById(R.id.tv_user_academy);
        mTvUserGender = (TextView) findViewById(R.id.tv_user_gender);
        mBtnUserSign = (Button) findViewById(R.id.btn_user_sign_in);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnUserSign.setOnClickListener(this);
        mCiAvatar.setOnClickListener(this);
        mTvUserAcademy.setOnClickListener(this);
        mTvUserGender.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mGDUTUser = new GDUTUser();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_user_sign_in:{
                if (mEtUserName.getText().toString() == null ||mEtUserPassword.getText().toString() == null){
                    showShortToast(getString(R.string.name_password_shoud_not_empte));
                    return;
                }
               if (isUpload){
                   showProgressDialog();
                   BmobFile.uploadBatch(SignInActivity.this, new String[]{headLocalFilePath}, new UploadBatchListener() {
                       @Override
                       public void onSuccess(List<BmobFile> list, final List<String> list1) {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   mGDUTUser.setAvatar(list1.get(0));
                                   userSigin();
                               }
                           });
                       }

                       @Override
                       public void onProgress(int i, int i1, int i2, int i3) {

                       }

                       @Override
                       public void onError(int i, final String s) {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   showShortToast("出错："+s);
                                   disProgressDialog();
                               }
                           });
                       }
                   });


               } else {
                   userSigin();
               }
                break;
            }

            case R.id.ci_user_avatar:{
                checkPermition();
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("crop",true);
                intent.setType("image/*");
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("outputX",50);
                intent.putExtra("outputY",50);
                intent.putExtra("return-data", false);
                tempFile=new File("/sdcard/ll1x/"+ Calendar.getInstance().getTimeInMillis()+".jpg"); // 以时间秒为文件名
                File temp = new File("/sdcard/ll1x/");//自已项目 文件夹
                if (!temp.exists()) {
                    temp.mkdir();
                }
                intent.putExtra("output", Uri.fromFile(tempFile));  // 专入目标文件
                intent.putExtra("outputFormat", "JPEG"); //输入文件格式

                Intent wrapperIntent = Intent.createChooser(intent, "先择图片"); //开始 并设置标题
                //startActivityForResult(wrapperIntent, 1);
                startActivityForResult(wrapperIntent,GET_AVATAR_REQUEST_CODE);*/
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent,GET_AVATAR_REQUEST_CODE);
                break;
            }

            case R.id.tv_user_academy:{
                Intent intent = new Intent(this,ChooseAcademyActivity.class);
                startActivityForResult(intent,REQUEST_ACADEMY_CODE);
                break;
            }

            case R.id.tv_user_gender:{
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle(getString(R.string.choose_gender))
                        .setItems(GENDER_LIST, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                index = i;
                                mTvUserGender.setText(GENDER_LIST[i]);
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ){
            if (requestCode == GET_AVATAR_REQUEST_CODE){
               /* refreshImgString();
                //startPhotoZoom(data.getData());
                Log.d("TAG", "onActivityResult: "+data.getData());
                try {
                    InputStream is = (new URL( data.getData().toString() )).openStream();
                    mCiAvatar.setImageBitmap(BitmapFactory.decodeStream(is));
                    isUpload = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    isUpload = false;
                }*/

                 //   Bitmap bitmap = BitmapFactory.decodeStream( is );
                //mCiAvatar.setImageDrawable(Drawable.createFromPath(headLocalFilePath));
                //mCiAvatar.setImageBitmap(BitmapFactory.decodeFile(data.getData().toString()));
                imageUri = data.getData();
                refreshImgString();
                Log.d("TAGSign", "onActivityResult: "+imageUri+headLocalFilePath);
                isUpload = true;
                ImageLoader.getInstance().displayImage(String.valueOf(imageUri),mCiAvatar);
            } else if (requestCode == PHOTORESOULT){

            } else if (requestCode == REQUEST_ACADEMY_CODE){
                String academyName = data.getStringExtra(KEY_GET_ACADEMY);
                String objectId = data.getStringExtra(KEY_GET_OBJECTID);
                mTvUserAcademy.setText(academyName);
                Academy academy = new Academy();
                academy.setName(academyName);
                academy.setObjectId(objectId);
                mGDUTUser.setAcademy(academy);
            }
        }
    }

    private void checkPermition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                       // 检查该权限是否已经获取
                       int i = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                       if (i != PackageManager.PERMISSION_GRANTED) {
                                 // 如果没有授予该权限，就去提示用户请求
                           ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                    }
    }
    private void refreshImgString() {
        /*headLocalFilePath = Environment.getExternalStorageDirectory() + "/"
                + "GDUTBook/" + System.currentTimeMillis() + ".jpg";
        imageUri = Uri.fromFile(new File(headLocalFilePath));*/
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(imageUri, proj, // Which
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                headLocalFilePath  = cursor.getString(column_index);
            }
        }
    }

    private void userSigin(){
        mGDUTUser.setUsername(mEtUserName.getText().toString().trim());
        mGDUTUser.setPassword(mEtUserPassword.getText().toString().trim());
        String phone = mEtUserPhone.getText().toString().trim();
        String email = mEtUserEmail.getText().toString();
        mGDUTUser.setMobilePhoneNumber(phone);
        mGDUTUser.setEmail(email);
        Log.d("TAG", "userSigin: "+phone);
        if (index != -1){
            mGDUTUser.setGender(GENDER_LIST[index]);
        }

        mGDUTUser.signUp(SignInActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast(getString(R.string.sign_in_successfully));
                        disProgressDialog();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("message"+s+i);
                        disProgressDialog();
                    }
                });
            }
        });
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTORESOULT);
    }


}
