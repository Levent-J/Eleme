package com.levent_j.client.main.shopdetail;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.MyLog;

import java.util.List;

/**
 * @auther : levent_j on 2018/4/8.
 * @desc :
 */
public class ShopDetailPresenter extends BasePresenter{

    private IShopDetailCallback mCallback;

    public ShopDetailPresenter(IShopDetailCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void getShopDetail(String shopId){
        ApiService.getShopDetail(shopId, new GetCallback<Shop>() {
            @Override
            public void done(Shop shop, AVException e) {
                if (e==null){
                    mCallback.onGetShopDetailSuccess(shop);
                }else {
                    mCallback.onGetShopDetailFailed(e);
                }
            }
        });
    }

    public void getShopMenu(String shopId){
        ApiService.loadFoodList(shopId, new FindCallback<Food>() {
            @Override
            public void done(List<Food> list, AVException e) {
                if (e==null){
                    mCallback.onGetMenuSuccess(list);
                }else {
                    mCallback.onGetMenuFailed(e);
                }
            }
        });
    }

    public void findAvaliableSeat(String shopId, String type){
        ApiService.findAvaliableSeat(shopId, type, new GetCallback<Seat>() {
            @Override
            public void done(Seat seat, AVException e) {
                if (e==null){
                    mCallback.onSeatFindSuccess(seat);
                }else {
                    mCallback.onSeatFindFailed(e);
                }
            }
        });
    }


    public void takeOrder(Shop shop, User user, final Seat seat) {
        ApiService.takeOrder(shop, user, seat, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    mCallback.onTakeOrderSuccess(seat.getType());
                }else {
                    mCallback.onTakeOrderFailed(e);
                }
            }
        });
        //同时还要锁定座位
        ApiService.obtainSemphore(seat);
        //更新座位状态
        ApiService.updateSeatStatus(seat.getObjectId(),Seat.STATUS_BOOKED);
        //还要更新座位数量
        ApiService.updateSeatAvailableAmount(shop, seat, -1, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    mCallback.onBlockSeatSuccess();
                }else {
                    mCallback.onBlockSeatFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }

}
