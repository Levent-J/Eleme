package com.levent_j.business.main;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Shop;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public interface MainCallBack {
    void onGetShopSuccess(Shop shop);
    void onGetShopFailed(AVException e);

    void onGetUserDataSuccess();
    void onGetUSerDataFailed(AVException e);
}
