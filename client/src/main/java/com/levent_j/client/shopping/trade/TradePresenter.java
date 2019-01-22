package com.levent_j.client.shopping.trade;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/23.
 * @desc :
 */

public class TradePresenter extends BasePresenter{

    private ITradeCallback tradeCallback;

    public TradePresenter(ITradeCallback tradeCallback) {
        this.tradeCallback = tradeCallback;
    }

    public void takeOrder(TradeModel tradeModel, String totalPrice, Shop mShop, String note,Seat seat){
        Map<String,Integer> menu = new HashMap<>();
        Map<String,String> prices = new HashMap<>();
        String intro = "";

        Map<Food,Integer> map = tradeModel.getMap();
        for (Map.Entry<Food,Integer> entry : map.entrySet()){
            if (TextUtils.isEmpty(intro)){
                intro = entry.getKey().getName()+"等";
            }
            menu.put(entry.getKey().getName(),entry.getValue());
            prices.put(entry.getKey().getName(),entry.getKey().getPrice() * entry.getValue() + "￥");
        }
        intro = intro+map.size()+"件菜品";

        final Order order = new Order();
        ApiService.takeOrder(
                order,
                menu,
                seat,
                intro,
                prices,
                totalPrice,
                mShop.getName(),
                mShop.getObjectId(),
                UserAccountManager.getInstance().getmUser().getObjectId(),
                note,
                Order.STATUS_ORIGIN,
                new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null){
                            tradeCallback.onTradeSuccess(order.getObjectId());
                        }else {
                            tradeCallback.onTradeFailed(e);
                        }
                    }
                });
    }
    @Override
    public void onDestroy() {

    }
}
