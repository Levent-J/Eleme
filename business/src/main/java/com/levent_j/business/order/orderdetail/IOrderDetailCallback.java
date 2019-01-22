package com.levent_j.business.order.orderdetail;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public interface IOrderDetailCallback {
    void onGetOrderDetailSuccess(Order order);
    void onGetOrderDetailFailed(AVException e);

    void onOrderStatusUpdateSuccess(String orderId);
    void onOrderStatusUpdateFailed(AVException e);

    void onGetSeatDetailSuccess(Seat seat);

    void onGetSeatDetailFailed(AVException e);
}
