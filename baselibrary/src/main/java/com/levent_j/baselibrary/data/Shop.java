package com.levent_j.baselibrary.data;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

@AVClassName("Shop")
public class Shop extends AVObject{


    public static final String SHOP_NAME = "name";
    public static final String SHOP_ADDRESS = "address";
    public static final String SHOP_PHONE = "phone";
    public static final String SHOP_MASTER_ID = "masterId";
    public static final String SHOP_SEAT_AMOUNT_A = "seatAmountA";
    public static final String SHOP_SEAT_AMOUNT_B = "seatAmountB";
    public static final String SHOP_SEAT_AMOUNT_C = "seatAmountC";
    public static final String SHOP_SEAT_AVAILABLE_AMOUNT_A = "seatAvailableAmountA";
    public static final String SHOP_SEAT_AVAILABLE_AMOUNT_B = "seatAvailableAmountB";
    public static final String SHOP_SEAT_AVAILABLE_AMOUNT_C = "seatAvailableAmountC";
    public static final String SHOP_AVATAR = "avatar";

    private String name;
    private String address;
    private String phone;
    private String masterId;
    private int seatAmountA;
    private int seatAmountB;
    private int seatAmountC;
    private byte[] avatar;
    private int seatAvailableAmountA;
    private int seatAvailableAmountB;
    private int seatAvailableAmountC;

    public Shop() {
        super();
    }

    public Shop(Parcel in) {
        super(in);
    }



    public String getName() {
        return getString(SHOP_NAME);
    }

    public void setName(String name) {
        put(SHOP_NAME,name);
    }

    public String getAddress() {
        return getString(SHOP_ADDRESS);
    }

    public void setAddress(String address) {
        put(SHOP_ADDRESS,address);
    }

    public String getPhone() {
        return getString(SHOP_PHONE);
    }

    public void setPhone(String phone) {
        put(SHOP_PHONE,phone);
    }

    public String getMasterId() {
        return getString(SHOP_MASTER_ID);
    }

    public void setMasterId(String masterId) {
        put(SHOP_MASTER_ID,masterId);
    }

    public int getSeatAmountA() {
        return getInt(SHOP_SEAT_AMOUNT_A);
    }

    public void setSeatAmountA(int amount) {
        put(SHOP_SEAT_AMOUNT_A,amount);
    }

    public int getSeatAmountB() {
        return getInt(SHOP_SEAT_AMOUNT_B);
    }

    public void setSeatAmountB(int amount) {
        put(SHOP_SEAT_AMOUNT_B,amount);
    }

    public int getSeatAmountC() {
        return getInt(SHOP_SEAT_AMOUNT_C);
    }

    public void setSeatAmountC(int amount) {
        put(SHOP_SEAT_AMOUNT_C,amount);
    }

    public byte[] getAvatar() {
        return getBytes(SHOP_AVATAR);
    }

    public void setAvatar(byte[] avatar) {
        put(SHOP_AVATAR,avatar);
    }

    public int getSeatAvailableAmountA() {
        return getInt(SHOP_SEAT_AVAILABLE_AMOUNT_A);
    }

    public void setSeatAvailableAmountA(int seatAvailableAmountA) {
        put(SHOP_SEAT_AVAILABLE_AMOUNT_A,seatAvailableAmountA);
    }

    public int getSeatAvailableAmountB() {
        return getInt(SHOP_SEAT_AVAILABLE_AMOUNT_B);
    }

    public void setSeatAvailableAmountB(int seatAvailableAmountB) {
        put(SHOP_SEAT_AVAILABLE_AMOUNT_B,seatAvailableAmountB);
    }

    public int getSeatAvailableAmountC() {
        return getInt(SHOP_SEAT_AVAILABLE_AMOUNT_C);
    }

    public void setSeatAvailableAmountC(int seatAvailableAmountC) {
        put(SHOP_SEAT_AVAILABLE_AMOUNT_C,seatAvailableAmountC);
    }

    //此处为我们的默认实现，当然你也可以自行实现
    public static final Creator CREATOR = AVObjectCreator.instance;
}
