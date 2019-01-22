package com.levent_j.client.main.shoplist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.client.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.VH>{

    private List<Shop> mDatas;
    private IItemClickCallback mItemClickCallback;

    public ShopListAdapter(IItemClickCallback mItemClickCallback) {
        mDatas = new ArrayList<>();
        this.mItemClickCallback = mItemClickCallback;
    }

    public void addDatas(List<Shop> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Shop shop = mDatas.get(position);
        holder.bindData(shop);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder{

        private ImageView mShopAvater;
        private TextView mShopName;

        public VH(View itemView) {
            super(itemView);

            mShopAvater = itemView.findViewById(R.id.iv_item_shop_avater);
            mShopName = itemView.findViewById(R.id.tv_item_shop_name);
        }

        public void bindData(final Shop shop){

            mShopName.setText(shop.getName());

            byte[] bytes = shop.getAvatar();
            if (bytes!=null){
                File file = ImageUtil.Bytes2File(bytes);
                Picasso.get().load(file).into(mShopAvater);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickCallback.onItemClick(shop);
                }
            });
        }
    }

    interface IItemClickCallback{
        void onItemClick(Shop shop);
    }
}
