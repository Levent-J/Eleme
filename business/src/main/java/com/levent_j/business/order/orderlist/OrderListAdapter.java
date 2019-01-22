package com.levent_j.business.order.orderlist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.DateUtil;
import com.levent_j.business.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.VH>{


    private List<Order> mDatas;

    private IOrderClickCallback clickCallback;

    public OrderListAdapter(IOrderClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        mDatas = new ArrayList<>();
    }

    public void setDatas(List<Order> orders){
        mDatas.clear();
        mDatas.addAll(orders);
        notifyDataSetChanged();
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

    class VH extends RecyclerView.ViewHolder {

        private TextView mSeatNo;
        private TextView mDate;
        private TextView mInfo;
        private TextView mPrice;
        private TextView mStatus;

        public VH(View itemView) {
            super(itemView);

            mSeatNo = itemView.findViewById(R.id.tv_order_item_seat_no);
            mDate = itemView.findViewById(R.id.tv_order_item_date);
            mInfo = itemView.findViewById(R.id.tv_order_item_content);
            mPrice = itemView.findViewById(R.id.tv_order_item_price);
            mStatus = itemView.findViewById(R.id.tv_order_item_status);
        }

        public void bindData(final Order order) {

            mSeatNo.setText("座位号："+order.getSeatName());
            mDate.setText(DateUtil.Date2String(order.getCreatedAt()));
            mInfo.setText(order.getIntro());
            mPrice.setText(order.getTotalPrice());

            if (order.getType() == Order.TYPE_FOOD){
                Pair<String,String> pair = order.getTextByStatus();
                mStatus.setText(pair.first);
                mStatus.setTextColor(Color.parseColor(pair.second));
            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        clickCallback.onClickItem(order.getObjectId());
                }
            });
        }
    }

    public interface IOrderClickCallback{
        void onClickItem(String orderId);
    }
}
