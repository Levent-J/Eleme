package com.levent_j.baselibrary.data;

import android.os.Parcel;
import android.util.Pair;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.Map;

/**
 * @auther : levent_j on 2018/3/23.
 * @desc :
 */

@AVClassName("Order")
public class Order extends AVObject {

    private int type;//区分：用餐型订单/订座型订单

    //公共属性
    private String shopName;
    private String shopId;
    private String userId;
    private String intro;
    private String seatName;
    private int status;
    private String installationId;

    //仅限于用餐类型
    private Map<String, Integer> foodAmountMap;
    private Map<String, String> foodPriceMap;
    private String totalPrice;
    private String note;

    //仅限于订座类型
    private String seatId;

    public static final String ORDER_MENU = "foodAmountMap";
    public static final String ORDER_INTRO = "intro";
    public static final String ORDER_PRICES = "foodPriceMap";
    public static final String ORDER_TOTAL = "totalPrice";
    public static final String ORDER_SHOP_NAME = "shopName";
    public static final String ORDER_SHOP_ID = "shopId";
    public static final String ORDER_USER_ID = "userId";
    public static final String ORDER_NOTE = "note";
    public static final String ORDER_STATUS = "status";
    public static final String ORDER_TYPE = "type";
    public static final String ORDER_SEAT = "seat";
    public static final String ORDER_SEAT_ID = "seatId";
    public static final String ORDER_INSTALLATION_ID = "installationId";

    public static final int STATUS_ORIGIN = 0;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_WAIT = 2;
    public static final int STATUS_FINISHED = 3;
    public static final int STATUS_CANCELED = 4;
    public static final int STATUS_BOOKED = 5;

    public static final int TYPE_FOOD = 0;
    public static final int TYPE_BOOK = 1;

    public Order() {
    }

    public Order(Parcel in) {
        super(in);
    }

    public String getInstallationId() {
        return getString(ORDER_INSTALLATION_ID);
    }

    public void setInstallationId(String installationId) {
        put(ORDER_INSTALLATION_ID,installationId);
    }

    public String getSeatId() {
        return getString(ORDER_SEAT_ID);
    }

    public void setSeatId(String seatId) {
        put(ORDER_SEAT_ID,seatId);
    }

    public Map<String, Integer> getFoodAmountMap() {
        return getMap(ORDER_MENU);
    }

    public void setFoodAmountMap(Map<String, Integer> foodAmountMap) {
        put(ORDER_MENU, foodAmountMap);
    }

    public String getIntro() {
        return getString(ORDER_INTRO);
    }

    public void setIntro(String intro) {
        put(ORDER_INTRO, intro);
    }

    public String getShopName() {
        return getString(ORDER_SHOP_NAME);
    }

    public void setShopName(String shopName) {
        put(ORDER_SHOP_NAME, shopName);
    }

    public String getShopId() {
        return getString(ORDER_SHOP_ID);
    }

    public Map<String, String> getFoodPriceMap() {
        return getMap(ORDER_PRICES);
    }

    public void setFoodPriceMap(Map<String, String> foodPriceMap) {
        put(ORDER_PRICES, foodPriceMap);
    }

    public String getTotalPrice() {
        return getString(ORDER_TOTAL);
    }

    public void setTotalPrice(String totalPrice) {
        put(ORDER_TOTAL, totalPrice);
    }

    public void setShopId(String shopId) {
        put(ORDER_SHOP_ID, shopId);
    }

    public String getUserId() {
        return getString(ORDER_USER_ID);
    }

    public void setUserId(String userId) {
        put(ORDER_USER_ID, userId);
    }

    public String getNote() {
        return getString(ORDER_NOTE);
    }

    public void setNote(String note) {
        put(ORDER_NOTE, note);
    }

    public int getStatus() {
        return getInt(ORDER_STATUS);
    }

    public void setStatus(int status) {
        put(ORDER_STATUS, status);
    }

    public int getType() {
        return getInt(ORDER_TYPE);
    }

    public void setType(int type) {
        put(ORDER_TYPE,type);
    }

    public String getSeatName() {
        return getString(ORDER_SEAT);
    }

    public void setSeatName(String seatName) {
        put(ORDER_SEAT,seatName);
    }

    /**
     * 根据订单状态值获取订单状态文字显示的颜色和内容
     *
     * @return
     */
    public Pair<String, String> getTextByStatus() {
        switch (getStatus()) {
            case STATUS_ORIGIN:
                return new Pair<>("待确认", "#FF9800");
            case STATUS_CONFIRMED:
                return new Pair<>("备餐中", "#39C5BB");
            case STATUS_WAIT:
                return new Pair<>("待用餐", "#21867f");
            case STATUS_FINISHED:
                return new Pair<>("已完成", "#ce3c45");
            case STATUS_BOOKED:
                return new Pair<>("已预定", "#FFFFFF");
            default:
                return new Pair<>("已取消", "#757575");
        }
    }

    //此处为我们的默认实现，当然你也可以自行实现
    public static final Creator CREATOR = AVObjectCreator.instance;
}
