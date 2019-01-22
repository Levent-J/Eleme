package com.levent_j.business.order.orderdetail;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.google.protobuf.Api;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderDetailPresenter extends BasePresenter{

    private IOrderDetailCallback callback;

    public OrderDetailPresenter(IOrderDetailCallback callback) {
        this.callback = callback;
    }

    public void getOrderDetail(String orderId){
        ApiService.getOrderDetail(orderId, new GetCallback<Order>() {
            @Override
            public void done(Order order, AVException e) {
                if (e==null){
                    callback.onGetOrderDetailSuccess(order);
                }else {
                    callback.onGetOrderDetailFailed(e);
                }
            }
        });
    }

    public void updateOrderConfirmed(final Order order){
        ApiService.updateOrderStatus(order.getObjectId(), Order.STATUS_CONFIRMED, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    callback.onOrderStatusUpdateSuccess(order.getObjectId());
                }else {
                    callback.onGetOrderDetailFailed(e);
                }
            }
        });
    }

    public void updateOrderWait(final Order order){
        ApiService.updateOrderStatus(order.getObjectId(), Order.STATUS_WAIT, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    callback.onOrderStatusUpdateSuccess(order.getObjectId());
                }else {
                    callback.onGetOrderDetailFailed(e);
                }
            }
        });
    }

    public void updateOrderFinished(final Order order, Seat seat){
        ApiService.updateOrderStatus(order.getObjectId(), Order.STATUS_FINISHED, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    callback.onOrderStatusUpdateSuccess(order.getObjectId());
                }else {
                    callback.onGetOrderDetailFailed(e);
                }
            }
        });

        try {
            //除此之外，还要让该座位处于开放状态
            ApiService.freeSemphore(seat);
            ApiService.updateSeatStatus(seat.getObjectId(), Seat.STATUS_FREE);
            //更新店铺座位信息
            Shop shop = AVObject.createWithoutData(Shop.class,order.getShopId());
            ApiService.updateSeatAvailableAmount(shop,seat,1,null);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroy() {

    }

    public void pushNotification(Order mOrder) {
        ApiService.pushOrderNotification(mOrder);
    }

    public void getSeatDetail(String seatId) {
        ApiService.getSeatData(seatId, new GetCallback<Seat>() {
            @Override
            public void done(Seat seat, AVException e) {
                if (e==null){
                    callback.onGetSeatDetailSuccess(seat);
                }else {
                    callback.onGetSeatDetailFailed(e);
                }
            }
        });
    }
}
