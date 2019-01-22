package com.levent_j.business.order.orderdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.DateUtil;
import com.levent_j.business.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/26.
 * @desc :
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_TITLE = 0;
    private static final int ITEM_TYPE_FOOD = 1;
    private static final int ITEM_TYPE_TOTAL = 2;
    private static final int ITEM_TYPE_INFO = 3;

    private List<FoodItem> mFoods;
    private Order mOrder;

    public OrderDetailAdapter() {
        this.mFoods = new ArrayList<>();
    }

    public void setOrderDetailData(Order order) {
        this.mOrder = order;

        mFoods.clear();
        Map<String, Integer> foodAmount = order.getFoodAmountMap();
        Map<String,String> foodPrices = order.getFoodPriceMap();
        for (Map.Entry<String, Integer> entry : foodAmount.entrySet()) {
            FoodItem foodItem = new FoodItem(""+entry.getKey(), ""+entry.getValue(), ""+foodPrices.get(entry.getKey()));
            mFoods.add(foodItem);
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_TITLE) {
            View view = inflater.inflate(R.layout.item_order_detail_title, parent, false);
            return new TitleVH(view);
        } else if (viewType == ITEM_TYPE_FOOD) {
            View view = inflater.inflate(R.layout.item_order_detail_food, parent, false);
            return new FoodVH(view);
        } else if (viewType == ITEM_TYPE_TOTAL) {
            View view = inflater.inflate(R.layout.item_order_detail_total, parent, false);
            return new TotalVH(view);
        } else {
            View view = inflater.inflate(R.layout.item_order_detail_info, parent, false);
            return new InfoVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mOrder == null){
            return;
        }
        if (holder.getItemViewType() == ITEM_TYPE_TITLE) {
            ((TitleVH) holder).bindSeatNo("A01");
        } else if (holder.getItemViewType() == ITEM_TYPE_TOTAL) {
            ((TotalVH) holder).bindTotalPrice(mOrder.getTotalPrice());
        } else if (holder.getItemViewType() == ITEM_TYPE_INFO) {
            ((InfoVH) holder).bindInfoData(mOrder);
        } else {
            ((FoodVH) holder).bindFoodData(mFoods.get(position -1));
        }
    }

    @Override
    public int getItemCount() {
        return mFoods.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TITLE;
        } else if (position == mFoods.size() +1) {
            return ITEM_TYPE_TOTAL;
        } else if (position == mFoods.size() + 2) {
            return ITEM_TYPE_INFO;
        } else {
            return ITEM_TYPE_FOOD;
        }
    }


    static class TitleVH extends RecyclerView.ViewHolder {

        private TextView mSeatNo;

        public TitleVH(View itemView) {
            super(itemView);

            mSeatNo = itemView.findViewById(R.id.tv_seat_no);
        }

        public void bindSeatNo(String name) {
            mSeatNo.setText(name);
        }
    }

    static class FoodVH extends RecyclerView.ViewHolder {

        private TextView mFoodName;
        private TextView mFoodAmount;
        private TextView mFoodPrice;

        public FoodVH(View itemView) {
            super(itemView);

            mFoodName = itemView.findViewById(R.id.tv_food_name);
            mFoodAmount = itemView.findViewById(R.id.tv_food_amount);
            mFoodPrice = itemView.findViewById(R.id.tv_food_price);
        }

        public void bindFoodData(FoodItem foodItem) {
            mFoodName.setText(foodItem.name);
            mFoodAmount.setText("X" + foodItem.amount);
            mFoodPrice.setText(foodItem.price);
        }
    }


    static class TotalVH extends RecyclerView.ViewHolder {

        private TextView mTotalPrice;

        public TotalVH(View itemView) {
            super(itemView);

            mTotalPrice = itemView.findViewById(R.id.tv_total_price);
        }

        public void bindTotalPrice(String price) {
            mTotalPrice.setText(price);
        }
    }

    static class InfoVH extends RecyclerView.ViewHolder {

        private TextView mOrderId;
        private TextView mOrderDate;

        public InfoVH(View itemView) {
            super(itemView);
            mOrderId = itemView.findViewById(R.id.tv_order_info_id);
            mOrderDate = itemView.findViewById(R.id.tv_order_info_date);
        }

        public void bindInfoData(Order order) {
            mOrderId.setText(order.getObjectId());
            mOrderDate.setText(DateUtil.Date2String(order.getCreatedAt()));
        }

    }

    static class FoodItem {
        public String name;
        public String amount;
        public String price;

        public FoodItem(String name, String amount, String price) {
            this.name = name;
            this.amount = amount;
            this.price = price;
        }
    }
}
