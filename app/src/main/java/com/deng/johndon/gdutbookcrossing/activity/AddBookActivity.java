package com.deng.johndon.gdutbookcrossing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deng.johndon.gdutbookcrossing.R;
import com.deng.johndon.gdutbookcrossing.model.Address;
import com.deng.johndon.gdutbookcrossing.model.Book;
import com.deng.johndon.gdutbookcrossing.model.BookType;
import com.deng.johndon.gdutbookcrossing.model.GDUTUser;
import com.deng.johndon.gdutbookcrossing.util.CheckUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by DELL on 2017/3/19.
 */

public class AddBookActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mIvBookPicture;
    private EditText mEtBookName;
    private EditText mEtBookISBN;
    private TextView mTvBookType;
    private TextView mTvBookAddress;
    private EditText mEtBookPrice;
    private EditText mEtBookDescription;
    private Button mBtnSubmit;
    private static final String KEY_ADDRESS_OBJECTID = "address_object_id";
    private static final String KEY_ADDRESS_NAME = "address_name";
    private final static int GER_ADDRESS_REQUEST_CODE = 0x11;
    private final static String KEY_GET_TYPE_ID = "get_type_id";
    private final static String KEY_GET_TYPE_NAME = "get_type_name";
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private final static int GET_TYPE_REQUEST_CODE = 0x12;
    private final static int GET_BOOK_PICTURE_CODE = 0x13;
    private final static int PHOTORESOULT = 0x14;
    private String headLocalFilePath = Environment
            .getExternalStorageDirectory() + "/" + "GDUTBook/" +"headCrop.jpg";
    private Uri imageUri = Uri.fromFile(new File(headLocalFilePath)); // 图片本地路径
    private Address mAddress;
    private BookType mBookType;
    private File tempFile;
    @Override
    protected int getLayoutId() {
        return R.layout.add_book_activity_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvBookPicture = (ImageView) findViewById(R.id.iv_add_book_picture);
        mEtBookName = (EditText) findViewById(R.id.et_book_name);
        mEtBookISBN = (EditText) findViewById(R.id.et_add_book_isbn);
        mTvBookType = (TextView) findViewById(R.id.tv_book_type);
        mTvBookAddress = (TextView) findViewById(R.id.tv_book_interchange_address);
        mEtBookPrice = (EditText) findViewById(R.id.et_book_price);
        mEtBookDescription = (EditText) findViewById(R.id.et_book_description);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void onCreateGDUT(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvBookAddress.setOnClickListener(this);
        mTvBookType.setOnClickListener(this);
        mIvBookPicture.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_book_type: {
                Intent intent = new Intent(AddBookActivity.this,ChooseBookTypeActivity.class);
                startActivityForResult(intent,GET_TYPE_REQUEST_CODE);
                break;
            }

            case R.id.btn_submit: {
                upLoad();
                break;
            }

            case R.id.iv_add_book_picture: {
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
                startActivityForResult(wrapperIntent,GET_BOOK_PICTURE_CODE);*/
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent,GET_BOOK_PICTURE_CODE);
                break;
            }

            case R.id.tv_book_interchange_address:{
                Intent intent = new Intent(AddBookActivity.this,AddressActivity.class);
                startActivityForResult(intent,GER_ADDRESS_REQUEST_CODE);
                break;
            }
        }
    }

    private void upLoad(){
        String name = mEtBookName.getText().toString();
        String isbn = mEtBookISBN.getText().toString().trim();
        String type = mBookType.getName();
        String address = mAddress.getPlace();
        String price = mEtBookPrice.getText().toString();
        String description = mEtBookDescription.getText().toString();
      /*  if (!CheckUtil.checkISBN(isbn)) {
            showShortToast(getString(R.string.isbn_not_legal)+isbn);
            return;
        }*/

        final Book book = new Book();
        if (check(name,isbn,price,type,address,headLocalFilePath,description)){
            book.setName(name);
            book.setAddress(mAddress);
            book.setISBN(isbn);
            book.setOwner(BmobUser.getCurrentUser(this, GDUTUser.class));
            book.setBookType(mBookType);
            book.setPrice(price);
            book.setDescription(description);
            book.setState("0");

        } else {
            showShortToast(getResources().getString(R.string.something_is_empty));
            return;
        }
        Log.d("TAG", "upLoad: "+headLocalFilePath);
        showProgressDialog();
        Log.d("TAG", "upLoad:111 "+headLocalFilePath);
        BmobFile.uploadBatch(AddBookActivity.this, new String[]{headLocalFilePath}, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, final List<String> list1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        book.setAvatar(list1.get(0));
                        upBook(book);
                    }
                });
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "run: code"+i+s);
                       disProgressDialog();
                        showShortToast("code"+i+"message"+s);
                    }
                });

            }
        });
        Log.d("TAG", "upLoad:222 "+headLocalFilePath);

    }

    private void upBook(Book book) {
        book.save(this, new SaveListener() {
            @Override
            public void onSuccess() {// /storage/emulated/0/DCIM/Camera/IMG_20170408_161154.jpg  /storage/emulated/0/DCIM/Camera/IMG_20170408_161154.jpg
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        showShortToast(getResources().getString(R.string.upload_success));
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disProgressDialog();
                        showShortToast(getResources().getString(R.string.upload_fail)+"code"+i+"message"+s);
                    }
                });
            }
        });

    }

    private boolean check(String name,String isbn,String price,String type,
                          String address,String url,String description) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(isbn)
        && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(type)
                && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(url) && !TextUtils.isEmpty(description);
    }



    private void refreshImgString() {
        /*headLocalFilePath = Environment.getExternalStorageDirectory() + "/"
                + "GDUTBook/" + System.currentTimeMillis() + ".jpg";
        imageUri = Uri.fromFile(new File(headLocalFilePath));*/
        //String imagePath = "";
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

    private void checkPermition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mAddress = new Address();
        mBookType = new BookType();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Log.d("TAG", "onActivityResult: "+1);
            if (requestCode == GER_ADDRESS_REQUEST_CODE){
                String address = data.getStringExtra(KEY_ADDRESS_NAME);
                String objectId = data.getStringExtra(KEY_ADDRESS_OBJECTID);
                mTvBookAddress.setText(address);
                mAddress.setObjectId(objectId);
                mAddress.setPlace(address);
                Log.d("TAG", "onActivityResult: "+address+objectId);

            } else if (requestCode == GET_TYPE_REQUEST_CODE){
                String objectId = data.getStringExtra(KEY_GET_TYPE_ID);
                String type = data.getStringExtra(KEY_GET_TYPE_NAME);
                mBookType.setObjectId(objectId);
                mBookType.setName(type);
                mTvBookType.setText(type);
            } else if (requestCode == GET_BOOK_PICTURE_CODE) {

               /* try {
                    Log.d("TAG", "onActivityResult:try "+data.getData());
                    InputStream is = (new URL( data.getData().toString() )).openStream();
                    mIvBookPicture.setImageBitmap(BitmapFactory.decodeStream(is));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("TAG", "onActivityResult:error ");
                }*/
                Log.d("TAG", "onActivityResult:try11 "+data.getData());
                imageUri = data.getData();
                refreshImgString();
                ImageLoader.getInstance().displayImage(String.valueOf(imageUri),mIvBookPicture);
                //startPhotoZoom(data.getData());
            } else if (requestCode == PHOTORESOULT){
                Log.d("TAG", "onActivityResult:try "+data.getData());
                ImageLoader.getInstance().displayImage(String.valueOf(imageUri),mIvBookPicture);
            }

        }
        Log.d("TAG", "onActivityResult: "+12);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disProgressDialog();
    }
}
