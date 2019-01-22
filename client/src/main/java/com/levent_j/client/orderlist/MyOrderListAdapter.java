package com.levent_j.client.orderlist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.DateUtil;
import com.levent_j.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/24.
 * @desc :
 */

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.VH>{

    private List<Order> mDatas;
    private IOrderItemClickCallback callback;

    public MyOrderListAdapter(IOrderItemClickCallback callback) {
        this.mDatas = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_list,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Order order = mDatas.get(position);
        holder.bindData(order);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<Order> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{

        private TextView mShopName;
        private TextView mDate;
        private TextView mContent;
        private TextView mPrice;
        private TextView mStatus;

        public VH(View itemView) {
            super(itemView);

            mShopName = itemView.findViewById(R.id.tv_order_item_shop_name);
            mDate = itemView.findViewById(R.id.tv_order_item_date);
            mContent = itemView.findViewById(R.id.tv_order_item_content);
            mPrice = itemView.findViewById(R.id.tv_order_item_price);
            mStatus = itemView.findViewById(R.id.tv_order_item_status);
        }

        public void bindData(final Order order) {
            mShopName.setText(order.getShopName());
            mDate.setText(DateUtil.Date2String(order.getCreatedAt()));
            mContent.setText(""+order.getIntro());
            mPrice.setText(order.getTotalPrice());


            if (order.getType() == Order.TYPE_FOOD){
                Pair<String,String> pair = order.getTextByStatus();
                mStatus.setText(pair.first);
                mStatus.setTextColor(Color.parseColor(pair.second));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClickOrder(order.getObjectId());
                }
            });
        }
    }

    interface IOrderItemClickCallback {
        void onClickOrder(String id);
    }
}
