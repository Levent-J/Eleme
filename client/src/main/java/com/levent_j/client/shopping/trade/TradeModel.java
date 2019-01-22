package com.levent_j.client.shopping.trade;

import android.os.Parcel;
import android.os.Parcelable;

import com.levent_j.baselibrary.data.Food;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/21.
 * @desc :
 */

public class TradeModel implements Parcelable {
    private Map<Food,Integer> map;

    public TradeModel() {
    }

    public Map<Food, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Food, Integer> map) {
        this.map = map;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.map.size());
        for (Map.Entry<Food, Integer> entry : this.map.entrySet()) {
            dest.writeParcelable(entry.getKey(), flags);
            dest.writeValue(entry.getValue());
        }
    }

    protected TradeModel(Parcel in) {
        int mapSize = in.readInt();
        this.map = new HashMap<Food, Integer>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            Food key = in.readParcelable(Food.class.getClassLoader());
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.map.put(key, value);
        }
    }

    public static final Creator<TradeModel> CREATOR = new Creator<TradeModel>() {
        @Override
        public TradeModel createFromParcel(Parcel source) {
            return new TradeModel(source);
        }

        @Override
        public TradeModel[] newArray(int size) {
            return new TradeModel[size];
        }
    };
}
