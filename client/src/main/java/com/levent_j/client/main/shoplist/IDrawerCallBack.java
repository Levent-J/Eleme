package com.levent_j.client.main.shoplist;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.User;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public interface IDrawerCallBack {
    void onLoadUserDataSuccess(User user);

    void onLoadUserDataFailed(AVException e);
}
