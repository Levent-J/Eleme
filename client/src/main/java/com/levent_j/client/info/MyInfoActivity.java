package com.levent_j.client.info;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.PermissionUtil;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.client.R;
import com.levent_j.client.common.util.MyCircleImgTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class MyInfoActivity extends BaseActivity implements MyInfoCallback, View.OnClickListener {

    private MainTitleBar mainTitleBar;
    private ImageView mAvatar;
    private TextView mUserName;
    private TextView mUserId;
    private TextView mUserPhone;
    private TextView mUserMail;

    private File mImageFile;

    private MyInfoPresneter mInfoPresneter;

    @Override
    public int setLayout() {
        return R.layout.activity_my_info;
    }

    @Override
    public void init() {
        mainTitleBar = findViewById(R.id.title_bar);
        mAvatar = findViewById(R.id.iv_user_avater);
        mUserName = findViewById(R.id.tv_user_name);
        mUserId = findViewById(R.id.tv_user_id);
        mUserPhone = findViewById(R.id.tv_user_phone);
        mUserMail = findViewById(R.id.tv_user_email);

        initTitleBar();

        mAvatar.setOnClickListener(this);

        mInfoPresneter = new MyInfoPresneter(this);
        addPresenter(mInfoPresneter);

        loadData();
    }

    private void loadData() {
        String id = UserAccountManager.getInstance().getUserId(this);
        mInfoPresneter.getUserInfoData(id);
    }

    private void initTitleBar() {
        mainTitleBar.setTitle("账户信息");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, MyInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onGetUserInfoSuccess(User user) {
        MyLog.d("onGetUserInfoSuccess");
        mUserName.setText(user.getUsername());
        mUserId.setText(user.getUuid());
        mUserPhone.setText(user.getMobilePhoneNumber());
        mUserMail.setText(TextUtils.isEmpty(user.getEmail()) ? "暂无" : user.getEmail());

        byte[] bytes = user.getAvatar();
        if (bytes != null) {
            File file = ImageUtil.Bytes2File(bytes);
            Picasso.get().load(file).transform(new MyCircleImgTransformation(120,120)).into(mAvatar);
        }else {
            Picasso.get().load(R.mipmap.ic_launcher_round).into(mAvatar);
        }
    }

    @Override
    public void onGetUserInfoFailed(AVException e) {
        MyLog.e("onGetUserInfoFailed " + e.getMessage());
    }

    @Override
    public void onUpdateUserAvatarSuccess() {
        MyLog.d("onUpdateUserAvatarSuccess");
    }

    @Override
    public void onUpdateUserAvatarFailed(AVException e) {
        MyLog.e("onUpdateUserAvatarFailed " +e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_user_avater:
                //上传
                //先检查权限
                if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_CAMERA)){
                    PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_CAMERA);
                    break;
                }
                if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE)){
                    PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE);
                    break;
                }
                if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE)){
                    PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE);
                    break;
                }
                ImageUtil.showImageDialog(this);
                break;
        }
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
                Picasso.get().load(mImageFile).into(mAvatar);
                //开始上传
                mInfoPresneter.updateUserAvatar(UserAccountManager.getInstance().getUserId(this),mImageFile);
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
