package com.levent_j.business.seat.seatlist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Seat;
import com.levent_j.business.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public class SeatListAdapter extends RecyclerView.Adapter<SeatListAdapter.VH>{

    private List<Seat> mDatas;
    private ISeatClickCallback mSeatClickCallback;

    public SeatListAdapter(ISeatClickCallback seatClickCallback) {
        this.mSeatClickCallback = seatClickCallback;
        mDatas = new ArrayList<>();
    }

    public void setData(List<Seat> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_seat,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Seat seat = mDatas.get(position);
        holder.bindData(seat);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder {

        private TextView mNumber;
        private TextView mStatus;
        private ImageView mCode;

        public VH(View itemView) {
            super(itemView);

            mNumber = itemView.findViewById(R.id.tv_item_seat_number);
            mStatus = itemView.findViewById(R.id.tv_item_seat_status);
            mCode = itemView.findViewById(R.id.iv_item_seat_code);

        }

        public void bindData(final Seat seat) {

            mNumber.setText(seat.getType() + seat.getNumber());
            Pair<String,String> pair = seat.getTextByStatus();
            mStatus.setText(pair.first);
            mStatus.setTextColor(Color.parseColor(pair.second));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSeatClickCallback.onClickSeatItem(seat);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mSeatClickCallback.onLongClick(seat.getObjectId());
                    return true;
                }
            });

        }
    }

    public interface ISeatClickCallback{
        void onClickSeatItem(Seat seat);
        void onLongClick(String seatId);
    }
}
