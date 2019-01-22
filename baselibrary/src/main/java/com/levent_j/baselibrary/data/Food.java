package com.levent_j.baselibrary.data;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

@AVClassName("Food")
public class Food extends AVObject{

    private String name;
    private double price;
    private String description;
    private int sales;
    private byte[] mAvatar;
    private String shopId;

    public static final String FOOD_NAME = "name";
    public static final String FOOD_PRICE = "price";
    public static final String FOOD_DESC = "description";
    public static final String FOOD_SALES = "sales";
    public static final String FOOD_SHOP_ID = "shopId";
    public static final String FOOD_AVATAR = "avatar";

    public Food() {
        super();
    }

    public Food(Parcel in) {
        super(in);
    }

    public String getName() {
        return getString(FOOD_NAME);
    }

    public void setName(String name) {
        put(FOOD_NAME,name);
    }

    public double getPrice() {
        return getDouble(FOOD_PRICE);
    }

    public void setPrice(double price) {
        put(FOOD_PRICE,price);
    }

    public String getDescription() {
        return getString(FOOD_DESC);
    }

    public void setDescription(String description) {
        put(FOOD_DESC,description);
    }

    public int getSales() {
        return getInt(FOOD_SALES);
    }

    public void setSales(int sales) {
        put(FOOD_SALES,sales);
    }

    public byte[] getAvatar() {
        return getBytes(FOOD_AVATAR);
    }

    public void setAvatar(byte[] mAvatar) {
        put(FOOD_AVATAR,mAvatar);
    }


    public String getShopId() {
        return getString(FOOD_SHOP_ID);
    }

    public void setShopId(String shopId) {
        put(FOOD_SHOP_ID,shopId);
    }

    //此处为我们的默认实现，当然你也可以自行实现
    public static final Creator CREATOR = AVObjectCreator.instance;

    @Override
    public String toString() {
        return "name: " + name + " desc: " + description + " price: " + price + " sales: " + sales;
    }
}
