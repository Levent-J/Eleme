package com.levent_j.client.main.shopdetail;

import com.avos.avoscloud.AVErrorUtils;
import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;

import java.util.List;

/**
 * @auther : levent_j on 2018/4/8.
 * @desc :
 */
public interface IShopDetailCallback {
    void onGetShopDetailSuccess(Shop shop);
    void onGetShopDetailFailed(AVException e);

    void onGetMenuSuccess(List<Food> foods);
    void onGetMenuFailed(AVException e);

    void onSeatFindSuccess(Seat seat);
    void onSeatFindFailed(AVException e);

    void onTakeOrderSuccess(String type);
    void onTakeOrderFailed(AVException e);

    void onBlockSeatSuccess();
    void onBlockSeatFailed(AVException e);
}
