package com.levent_j.business.order.orderlist;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyEmptyView;
import com.levent_j.business.R;
import com.levent_j.business.order.orderdetail.OrderDetailActivity;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/29.
 * @desc :
 */

public class OrderListActivity extends BaseActivity implements OrderListAdapter.IOrderClickCallback, IOrderListCallback {

    private MainTitleBar titleBar;
    private RecyclerView mOrderListView;
    private MyEmptyView myEmptyView;

    private OrderListAdapter mOrderListAdapter;
    private OrderListPresenter mOrderListPresenter;

    @Override
    public int setLayout() {
        return R.layout.activity_order_list;
    }

    @Override
    public void init() {
        titleBar = findViewById(R.id.title_bar);
        mOrderListView = findViewById(R.id.rlv_order_list);
        myEmptyView = findViewById(R.id.view_empty);

        mOrderListView.setLayoutManager(new LinearLayoutManager(this));
        mOrderListView.setHasFixedSize(true);
        mOrderListView.addItemDecoration(new MyItemDecoration(20));
        mOrderListAdapter = new OrderListAdapter(this);
        mOrderListView.setAdapter(mOrderListAdapter);

        mOrderListPresenter = new OrderListPresenter(this);
        addPresenter(mOrderListPresenter);

        initTitleBar();
        loadData();
    }

    private void loadData() {
        mOrderListView.setVisibility(View.GONE);
        myEmptyView.setVisibility(View.VISIBLE);
        myEmptyView.setMode(MyEmptyView.MODE_LOADING);

        mOrderListPresenter.getOrderList();
    }

    private void initTitleBar() {
        titleBar.setTitle("订单列表");
        titleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity){
        Intent intent = new Intent(activity,OrderListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClickItem(String orderId) {
        OrderDetailActivity.openActivity(this,orderId);
    }

    @Override
    public void onGetOrderListSuccess(List<Order> orders) {
        MyLog.d("onGetOrderListSuccess");
        if (orders.isEmpty()){
            myEmptyView.setMode(MyEmptyView.MODE_EMPTY);
            myEmptyView.setEmptyContent("没有订单");
        }else {
            mOrderListView.setVisibility(View.VISIBLE);
            myEmptyView.setVisibility(View.GONE);
            mOrderListAdapter.setDatas(orders);
        }
    }

    @Override
    public void onGetOrderListFailed(AVException e) {
        MyLog.e("onGetOrderListFailed " + e.getMessage());

        myEmptyView.setMode(MyEmptyView.MODE_RETRY);
        myEmptyView.setRetryCallvack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration{
        private int space;

        public MyItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
}
