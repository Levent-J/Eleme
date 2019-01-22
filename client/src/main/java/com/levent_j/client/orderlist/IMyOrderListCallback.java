package com.levent_j.client.orderlist;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Order;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/24.
 * @desc :
 */

public interface IMyOrderListCallback {
    void onGetOrderListSuccess(List<Order> list);
    void onGetOrderListFailed(AVException e);
}
