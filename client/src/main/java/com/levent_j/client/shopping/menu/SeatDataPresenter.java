package com.levent_j.client.shopping.menu;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.net.ApiService;

/**
 * @auther : levent_j on 2018/4/6.
 * @desc :
 */

public class SeatDataPresenter extends BasePresenter{

    private IMenuCallback menuCallback;

    public SeatDataPresenter(IMenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    /**
     * 获取座位数据
     * @param seatId
     */
    public void getSeatData(String seatId){
        ApiService.getSeatData(seatId, new GetCallback<Seat>() {
            @Override
            public void done(Seat seat, AVException e) {
                if (e==null){
                    menuCallback.onGetSeatDataSuccess(seat);
                }else {
                    menuCallback.onGetSeatDataFailed(e);
                }
            }
        });
    }

    /**
     * 获取信号量
     * @param seat
     */
    public void obtainSemaphore(Shop shop,Seat seat) {
        ApiService.obtainSemphore(seat);
        ApiService.updateSeatAvailableAmount(shop,seat,-1,null);
        ApiService.updateSeatStatus(seat.getObjectId(),Seat.STATUS_DINING);
    }

    /**
     * 释放信号量
     * @param seat
     */
    public void freeSemphore(Shop shop,Seat seat){
        ApiService.freeSemphore(seat);
        ApiService.updateSeatAvailableAmount(shop,seat,1,null);
        ApiService.updateSeatStatus(seat.getObjectId(),Seat.STATUS_FREE);
    }

    @Override
    public void onDestroy() {

    }


    public void updateSeatStatus(Seat seat, int statusDining) {
        ApiService.updateSeatStatus(seat.getObjectId(),statusDining);
    }

    public void updateOrderStatus(Shop mShop, String userId,Seat seat) {
        ApiService.updateOrderStatus(mShop.getObjectId(),userId,seat.getObjectId());
    }
}
