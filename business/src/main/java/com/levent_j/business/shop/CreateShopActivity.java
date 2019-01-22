package com.levent_j.business.shop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.PermissionUtil;
import com.levent_j.baselibrary.views.ImageSelectView;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyLoadingDialog;
import com.levent_j.business.R;
import com.levent_j.business.main.MainActivity;
import com.levent_j.business.views.MyEditTextView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class CreateShopActivity extends BaseActivity implements View.OnClickListener, ICreateCallBack {

    private MainTitleBar mainTitleBar;
    private MyEditTextView mShopName;
    private MyEditTextView mShopAddr;
    private MyEditTextView mShopPhone;
    private ImageSelectView mShopAvatar;
    private Button mSubmit;
    private MyLoadingDialog myLoadingDialog;

    private File mImageFile;
    private boolean isEdit;
    private String mShopId;

    private CreateShopPresenter mCreateShopPresenter;

    private static final String EXTRA_IS_EDIT = "key_shop";

    @Override
    public int setLayout() {
        return R.layout.activity_create_shop;
    }

    @Override
    public void init() {
        mainTitleBar = findViewById(R.id.title_bar);
        mShopName = findViewById(R.id.et_create_shop_name);
        mShopAddr = findViewById(R.id.et_create_shop_addr);
        mShopPhone = findViewById(R.id.et_create_shop_phone);
        mShopAvatar = findViewById(R.id.iv_create_shop_avatar);
        mSubmit = findViewById(R.id.btn_create_submit);

        mShopAvatar.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        myLoadingDialog = new MyLoadingDialog(this);
        myLoadingDialog.setCancelable(false);

        mShopAvatar.setCoverCallback(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        mShopAvatar.setChaCallback(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg();
            }
        });

        mCreateShopPresenter = new CreateShopPresenter(this);

        initTitleBar();
        initEditText();
        initData();
    }

    private void initData() {
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT,false);
        if (isEdit){//true，说明现在在编辑
            Shop shop = UserAccountManager.getInstance().getmShop();
            mShopName.setContent(shop.getName());
            mShopAddr.setContent(shop.getAddress());
            mShopPhone.setContent(shop.getPhone());

            mImageFile = ImageUtil.Bytes2File(shop.getAvatar());
            Picasso.get().load(mImageFile).into(mShopAvatar.getImageView());
            mShopAvatar.setImgMode();

            mShopId = shop.getObjectId();
        }
    }

    //初始化输入框
    private void initEditText() {
        mShopName.setInputTitle("店铺名称");
        mShopName.setCheckRule(new MyEditTextView.ICheckRule() {
            @Override
            public boolean check(String s) {
                return !TextUtils.isEmpty(s);
            }
        });
        mShopAddr.setInputTitle("店铺地址");
        mShopAddr.setCheckRule(new MyEditTextView.ICheckRule() {
            @Override
            public boolean check(String s) {
                return !TextUtils.isEmpty(s);
            }
        });
        mShopPhone.setInputTitle("联系电话");
        mShopPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        mShopPhone.setCheckRule(new MyEditTextView.ICheckRule() {
            @Override
            public boolean check(String s) {
                return !TextUtils.isEmpty(s);
            }
        });
    }

    private void initTitleBar() {
        mainTitleBar.getTitle().setText("创建商铺");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity){
        Intent intent = new Intent(activity,CreateShopActivity.class);
        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_CREATE_SHOP);
    }

    public static void openActivity(BaseActivity activity,boolean edit){
        Intent intent = new Intent(activity,CreateShopActivity.class);
        intent.putExtra(EXTRA_IS_EDIT,edit);
        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_EDIT_SHOP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_submit:
                //提交
                if (mShopName.checkInputContent()&&mShopAddr.checkInputContent()&&mShopPhone.checkInputContent()&&mImageFile!=null){
                    //输入正确
                    myLoadingDialog.setmTips("上传中……");
                    myLoadingDialog.show();

                    if (isEdit){
                        mCreateShopPresenter.editShop(mShopId,mShopName.getContent(),mShopAddr.getContent(),mShopPhone.getContent(),mImageFile);
                    }else {
                        mCreateShopPresenter.createShop(mShopName.getContent(),mShopAddr.getContent(),mShopPhone.getContent(),mImageFile);
                    }
                }else {
                    Toast.makeText(this,"输入有误",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void resetImg() {
        mShopAvatar.setCoverMode();
        mImageFile = null;
    }

    public void selectImg(){
        //先检查权限
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_CAMERA)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_CAMERA);
            return;
        }
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE);
            return;
        }
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }
        ImageUtil.showImageDialog(this);
    }

    @Override
    public void onCreateSuccess() {
        //商铺创建成功
        MyLog.d("onCreateSuccess");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onCreateFailed(Exception e) {
        myLoadingDialog.dismiss();
        Toast.makeText(this,"创建失败："+e.getMessage(),Toast.LENGTH_SHORT).show();
        MyLog.e("onCreateFailed:"+e);
    }

    @Override
    public void onEditSuccess() {
        //商铺创建成功
        MyLog.d("onEditSuccess");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onEditFailed(AVException e) {
        myLoadingDialog.dismiss();
        Toast.makeText(this,"编辑失败："+e.getMessage(),Toast.LENGTH_SHORT).show();
        MyLog.e("onEditFailed:"+e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode){
            return;
        }
        switch (requestCode){
            case ImageUtil.REQUEST_CAMERA:
                ImageUtil.cropImage(this,ImageUtil.getImageUri());
                break;
            case ImageUtil.REQUEST_GALLERY:
                ImageUtil.createImageFile();
                Uri uri = data.getData();
                if (uri!=null){
                    ImageUtil.cropImage(this,uri);
                }else {
                    MyLog.e("uri is null");
                }
                break;
            case ImageUtil.REQUEST_CROP:
                mImageFile = ImageUtil.getFile();
                Picasso.get().load(mImageFile).resize(100,100).into(mShopAvatar.getImageView());
                mShopAvatar.setImgMode();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtil.PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //已授予权限
                    MyLog.d("OK!!!");
                }
                break;
        }
    }

}
