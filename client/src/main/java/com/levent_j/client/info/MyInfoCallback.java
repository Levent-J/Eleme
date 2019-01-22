package com.levent_j.client.info;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.User;

/**
 * @auther : levent_j on 2018/4/14.
 * @desc :
 */
public interface MyInfoCallback {
    void onGetUserInfoSuccess(User user);
    void onGetUserInfoFailed(AVException e);

    void onUpdateUserAvatarSuccess();
    void onUpdateUserAvatarFailed(AVException e);
}
