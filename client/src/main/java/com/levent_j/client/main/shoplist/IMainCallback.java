package com.levent_j.client.main.shoplist;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Shop;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public interface IMainCallback {
    void onGetShopListSuccess(List<Shop> shops);
    void onGetShopListFailed(AVException e);
}
