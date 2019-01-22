package com.levent_j.client.order;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Order;

/**
 * @auther : levent_j on 2018/3/26.
 * @desc :
 */

public interface IMyOrderDetailCallback {
    void onGetDetailSuccess(Order order);
    void onGetDetailFailed(AVException e);
}
