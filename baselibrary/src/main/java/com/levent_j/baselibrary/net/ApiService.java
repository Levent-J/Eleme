package com.levent_j.baselibrary.net;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.levent_j.baselibrary.data.Food.*;
import static com.levent_j.baselibrary.data.Shop.*;

/**
 * @auther : levent_j on 2018/3/13.
 * @desc :
 */

public class ApiService {


    public static void getUserInfo(String id,GetCallback<User> callback){
        AVQuery<User> userAVQuery = AVQuery.getQuery(User.class);
        userAVQuery.getInBackground(id,callback);
    }

    /**
     * 创建食物
     *
     * @param saveCallback
     */
    public static void createFood(String name, double price, String desc, int sales, String shopId, byte[] avatar, SaveCallback saveCallback) {
        Food food = new Food();
        food.setName(name);
        food.setPrice(price);
        food.setDescription(desc);
        food.setSales(sales);
        food.setShopId(shopId);
        food.setAvatar(avatar);
        food.saveInBackground(saveCallback);

    }

    /**
     * 编辑食物
     *
     * @param name
     * @param price
     * @param desc
     * @param saveCallback
     */
    public static void editFood(String id, String name, double price, String desc, byte[] avatar, SaveCallback saveCallback) {
        Food food = null;
        try {
            food = AVObject.createWithoutData(Food.class, id);
            food.setName(name);
            food.setPrice(price);
            food.setDescription(desc);
            food.setAvatar(avatar);
            food.saveInBackground(saveCallback);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFood(Food food, DeleteCallback callback) {
        food.deleteEventually(callback);
    }

    /**
     * 创建店铺
     */
    public static void createShop(String name, String address, String phone, byte[] avatar, String masterId, SaveCallback saveCallback) {
        Shop shop = new Shop();
        shop.setName(name);
        shop.setAddress(address);
        shop.setPhone(phone);
        shop.setSeatAmountA(0);
        shop.setSeatAmountB(0);
        shop.setSeatAmountC(0);
        shop.setSeatAvailableAmountA(0);
        shop.setSeatAvailableAmountB(0);
        shop.setSeatAvailableAmountC(0);
        shop.setAvatar(avatar);
        shop.setMasterId(masterId);
        shop.saveInBackground(saveCallback);
    }

    /**
     * 编辑店铺
     */
    public static void editShop(String id, String name, String address, String phone, byte[] avatar, SaveCallback saveCallback) {
        Shop shop = null;
        try {
            shop = AVObject.createWithoutData(Shop.class, id);
            shop.setName(name);
            shop.setAddress(address);
            shop.setPhone(phone);
            shop.setAvatar(avatar);
            shop.saveInBackground(saveCallback);
        } catch (AVException e) {
            e.printStackTrace();
        }

    }


    /**
     * 查询商家信息
     *
     * @param masterId
     * @param getCallback
     */
    public static void queryShop(String masterId, GetCallback<Shop> getCallback) {
        AVQuery<Shop> shopAVQuery = AVQuery.getQuery(Shop.class);
        shopAVQuery.whereEqualTo(SHOP_MASTER_ID, masterId);
        shopAVQuery.getFirstInBackground(getCallback);
    }


    public static void getShopDetail(String shopId, GetCallback<Shop> getCallback) {
        AVQuery<Shop> shopAVQuery = AVQuery.getQuery(Shop.class);
        shopAVQuery.getInBackground(shopId, getCallback);
    }

    /**
     * 查询菜单
     *
     * @param shopId
     * @param callback
     */
    public static void loadFoodList(String shopId, FindCallback<Food> callback) {
        AVQuery<Food> foodAVQuery = AVQuery.getQuery(Food.class);
        foodAVQuery.whereEqualTo(FOOD_SHOP_ID, shopId);
        foodAVQuery.orderByAscending(Food.CREATED_AT);
        foodAVQuery.findInBackground(callback);
    }

    public static void loadUserData(String userId, GetCallback<User> callback) {
        AVQuery<User> userAVQuery = AVQuery.getQuery(User.class);
        userAVQuery.getInBackground(userId, callback);
    }

    /**
     * 获取商家列表
     */
    public static void loadShopList(FindCallback<Shop> callback) {
        AVQuery<Shop> shopAVQuery = AVQuery.getQuery(Shop.class);
        shopAVQuery.addDescendingOrder(CREATED_AT);
        shopAVQuery.findInBackground(callback);
    }

    /**
     * 下单
     * @param order
     * @param menu
     * @param intro
     * @param prices
     * @param totalPrice
     * @param shopName
     * @param shopId
     * @param id
     * @param note
     * @param status
     * @param saveCallback
     */
    public static void takeOrder(Order order,
                                 Map<String, Integer> menu,
                                 Seat seat,
                                 String intro,
                                 Map<String, String> prices,
                                 String totalPrice,
                                 String shopName,
                                 String shopId,
                                 String id,
                                 String note,
                                 int status,
                                 SaveCallback saveCallback) {
        order.setFoodAmountMap(menu);
        order.setSeatName(seat.getType() + seat.getNumber());
        order.setSeatId(seat.getObjectId());
        order.setIntro(intro);
        order.setFoodPriceMap(prices);
        order.setTotalPrice(totalPrice);
        order.setShopName(shopName);
        order.setShopId(shopId);
        order.setUserId(id);
        order.setNote(note);
        order.setStatus(status);
        order.setType(Order.TYPE_FOOD);
        order.setInstallationId(AVInstallation.getCurrentInstallation().getInstallationId());
        order.saveInBackground(saveCallback);
    }

    public static void takeOrder(Shop shop,User user,Seat seat,SaveCallback saveCallback){
        Order order = new Order();
        order.setShopName(shop.getName());
        order.setShopId(shop.getObjectId());
        order.setUserId(user.getObjectId());
        order.setIntro("预定"  + seat.getType() + seat.getNumber());
        order.setSeatName(seat.getType() + seat.getNumber());
        order.setType(Order.TYPE_BOOK);
        order.setStatus(Order.STATUS_BOOKED);
        order.setSeatId(seat.getObjectId());
        order.setInstallationId(AVInstallation.getCurrentInstallation().getInstallationId());
        order.saveInBackground(saveCallback);

        seat.setBookedId(user.getObjectId());
        seat.saveInBackground();
    }

    /**
     * 获取该用户的订单列表
     *
     * @param userId
     * @param findCallback
     */
    public static void loadUserOrderList(String userId, FindCallback<Order> findCallback) {
        AVQuery<Order> orderAVQuery = AVQuery.getQuery(Order.class);
        orderAVQuery.whereEqualTo(Order.ORDER_USER_ID, userId);
        orderAVQuery.orderByDescending(Order.CREATED_AT);
        orderAVQuery.findInBackground(findCallback);
    }

    /**
     * 获取该商家的订单列表
     *
     * @param shopId
     * @param findCallback
     */
    public static void loadShopOrdaerList(String shopId, FindCallback<Order> findCallback) {
        AVQuery<Order> orderAVQuery = AVQuery.getQuery(Order.class);
        orderAVQuery.whereEqualTo(Order.ORDER_SHOP_ID, shopId);
        orderAVQuery.orderByDescending(Order.CREATED_AT);
        orderAVQuery.findInBackground(findCallback);
    }

    /**
     * 获取订单详情
     *
     * @param orderId
     * @param callback
     */
    public static void getOrderDetail(String orderId, GetCallback<Order> callback) {
        AVQuery<Order> orderAVQuery = AVQuery.getQuery(Order.class);
        orderAVQuery.getInBackground(orderId, callback);
    }

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param status
     * @param saveCallback
     */
    public static void updateOrderStatus(String orderId, int status, SaveCallback saveCallback) {
        try {
            Order order = AVObject.createWithoutData(Order.class, orderId);
            order.setStatus(status);
            order.saveInBackground(saveCallback);
        } catch (AVException e) {
            MyLog.e(e.getMessage());
        }
    }

    public static void updateOrderStatus(String shopId,String userId,String seatId){
       AVQuery<Order> orderAVQuery = AVQuery.getQuery(Order.class);
       orderAVQuery.whereEqualTo(Order.ORDER_SHOP_ID,shopId);
       orderAVQuery.whereEqualTo(Order.ORDER_USER_ID,userId);
       orderAVQuery.whereEqualTo(Order.ORDER_SEAT_ID,seatId);
       orderAVQuery.getFirstInBackground(new GetCallback<Order>() {
           @Override
           public void done(Order order, AVException e) {
               if (e==null){
                   order.setStatus(Order.STATUS_FINISHED);
               }else {
                   e.printStackTrace();
               }
           }
       });
    }

    /**
     * 获取座位列表
     *
     * @param shopId
     * @param callback
     */
    public static void loadSeatList(String shopId, FindCallback<Seat> callback) {
        AVQuery<Seat> seatAVQuery = AVQuery.getQuery(Seat.class);
        seatAVQuery.whereEqualTo(Seat.SEAT_SHOP_ID, shopId);
        seatAVQuery.orderByAscending(Seat.SEAT_NUMBER);
        seatAVQuery.orderByAscending(Seat.SEAT_TYPE);

        seatAVQuery.findInBackground(callback);
    }

    /**
     * 增加座位
     *
     * @param mShopId
     * @param type
     * @param number
     * @param saveCallback
     */
    public static void addSeat(String mShopId, String type, int number, SaveCallback saveCallback) {
        Seat seat = new Seat();
        seat.setShopId(mShopId);
        seat.setType(type);
        seat.setNumber(number);
        seat.setStatus(Seat.STATUS_FREE);
        seat.setSemaphore(1);
        seat.setBookedId("");
        seat.saveInBackground(saveCallback);
    }

    /**
     * 删除座位
     *
     * @param mShopId
     * @param type
     * @param number
     * @param deleteCallback
     */
    public static void delSeat(String mShopId, String type, int number, final DeleteCallback deleteCallback) {
        AVQuery<Seat> seatAVQuery = AVQuery.getQuery(Seat.class);
        seatAVQuery.whereEqualTo(Seat.SEAT_SHOP_ID, mShopId);
        seatAVQuery.whereEqualTo(Seat.SEAT_TYPE, type);
        seatAVQuery.whereEqualTo(Seat.SEAT_NUMBER, number);
        seatAVQuery.getFirstInBackground(new GetCallback<Seat>() {
            @Override
            public void done(Seat seat, AVException e) {
                if (e == null) {
                    seat.deleteInBackground(deleteCallback);
                } else {
                    MyLog.e("find seat failed: " + e.getMessage());
                }
            }
        });

    }

    /**
     * 更新对应类型的座位数量
     *
     * @param shop
     * @param mType
     * @param saveCallback
     * @throws AVException
     */
    public static void updateSeatAmount(Shop shop, String mType, int number, SaveCallback saveCallback) {
//        shop = AVObject.createWithoutData(Shop.class,shop.getObjectId());
        if (mType.equals(Seat.TYPE_SMALL)) {
            shop.setSeatAmountA(number);
            shop.setSeatAvailableAmountA(number);
        } else if (mType.equals(Seat.TYPE_MEDIUM)) {
            shop.setSeatAmountB(number);
            shop.setSeatAvailableAmountB(number);
        } else {
            shop.setSeatAmountC(number);
            shop.setSeatAvailableAmountC(number);
        }
        shop.saveInBackground(saveCallback);
    }

    public static void updateSeatAvailableAmount(Shop shop, Seat seat,int amountAdded,SaveCallback saveCallback) {
        if (seat.getType().equals(Seat.TYPE_SMALL)){
            int amount = shop.getSeatAvailableAmountA();
            shop.setSeatAvailableAmountA(amount + amountAdded);
        }else if (seat.getType().equals( Seat.TYPE_MEDIUM)){
            int amount = shop.getSeatAvailableAmountB();
            shop.setSeatAvailableAmountB(amount + amountAdded);
        }else {
            int amount = shop.getSeatAvailableAmountB();
            shop.setSeatAvailableAmountB(amount + amountAdded);
        }
        shop.saveInBackground(saveCallback);

    }

    /**
     * 获取座位详情
     * @param seatId
     * @param seatGetCallback
     */
    public static void getSeatData(String seatId,GetCallback<Seat> seatGetCallback){
        AVQuery<Seat> seatAVQuery = AVQuery.getQuery(Seat.class);
        seatAVQuery.getInBackground(seatId,seatGetCallback);
    }

    /**
     * 将信号量值置位0
     * @param seat
     */
    public static void obtainSemphore(Seat seat) {
        try {
            seat = AVObject.createWithoutData(Seat.class,seat.getObjectId());
            seat.obtainSemaphore();
            seat.saveInBackground();
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将信号量置位1
     * @param seat
     */
    public static void freeSemphore(Seat seat){
        try {
            seat = AVObject.createWithoutData(Seat.class,seat.getObjectId());
            seat.freeSemaphore();
            seat.saveInBackground();
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void updateSeatStatus(String seatId,int status,SaveCallback saveCallback){
        try {
            Seat seat = AVObject.createWithoutData(Seat.class,seatId);
            seat.setStatus(status);
            seat.saveInBackground(saveCallback);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void updateSeatStatus(String seatId,int status){
        updateSeatStatus(seatId,status,null);
    }

    /**
     * 查找空闲的座位
     * @param shopId
     * @param type
     * @param getCallback
     */
    public static void findAvaliableSeat(String shopId,String type,GetCallback<Seat> getCallback){
        AVQuery<Seat> seatAVQuery = AVQuery.getQuery(Seat.class);
        seatAVQuery.whereEqualTo(Seat.SEAT_SHOP_ID,shopId);
        seatAVQuery.whereEqualTo(Seat.SEAT_TYPE,type);
        seatAVQuery.whereEqualTo(Seat.SEAT_SEMAPHORE,1);
        seatAVQuery.getFirstInBackground(getCallback);
        seatAVQuery.cancel();
    }

    public static void updateUserAvatar(String userId, byte[] avatar,SaveCallback saveCallback) {
        try {
            User user = AVObject.createWithoutData(User.class,userId);
            user.setAvatar(avatar);
            user.saveInBackground(saveCallback);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static void pushOrderNotification(Order order){

        AVQuery avQuery = AVInstallation.getQuery();
        avQuery.whereEqualTo("installationId",order.getInstallationId());


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",order.getObjectId());
            jsonObject.put("action","com.levent_j.UPDATE_STATUS");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AVPush.sendDataInBackground(jsonObject, avQuery, new SendCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    MyLog.d("push send success");
                }else {
                    MyLog.e("push send failed " + e.getMessage());
                }
            }
        });
    }


    /**
     * 减少对应类型的座位数量
     * @param shop
     * @param mType
     * @param saveCallback
     * @throws AVException
     */
//    public static void incrementSeatAmount(Shop shop, String mType,SaveCallback saveCallback) throws AVException {
////        shop = AVObject.createWithoutData(Shop.class,shop.getObjectId());
//        if (mType.equals(Seat.TYPE_SMALL)) {
//            shop.(Shop.SHOP_SEAT_AMOUNT_A);
//        }else if (mType.equals(Seat.TYPE_MEDIUM)){
//            shop.increment(Shop.SHOP_SEAT_AMOUNT_B);
//        }else {
//            shop.increment(Shop.SHOP_SEAT_AMOUNT_C);
//        }
//        shop.saveInBackground(saveCallback);
//    }


}
