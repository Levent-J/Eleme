package com.levent_j.client.common.model;

import com.levent_j.baselibrary.data.Food;

/**
 * @auther : levent_j on 2018/3/20.
 * @desc :
 */

public class FoodModel {
    public Food food;
    public int amount;
    public double price;

    public FoodModel(Food food, int amount) {
        this.food = food;
        this.amount = amount;
        this.price = amount * food.getPrice();
    }
}
