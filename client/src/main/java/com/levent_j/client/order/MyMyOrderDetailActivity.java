package com.levent_j.client.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.DateUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.client.R;

/**
 * @auther : levent_j on 2018/3/23.
 * @desc :
 */

public class MyMyOrderDetailActivity extends BaseActivity implements IMyOrderDetailCallback, View.OnClickListener {

    private MainTitleBar mMainTitleBar;
    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView mOrderStatus;
    private Button mOrderReminder;
    private Button mOrderCancel;
    private Button mOrderCall;
    private RecyclerView mOrderDetailRlv;
    private ViewGroup mOrderDetail;
    private TextView mOrderShopName;
    private TextView mOrderIntro;
    private TextView mOrderId;
    private TextView mOrderDate;

    public static final String EXTRA_ORDER_ID = "order_id";

    private MyOrderDetailAdapter mMyOrderDetailAdapter;

    private MyOrderDetailPresenter mMyOrderDetailPresenter;
    private Order order;

    @Override
    public int setLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void init() {

        mMainTitleBar = findViewById(R.id.title_bar);
        appBarLayout = findViewById(R.id.app_layout);
        toolbar = findViewById(R.id.toolbar);
        mOrderStatus = findViewById(R.id.tv_order_status_text);
        mOrderReminder = findViewById(R.id.btn_reminder);
        mOrderCancel = findViewById(R.id.btn_cancel);
        mOrderCall = findViewById(R.id.btn_call_service);
        mOrderDetailRlv = findViewById(R.id.rlv_order_detail);
        mOrderDetail = findViewById(R.id.layout_order_detail);
        mOrderShopName = findViewById(R.id.tv_shop_name);
        mOrderIntro = findViewById(R.id.tv_order_intro);
        mOrderId = findViewById(R.id.tv_order_id);
        mOrderDate = findViewById(R.id.tv_order_date);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                MyLog.d("verticalOffset " + verticalOffset);
                if (verticalOffset==0){
                    if (toolbar.getVisibility()!=View.GONE){
                        toolbar.setVisibility(View.GONE);
                    }
                }else {
                    if (toolbar.getVisibility()!=View.VISIBLE){
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mOrderReminder.setOnClickListener(this);
        mOrderCancel.setOnClickListener(this);
        mOrderCall.setOnClickListener(this);



        mOrderDetailRlv.setLayoutManager(new LinearLayoutManager(this));
        mOrderDetailRlv.setHasFixedSize(true);
        mMyOrderDetailAdapter = new MyOrderDetailAdapter();
        mOrderDetailRlv.setAdapter(mMyOrderDetailAdapter);

        mMyOrderDetailPresenter = new MyOrderDetailPresenter(this);

        initTitleBar();

        initData();
    }

    private void initData() {
        String id = getIntent().getStringExtra(EXTRA_ORDER_ID);
        MyLog.d("get id " + id);
        mMyOrderDetailPresenter.getOrderDetail(id);
    }

    private void initTitleBar() {
        mMainTitleBar.setTitle("订单详情");
        mMainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivityFromCreate(BaseActivity activity, String orderId) {
        Intent intent = new Intent(activity, MyMyOrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void openActivityFromList(BaseActivity activity, String orderId) {
        Intent intent = new Intent(activity, MyMyOrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        activity.startActivity(intent);
    }

    @Override
    public void onGetDetailSuccess(Order order) {
        MyLog.d("onGetDetailSuccess ");

        if (order.getType() == Order.TYPE_FOOD){

            mOrderDetail.setVisibility(View.GONE);
            mOrderDetailRlv.setVisibility(View.VISIBLE);

            //订单详情
            mMyOrderDetailAdapter.setOrderDetailData(order);
            if (order.getType() == Order.TYPE_FOOD){
                //订单状态
                Pair<String,String> pair = order.getTextByStatus();
                mOrderStatus.setText(pair.first);
                mOrderStatus.setTextColor(Color.parseColor(pair.second));
            }

        }else {

            mOrderDetail.setVisibility(View.VISIBLE);
            mOrderDetailRlv.setVisibility(View.GONE);

            mOrderShopName.setText(order.getShopName());
            mOrderIntro.setText(order.getIntro());
            mOrderId.setText(order.getObjectId());
            mOrderDate.setText(DateUtil.Date2String(order.getCreatedAt()));
        }
    }

    @Override
    public void onGetDetailFailed(AVException e) {
        MyLog.e("onGetDetailFailed " + e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reminder:
                Toast.makeText(this,"暂未实现催促商家功能~",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                Toast.makeText(this,"暂未实现取消订单功能~",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_call_service:
                Toast.makeText(this,"暂未实现呼叫服务功能~",Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
