package com.levent_j.business.order.orderlist;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderListPresenter extends BasePresenter{

    private IOrderListCallback callback;

    public OrderListPresenter(IOrderListCallback callback) {
        this.callback = callback;
    }

    public void getOrderList(){
        String shopId = UserAccountManager.getInstance().getmShop().getObjectId();
        ApiService.loadShopOrdaerList(shopId, new FindCallback<Order>() {
            @Override
            public void done(List<Order> list, AVException e) {
                if (e==null){
                    callback.onGetOrderListSuccess(list);
                }else {
                    callback.onGetOrderListFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
