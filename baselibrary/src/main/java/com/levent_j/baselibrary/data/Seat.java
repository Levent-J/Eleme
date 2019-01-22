package com.levent_j.baselibrary.data;

import android.os.Parcel;
import android.util.Pair;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

@AVClassName("Seat")
public class Seat extends AVObject {

    private int number;
    private String type;
    private int status;
    private String shopId;
    private int semaphore;
    private String bookedId;

    public static final String SEAT_NUMBER = "number";
    public static final String SEAT_TYPE = "type";
    public static final String SEAT_STATUS = "status";
    public static final String SEAT_SHOP_ID = "shopId";
    public static final String SEAT_SEMAPHORE = "semaphore";
    public static final String SEAT_BOOKED_ID = "bookedId";

    public static final int STATUS_UNABLE = -1;//不可用
    public static final int STATUS_FREE = 0;//空闲
    public static final int STATUS_BOOKED = 1;//已被预订
    public static final int STATUS_DINING = 2;//正在就餐

    public static final String TYPE_SMALL = "A";
    public static final String TYPE_MEDIUM = "B";
    public static final String TYPE_LARGE = "C";

    public Seat() {
    }

    public Seat(Parcel in) {
        super(in);
    }

    public String getBookedId() {
        return getString(SEAT_BOOKED_ID);
    }

    public void setBookedId(String bookedId) {
        put(SEAT_BOOKED_ID,bookedId);
    }

    public int getNumber() {
        return getInt(SEAT_NUMBER);
    }

    public void setNumber(int number) {
        put(SEAT_NUMBER,number);
    }

    public String getType() {
        return getString(SEAT_TYPE);
    }

    public void setType(String type) {
        put(SEAT_TYPE,type);
    }

    public int getStatus() {
        return getInt(SEAT_STATUS);
    }

    public void setStatus(int status) {
        put(SEAT_STATUS,status);
    }

    public String getShopId() {
        return getString(SEAT_SHOP_ID);
    }

    public void setShopId(String shopId) {
        put(SEAT_SHOP_ID,shopId);
    }

    public int getSemaphore() {
        return getInt(SEAT_SEMAPHORE);
    }

    public void setSemaphore(int semaphore) {
        put(SEAT_SEMAPHORE,semaphore);
    }

    public void freeSemaphore(){
        setSemaphore(1);
    }

    public void obtainSemaphore(){
        setSemaphore(0);
    }

    /**
     * 根据座位状态值获取座位状态文字显示的颜色和内容
     *
     * @return
     */
    public Pair<String, String> getTextByStatus() {
        switch (getStatus()) {
            case STATUS_FREE:
                return new Pair<>("空闲", "#39C5BB");
            case STATUS_BOOKED:
                return new Pair<>("已被预定", "#FF9800");
            case STATUS_DINING:
                return new Pair<>("正在就餐", "#ce3c45");
            default:
                return new Pair<>("不可用", "#757575");
        }
    }

    //此处为我们的默认实现，当然你也可以自行实现
    public static final Creator CREATOR = AVObjectCreator.instance;
}
