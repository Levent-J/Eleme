package com.levent_j.client.shopping.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.client.R;
import com.levent_j.client.common.model.FoodModel;
import com.levent_j.client.common.views.FoodCountView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/19.
 * @desc :
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    private List<FoodModel> mDatas;
    private IClickCarkItemCallback itemCallback;

    public CartAdapter(IClickCarkItemCallback itemCallback) {
        this.itemCallback = itemCallback;
        mDatas = new ArrayList<>();
    }

    public void setDatas(Map<Food,Integer> map) {
        mDatas.clear();
        for (Map.Entry<Food, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 0) {
                mDatas.add(new FoodModel(entry.getKey(), entry.getValue()));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cart_menu, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        FoodModel model = mDatas.get(position);
        holder.bindData(model,position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateData(Food food, int result, int pos) {
        if (result==0){
            mDatas.remove(pos);
            notifyDataSetChanged();
        }else {
            mDatas.get(pos).amount = result;
            mDatas.get(pos).price = food.getPrice()*result;
            notifyDataSetChanged();
        }
    }


    class VH extends RecyclerView.ViewHolder {

        private TextView mFoodName;
        private TextView mFoodPrice;
        private FoodCountView mFoodCountView;

        public VH(View itemView) {
            super(itemView);

            mFoodName = itemView.findViewById(R.id.tv_cart_food_name);
            mFoodPrice = itemView.findViewById(R.id.tv_cart_food_price);
            mFoodCountView = itemView.findViewById(R.id.view_cart_food_count);
        }

        public void bindData(FoodModel model, final int position) {
            mFoodName.setText(model.food.getName());
            mFoodPrice.setText(model.price + "￥");

            mFoodCountView.setFood(model.food);
            mFoodCountView.setClickCallback(new FoodCountView.ICountClickCallback() {
                @Override
                public void onAdd(Food f, int result) {
                    itemCallback.onCartItemAdd(f, result,position);
                }

                @Override
                public void onSub(Food f, int result) {
                    itemCallback.onCartItemSub(f, result,position);
                }
            });
            mFoodCountView.setCount(model.amount);
            mFoodCountView.build();
        }
    }


    interface IClickCarkItemCallback {
        void onCartItemAdd(Food food, int result,int pos);

        void onCartItemSub(Food food, int result,int pos);
    }

}
