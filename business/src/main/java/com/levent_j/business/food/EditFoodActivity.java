package com.levent_j.business.food;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.utils.DataCache;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.PermissionUtil;
import com.levent_j.baselibrary.utils.ToastUtils;
import com.levent_j.baselibrary.views.ImageSelectView;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyLoadingDialog;
import com.levent_j.business.R;
import com.levent_j.business.menu.MenuActivity;
import com.levent_j.business.views.MyEditTextView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/12.
 * @desc :
 */

public class EditFoodActivity extends BaseActivity implements IEditFoodCallBack {


    private MainTitleBar mTitleBar;
    private MyEditTextView mFoodName;
    private MyEditTextView mFoodPrice;
    private EditText mFoodDesc;
    private ImageSelectView mFoodAvatar;
    private Button mCreateFood;
    private MyLoadingDialog myLoadingDialog;

    private static final String KEY_FOOD_NAME = "key_name";
    private static final String KEY_FOOD_PRICE = "key_price";
    private static final String KEY_FOOD_DESC = "key_desc";
    private static final String KEY_FOOD_AVATAR_ID = "key_avatar_id";
    private static final String KEY_FOOD_ID = "key_food_id";

    private EditFoodPresenter mEditFoodPresenter;

    private File mImageFile;
    private String mFoodId;


    @Override
    public int setLayout() {
        return R.layout.activity_edit_food;
    }

    @Override
    public void init() {
        mTitleBar = findViewById(R.id.title_bar);
        mFoodName = findViewById(R.id.et_food_name);
        mFoodPrice = findViewById(R.id.et_food_price);
        mFoodDesc = findViewById(R.id.et_food_desc);
        mFoodAvatar = findViewById(R.id.iv_food_avatar);
        mCreateFood = findViewById(R.id.btn_create_food);

        mCreateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFood();
            }
        });
        mFoodAvatar.setCoverCallback(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        mFoodAvatar.setChaCallback(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg();
            }
        });

        mEditFoodPresenter = new EditFoodPresenter(this);

        myLoadingDialog = new MyLoadingDialog(this);
        myLoadingDialog.setCancelable(false);
        myLoadingDialog.setmTips("上传中……");

        initTitleBar();
        initEditText();
        initData();
    }

    private void initData() {
        Intent intent =getIntent();
        mFoodId = intent.getStringExtra(KEY_FOOD_ID);
        if (mFoodId!=null){//编辑
            mFoodName.setContent(intent.getStringExtra(KEY_FOOD_NAME));
            mFoodPrice.setContent(""+intent.getDoubleExtra(KEY_FOOD_PRICE,0));
            mFoodDesc.setText(intent.getStringExtra(KEY_FOOD_DESC));
            long imgId = intent.getLongExtra(KEY_FOOD_AVATAR_ID,-1);
            byte[] bytes = (byte[]) DataCache.get(imgId);
            mImageFile = ImageUtil.Bytes2File(bytes);
            Picasso.get().load(mImageFile).resize(100,100).into(mFoodAvatar.getImageView());
            mFoodAvatar.setImgMode();
        }
    }


    private void initEditText() {
        mFoodName.setInputTitle("名称");
        mFoodName.setCheckRule(new MyEditTextView.ICheckRule() {
            @Override
            public boolean check(String s) {
                return !TextUtils.isEmpty(s);
            }
        });

        mFoodPrice.setInputTitle("价格");
        mFoodPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mFoodPrice.setCheckRule(new MyEditTextView.ICheckRule() {
            @Override
            public boolean check(String s) {
                return !TextUtils.isEmpty(s);
            }
        });



    }

    private void initTitleBar() {
        mTitleBar.getTitle().setText("编辑食物");
        mTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 创建Food
     * @param activity
     */
    public static void openActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, EditFoodActivity.class);
        activity.startActivityForResult(intent, MenuActivity.REQUSET_CODE_CREATE);
    }

    /**
     * 编辑Food
     * @param activity
     * @param food
     */
    public static void openActivity(BaseActivity activity, Food food) {
        Intent intent = new Intent(activity, EditFoodActivity.class);
        intent.putExtra(KEY_FOOD_ID,food.getObjectId());
        intent.putExtra(KEY_FOOD_NAME,food.getName());
        intent.putExtra(KEY_FOOD_PRICE,food.getPrice());
        intent.putExtra(KEY_FOOD_DESC,food.getDescription());
        intent.putExtra(KEY_FOOD_AVATAR_ID, DataCache.add(food.getAvatar()));
        activity.startActivityForResult(intent, MenuActivity.REQUSET_CODE_EDIT);
    }

    @Override
    public void onCreateFoodSuccess() {
        MyLog.d("onCreateFoodSuccess : ");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onCreateFoodFailed(AVException e) {
        ToastUtils.avException(this,e.getCode());
        MyLog.e("onCreateFoodFailed : " + e.getMessage() + " " + e.getCode());
        myLoadingDialog.dismiss();
    }

    @Override
    public void onEditFoodSuccess() {
        MyLog.d("onEditFoodSuccess");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onEditFoodFailed(AVException e) {
        ToastUtils.avException(this,e.getCode());
        MyLog.e("onEditFoodFailed : " + e.getMessage());
        myLoadingDialog.dismiss();
    }


    private void submitFood(){
        //先检查输入
        if (mFoodName.checkInputContent()
                && mFoodPrice.checkInputContent()
                && mImageFile != null) {

            myLoadingDialog.show();

            if (mFoodId==null){
                //创建食物
                mEditFoodPresenter.createFood(
                        mFoodName.getContent(),
                        Double.parseDouble(mFoodPrice.getContent()),
                        mFoodDesc.getText().toString().trim(),
                        mImageFile);
            }else {
                //编辑食物
                mEditFoodPresenter.editFood(
                        mFoodId,
                        mFoodName.getContent(),
                        Double.parseDouble(mFoodPrice.getContent()),
                        mFoodDesc.getText().toString().trim(),
                        mImageFile);
            }
        }
    }

    private void resetImg() {
        mFoodAvatar.setCoverMode();
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
                Picasso.get().load(mImageFile).resize(100,100).into(mFoodAvatar.getImageView());
                mFoodAvatar.setImgMode();
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
