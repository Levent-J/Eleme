package com.levent_j.business.order.orderdetail;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.utils.DateUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderDetailActivity extends BaseActivity implements IOrderDetailCallback, OrderControlView.IOrderControlCallback {

    private MainTitleBar mainTitleBar;
    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private OrderControlView mOrderControlView;
    private RecyclerView mOrderDetailRlv;
    private ViewGroup mOrderDetail;
    private TextView mOrderShopName;
    private TextView mOrderIntro;
    private TextView mOrderId;
    private TextView mOrderDate;

    private static final String EXTRA_ORDER_ID = "key_order_id";

    private OrderDetailPresenter mOrderDetailPresenter;
    private OrderDetailAdapter mOrderDetailAdapter;

    private Order mOrder;
    private Seat mSeat;

    @Override
    public int setLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void init() {

        mainTitleBar = findViewById(R.id.title_bar);
        appBarLayout = findViewById(R.id.app_layout);
        toolbar = findViewById(R.id.toolbar);
        mOrderControlView = findViewById(R.id.view_order_control);
        mOrderDetailRlv = findViewById(R.id.rlv_order_detail);

        mOrderDetail = findViewById(R.id.layout_order_detail);
        mOrderShopName = findViewById(R.id.tv_shop_name);
        mOrderIntro = findViewById(R.id.tv_order_intro);
        mOrderId = findViewById(R.id.tv_order_id);
        mOrderDate = findViewById(R.id.tv_order_date);

        mOrderControlView.setCallBack(this);

        mOrderDetailRlv.setLayoutManager(new LinearLayoutManager(this));
        mOrderDetailRlv.setHasFixedSize(true);
        mOrderDetailAdapter = new OrderDetailAdapter();
        mOrderDetailRlv.setAdapter(mOrderDetailAdapter);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (toolbar.getVisibility() != View.GONE) {
                        toolbar.setVisibility(View.GONE);
                    }
                } else {
                    if (toolbar.getVisibility() != View.VISIBLE) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mOrderDetailPresenter = new OrderDetailPresenter(this);
        addPresenter(mOrderDetailPresenter);

        initTitleBar();
        loadOrderData();
    }

    private void initTitleBar() {
        mainTitleBar.setTitle("订单详情");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadOrderData() {
        String id = getIntent().getStringExtra(EXTRA_ORDER_ID);
        mOrderDetailPresenter.getOrderDetail(id);
    }

    public static void openActivity(BaseActivity activity, String orderId) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        activity.startActivity(intent);
    }

    @Override
    public void onGetOrderDetailSuccess(Order order) {
        MyLog.d("onGetOrderDetailSuccess");
        this.mOrder = order;
        //填充数据
        //Order状态
        mOrderControlView.setStatus(order, order.getStatus());

        if (order.getType() == Order.TYPE_BOOK){
            mOrderDetail.setVisibility(View.VISIBLE);
            mOrderDetailRlv.setVisibility(View.GONE);

            mOrderShopName.setText(order.getShopName());
            mOrderIntro.setText(order.getIntro());
            mOrderId.setText(order.getObjectId());
            mOrderDate.setText(DateUtil.Date2String(order.getCreatedAt()));

        }else {
            mOrderDetail.setVisibility(View.GONE);
            mOrderDetailRlv.setVisibility(View.VISIBLE);
            //Order详情
            mOrderDetailAdapter.setOrderDetailData(order);
        }

        mOrderDetailPresenter.getSeatDetail(order.getSeatId());
    }

    @Override
    public void onGetOrderDetailFailed(AVException e) {
        MyLog.e("onGetOrderDetailFailed");
    }

    @Override
    public void onOrderStatusUpdateSuccess(String orderId) {
        MyLog.d("onOrderStatusUpdateSuccess");
        loadOrderData();
        //订单状态已更新 需要发出推送

        //推送通知
        mOrderDetailPresenter.pushNotification(mOrder);
    }

    @Override
    public void onOrderStatusUpdateFailed(AVException e) {
        MyLog.e("onOrderStatusUpdateFailed " + e.getMessage());

    }

    @Override
    public void onGetSeatDetailSuccess(Seat seat) {
        MyLog.d("onGetSeatDetailSuccess");
        mSeat = seat;
    }

    @Override
    public void onGetSeatDetailFailed(AVException e) {
        MyLog.e("onGetSeatDetailFailed " + e.getMessage());
    }

    @Override
    public void onClick0() {
        mOrderDetailPresenter.updateOrderConfirmed(mOrder);
    }

    @Override
    public void onClick1() {
        mOrderDetailPresenter.updateOrderWait(mOrder);
    }

    @Override
    public void onClick2() {
        mOrderDetailPresenter.updateOrderFinished(mOrder,mSeat);
    }
}
