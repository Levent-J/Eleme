package com.levent_j.client.shopping.trade;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.utils.DataCache;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.ToastUtils;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyLoadingDialog;
import com.levent_j.client.R;
import com.levent_j.client.order.MyMyOrderDetailActivity;
import com.levent_j.client.shopping.menu.ShopMenuActivity;

import java.util.Map;

/**
 * @auther : levent_j on 2018/3/20.
 * @desc :
 */

public class TradeActivity extends BaseActivity implements TradeOrderAdapter.IItemClickCallback, View.OnClickListener, ITradeCallback {


    private MainTitleBar mMainTitleBar;
    private RecyclerView mOrderListView;
    private TextView mTotalPrice;
    private TextView mSubmitOrder;
    private MyLoadingDialog myLoadingDialog;

    private TradeOrderAdapter mTradeOrderAdapter;

    private TradePresenter mTradePresenter;

    private Shop mShop;
    private Seat mSeat;
    private TradeModel tradeModel;
    private String totalPrice;

    private static final String KEY_FOODS = "key_foods";
    private static final String KEY_SHOP = "key_shop";
    private static final String KEY_PRICE = "key_price";
    private static final String KEY_SEAT = "key_seat_no";

    @Override
    public int setLayout() {
        return R.layout.activity_trade;
    }

    @Override
    public void init() {
        mMainTitleBar = findViewById(R.id.title_bar);
        mOrderListView = findViewById(R.id.rlv_trade_order);
        mTotalPrice = findViewById(R.id.tv_trade_total_price);
        mSubmitOrder = findViewById(R.id.tv_trade_submit);

        mSubmitOrder.setOnClickListener(this);

        //初始化RecyclerView
        mOrderListView.setLayoutManager(new LinearLayoutManager(this));
        mOrderListView.setHasFixedSize(true);
        mTradeOrderAdapter = new TradeOrderAdapter(this);
        mOrderListView.setAdapter(mTradeOrderAdapter);

        myLoadingDialog = new MyLoadingDialog(this);
        myLoadingDialog.setCancelable(false);


        initTitleBar();
        initData();

        mTradePresenter = new TradePresenter(this);
        addPresenter(mTradePresenter);
    }

    private void initData() {
        tradeModel = (TradeModel) DataCache.get(getIntent().getLongExtra(KEY_FOODS,0));
        mShop = (Shop) DataCache.get(getIntent().getLongExtra(KEY_SHOP,0));
        totalPrice = getIntent().getStringExtra(KEY_PRICE);
        mSeat = getIntent().getParcelableExtra(KEY_SEAT);

        mTradeOrderAdapter.setDatas(tradeModel,mShop,mSeat);
        mTotalPrice.setText(totalPrice);
    }

    private void initTitleBar() {
        mMainTitleBar.setTitle("查看");
        mMainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity, Map<Food, Integer> cart, String totalPrice, Shop shop, Seat seat) {
        Intent intent = new Intent(activity, TradeActivity.class);
        TradeModel mapModel = new TradeModel();
        mapModel.setMap(cart);
        intent.putExtra(KEY_FOODS,DataCache.add(mapModel));
        intent.putExtra(KEY_PRICE,totalPrice);
        intent.putExtra(KEY_SHOP,DataCache.add(shop));
        intent.putExtra(KEY_SEAT,seat);
        activity.startActivity(intent);
    }

    @Override
    public void onClickNote() {
        Toast.makeText(this,"添加备注",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_trade_submit:

                mTradePresenter.takeOrder(tradeModel,totalPrice,mShop,"备注",mSeat);

                myLoadingDialog.setmTips("下单中……");
                myLoadingDialog.show();

                break;
        }
    }

    @Override
    public void onTradeSuccess(String orderId) {
        MyLog.d("onTradeSuccess : " + orderId);
        myLoadingDialog.dismiss();

        ShopMenuActivity.used = true;
        MyMyOrderDetailActivity.openActivityFromCreate(this,orderId);
    }

    @Override
    public void onTradeFailed(AVException e) {
        myLoadingDialog.dismiss();
        ToastUtils.avException(this,e.getCode());
        MyLog.e("onTradeFailed : " + e.getMessage());
    }
}
