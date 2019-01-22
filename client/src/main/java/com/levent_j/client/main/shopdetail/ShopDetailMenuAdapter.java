package com.levent_j.client.main.shopdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.client.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/4/8.
 * @desc :
 */
public class ShopDetailMenuAdapter extends RecyclerView.Adapter<ShopDetailMenuAdapter.VH> {

    private List<Food> mDatas;

    public ShopDetailMenuAdapter() {
        this.mDatas = new ArrayList<>();
    }

    public void setDatas(List<Food> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_detail_menu, parent, false);
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

    class VH extends RecyclerView.ViewHolder {

        ImageView mFoodAvatar;
        TextView mFoodName;
        TextView mFoodPrice;

        public VH(View itemView) {
            super(itemView);

            mFoodAvatar = itemView.findViewById(R.id.iv_item_food_avatar);
            mFoodName = itemView.findViewById(R.id.tv_item_food_name);
            mFoodPrice = itemView.findViewById(R.id.tv_item_food_price);
        }

        public void bindData(Food food) {

            byte[] bytes = food.getAvatar();
            File file = ImageUtil.Bytes2File(bytes);
            Picasso.get().load(file).into(mFoodAvatar);

            mFoodName.setText(food.getName());
            mFoodPrice.setText(food.getPrice() + "￥");

        }
    }
}
