package com.levent_j.business.order.orderlist;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Order;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public interface IOrderListCallback {
    void onGetOrderListSuccess(List<Order> orders);
    void onGetOrderListFailed(AVException e);
}
