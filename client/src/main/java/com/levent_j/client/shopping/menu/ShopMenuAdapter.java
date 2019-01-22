package com.levent_j.client.shopping.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.client.R;
import com.levent_j.client.common.model.FoodModel;
import com.levent_j.client.common.views.FoodCountView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public class ShopMenuAdapter extends RecyclerView.Adapter<ShopMenuAdapter.VH>{

    private List<FoodModel> mDatas;
    private IClickMenuItemCallback mClickItemCallback;
    private Map<Integer,Integer> tagMap = new HashMap<>();

    public ShopMenuAdapter(IClickMenuItemCallback clickItemCallback) {
        this.mDatas = new ArrayList<>();
        mClickItemCallback = clickItemCallback;
    }

    public void setDatas(List<Food> list){
        mDatas.clear();
        for (Food food : list) {
            mDatas.add(new FoodModel(food,0));
        }
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_menu,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //绑定数据
        FoodModel foodModel = mDatas.get(position);
        holder.bindData(foodModel);

        //通过tag解决item状态错乱
        Integer tag = Integer.valueOf(position);
        holder.itemView.setTag(tag);
        if (tagMap.containsKey(tag)){
            holder.setCount(tagMap.get(tag));
        }else {
            holder.setCount(0);
        }


    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateData(Food food, int result,RecyclerView recyclerView) {
        int index = 0;
        for (; index < mDatas.size(); index++) {
            if (mDatas.get(index).food.getObjectId().equals(food.getObjectId())){
                MyLog.d("更新的是："+mDatas.get(index).food.getName()+" amount " + result);
                break;
            }
        }

        //更新tagmap 否则会被覆盖
        if (tagMap.containsKey(index)){
            tagMap.put(index,result);
        }

        mDatas.get(index).amount = result;

        //完美解决了局部刷新问题!
        VH vh = (VH) recyclerView.findViewHolderForAdapterPosition(index);
        if (vh!=null){
            vh.mCountView.setCount(result);
        }else {
            notifyDataSetChanged();
        }
//        notifyItemChanged(index);

    }

    class VH extends RecyclerView.ViewHolder{

        private ImageView mFoodAvatar;
        private TextView mFoodName;
        private TextView mFoodPrice;
        private FoodCountView mCountView;

        public VH(View itemView) {
            super(itemView);

            mFoodAvatar = itemView.findViewById(R.id.iv_item_food_avatar);
            mFoodName = itemView.findViewById(R.id.tv_item_food_name);
            mFoodPrice = itemView.findViewById(R.id.tv_item_food_price);
            mCountView = itemView.findViewById(R.id.view_item_food_count);
        }

        public void bindData(final FoodModel foodModel){
            mFoodName.setText(foodModel.food.getName());
            mFoodPrice.setText("￥"+foodModel.food.getPrice());

            byte[] bytes = foodModel.food.getAvatar();
            File file = ImageUtil.Bytes2File(bytes);
            Picasso.get().load(file).into(mFoodAvatar);

            mCountView.setFood(foodModel.food);
            mCountView.setClickCallback(new FoodCountView.ICountClickCallback() {
                @Override
                public void onAdd(Food f,int result) {
                    mClickItemCallback.onMenuItemAdd(f,result);
                    tagMap.put((int)itemView.getTag(),result);
                }

                @Override
                public void onSub(Food f,int result) {
                    mClickItemCallback.onMenuItemSub(f,result);
                    tagMap.put((int)itemView.getTag(),result);
                }
            });
            mCountView.setCount(foodModel.amount);
            mCountView.build();

        }

        public int getCount(){
            return mCountView.getCount();
        }

        public void setCount(int count){
            mCountView.setCount(count);
        }

    }

    interface IClickMenuItemCallback {
        void onMenuItemAdd(Food food, int result);
        void onMenuItemSub(Food food, int result);
    }


}
