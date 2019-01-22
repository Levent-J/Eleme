package com.levent_j.client.shopping.trade;

import com.avos.avoscloud.AVException;

/**
 * @auther : levent_j on 2018/3/23.
 * @desc :
 */

public interface ITradeCallback {
    void onTradeSuccess(String orderId);
    void onTradeFailed(AVException e);
}
