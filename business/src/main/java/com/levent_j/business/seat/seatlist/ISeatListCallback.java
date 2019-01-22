package com.levent_j.business.seat.seatlist;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public interface ISeatListCallback {
    void onLoadSeatListSuccess(List<Seat> list);
    void onLoadSeatListFailed(AVException e);

    void onGetShopSuccess(Shop shop);
    void onGetShopFailed(AVException e);

    void onAddSeatSuccess();
    void onAddSeatFailed(AVException e);
    void onDelSeatSuccess();
    void onDelSeatFailed(AVException e);

    void onCantDelSeat();
}
