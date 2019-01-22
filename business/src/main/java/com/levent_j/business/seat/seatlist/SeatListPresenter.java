package com.levent_j.business.seat.seatlist;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.MyLog;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public class SeatListPresenter extends BasePresenter {

    private ISeatListCallback callback;

    public SeatListPresenter(ISeatListCallback callback) {
        this.callback = callback;
    }

    public void loadSeatList(String shopId) {
        ApiService.loadSeatList(shopId, new FindCallback<Seat>() {
            @Override
            public void done(List<Seat> list, AVException e) {
                if (e == null) {
                    callback.onLoadSeatListSuccess(list);
                } else {
                    callback.onLoadSeatListFailed(e);
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }


    public void addSeat(String shopId, final String type, final Shop mShop) {
        int number = 1;
        if (type.equals(Seat.TYPE_SMALL) ){
            number += mShop.getSeatAmountA();
        }else if (type.equals(Seat.TYPE_MEDIUM)){
            number += mShop.getSeatAmountB();
        }else {
            number += mShop.getSeatAmountC();
        }
        MyLog.d("add" + type + " number " + number);
        final int finalNumber = number;
        ApiService.addSeat(shopId, type, number, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    //添加成功 更新seat计数器
                    updateSeatAmount(mShop,type, finalNumber);
                    callback.onAddSeatSuccess();
                }else {
                    callback.onAddSeatFailed(e);
                }
            }
        });

    }

    public void subSeat(final String shopId, final String type, final Shop mShop) {

        int number = 0;
        if (type.equals(Seat.TYPE_SMALL) ){
            number = mShop.getSeatAmountA();
        }else if (type.equals(Seat.TYPE_MEDIUM)){
            number = mShop.getSeatAmountB();
        }else {
            number = mShop.getSeatAmountC();
        }
        MyLog.d("sub" + type + " number " +number);
        if (number == 0){//为0 说明不存在此种类型的座位
            callback.onCantDelSeat();
            return;
        }
        final int finalNumber = number - 1;
        ApiService.delSeat(shopId, type, number, new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    //删除成功 更新seat计数器
                    updateSeatAmount(mShop,type, finalNumber);
                    callback.onDelSeatSuccess();
                }else {
                    callback.onDelSeatFailed(e);
                }
            }
        });

    }


    public void getShop(String mShopId) {
        ApiService.getShopDetail(mShopId, new GetCallback<Shop>() {
            @Override
            public void done(Shop shop, AVException e) {
                if (e == null) {
                    callback.onGetShopSuccess(shop);
                } else {
                    callback.onGetShopFailed(e);
                }
            }
        });
    }

    private void updateSeatAmount(final Shop mShop, String type, int number) {
        ApiService.updateSeatAmount(mShop, type, number, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    getShop(mShop.getObjectId());
                }
            }
        });
    }

}
