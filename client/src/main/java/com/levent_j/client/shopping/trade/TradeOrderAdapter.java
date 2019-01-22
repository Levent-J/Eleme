package com.levent_j.client.shopping.trade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.client.R;
import com.levent_j.client.common.model.FoodModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/21.
 * @desc :
 */

public class TradeOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FoodModel> mMenuDatas;
    private Shop mShop;
    private Seat mSeat;
    private IItemClickCallback mClickCallback;

    private static final int ITEM_TYPE_MENU = 0;
    private static final int ITEM_TYPE_ORDER = 1;


    public TradeOrderAdapter(IItemClickCallback mClickCallback) {
        this.mClickCallback = mClickCallback;
        mMenuDatas = new ArrayList<>();
    }

    public void setDatas(TradeModel tradeModel, Shop shop,Seat seat){

        //先初始化菜单数据
        mMenuDatas.clear();
        Map<Food,Integer> map = tradeModel.getMap();
        for (Map.Entry<Food,Integer> entry : map.entrySet()){
            mMenuDatas.add(new FoodModel(entry.getKey(),entry.getValue()));
        }

        //在初始化订单数据
        mShop = shop;
        mSeat = seat;

        //刷新
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mMenuDatas.size()) {
            return ITEM_TYPE_MENU;
        } else {
            return ITEM_TYPE_ORDER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_MENU) {
            View view = inflater.inflate(R.layout.item_trade_menu, parent, false);
            return new MenuVH(view);
        } else {
            View view = inflater.inflate(R.layout.item_trade_order, parent, false);
            return new OrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_MENU) {
            FoodModel foodModel = mMenuDatas.get(position);
            ((MenuVH) holder).bindFoodData(foodModel);
        } else {
            ((OrderVH) holder).bindTradeData(mShop,mSeat);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuDatas.size() + 1;
    }

    class MenuVH extends RecyclerView.ViewHolder {

        private TextView mFoodName;
        private TextView mFoodAmount;
        private TextView mFoodPrice;

        public MenuVH(View itemView) {
            super(itemView);

            mFoodName = itemView.findViewById(R.id.tv_trade_food_name);
            mFoodAmount = itemView.findViewById(R.id.tv_trade_food_amount);
            mFoodPrice = itemView.findViewById(R.id.tv_trade_food_price);
        }

        public void bindFoodData(FoodModel model) {
            mFoodName.setText(model.food.getName());
            mFoodAmount.setText("× " + model.amount);
            mFoodPrice.setText(model.price + " ￥");
        }
    }

    class OrderVH extends RecyclerView.ViewHolder {

        private TextView mShopName;
        private TextView mOrderNote;
        private TextView mOrderSeat;

        public OrderVH(View itemView) {
            super(itemView);

            mShopName = itemView.findViewById(R.id.tv_trade_shop_name);
            mOrderNote = itemView.findViewById(R.id.tv_trade_shop_note);
            mOrderSeat = itemView.findViewById(R.id.tv_trade_shop_seat);
        }

        public void bindTradeData(Shop shop,Seat mSeat) {
            mShopName.setText(shop.getName());
            mOrderNote.setText("备注：无");
            mOrderSeat.setText("座位号 "+mSeat.getType() + mSeat.getNumber());

            mOrderNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickCallback.onClickNote();
                }
            });
        }
    }

    interface IItemClickCallback {
        void onClickNote();
    }

}
