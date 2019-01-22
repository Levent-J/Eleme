package com.levent_j.client.shopping.menu;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public interface IMenuCallback {
    void onLoadShopDetailSuccess(Shop shop);
    void onLoadShopDetailFailed(AVException e);

    void onLoadMenuSuccess(List<Food> list);
    void onLoadMenuFailed(AVException e);

    void onGetSeatDataSuccess(Seat seat);
    void onGetSeatDataFailed(AVException e);

}
