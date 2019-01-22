package com.levent_j.business.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.business.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/12.
 * @desc :
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH>{

    private List<Food> mDatas;

    private IFoodCallBack mFoodCallBack;

    public MenuAdapter(IFoodCallBack callBack) {
        this.mFoodCallBack = callBack;
        mDatas = new ArrayList<>();
    }

    public void loadData(List<Food> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Food food = mDatas.get(position);
        holder.bindData(food);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder{

        private TextView mFoodName;
        private TextView mFoodDesc;
        private TextView mFoodPrice;
        private TextView mFoodSales;
        private ImageView mFoodAvatar;

        public VH(View itemView) {
            super(itemView);

            mFoodName = itemView.findViewById(R.id.tv_food_name);
            mFoodDesc = itemView.findViewById(R.id.tv_food_desc);
            mFoodPrice = itemView.findViewById(R.id.tv_food_price);
            mFoodSales = itemView.findViewById(R.id.tv_food_sales);
            mFoodAvatar = itemView.findViewById(R.id.iv_food_avatar);


        }

        public void bindData(final Food food){
            mFoodName.setText(food.getName());
            mFoodDesc.setText(food.getDescription());
            mFoodPrice.setText("￥"+food.getPrice());
            mFoodSales.setText("销量"+food.getSales());
            Picasso.get().load(ImageUtil.Bytes2File(food.getAvatar())).into(mFoodAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFoodCallBack.onClick(food);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mFoodCallBack.onLongClick(food);
                    return true;
                }
            });
        }
    }

    public interface IFoodCallBack{
        void onClick(Food food);
        void onLongClick(Food food);
    }
}
