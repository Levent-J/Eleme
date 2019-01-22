package com.levent_j.client.info;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.ImageUtil;

import java.io.File;

/**
 * @auther : levent_j on 2018/4/14.
 * @desc :
 */
public class MyInfoPresneter extends BasePresenter{

    private MyInfoCallback callback;

    public MyInfoPresneter(MyInfoCallback callback) {
        this.callback = callback;
    }

    public void getUserInfoData(String id){
        ApiService.getUserInfo(id, new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                if (e==null){
                    callback.onGetUserInfoSuccess(user);
                }else {
                    callback.onGetUserInfoFailed(e);
                }
            }
        });
    }

    public void updateUserAvatar(String userId, File avatarFile){
        ApiService.updateUserAvatar(userId, ImageUtil.File2Bytes(avatarFile), new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    callback.onUpdateUserAvatarSuccess();
                }else {
                    callback.onUpdateUserAvatarFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
