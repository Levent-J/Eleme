package com.levent_j.client.order;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.net.ApiService;

/**
 * @auther : levent_j on 2018/3/26.
 * @desc :
 */

public class MyOrderDetailPresenter extends BasePresenter{

    private IMyOrderDetailCallback mCallback;

    public MyOrderDetailPresenter(IMyOrderDetailCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void getOrderDetail(String orderId){
        ApiService.getOrderDetail(orderId, new GetCallback<Order>() {
            @Override
            public void done(Order order, AVException e) {
                if (e==null){
                    mCallback.onGetDetailSuccess(order);
                }else {
                    mCallback.onGetDetailFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
