package com.levent_j.client.orderlist;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Order;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyEmptyView;
import com.levent_j.client.R;
import com.levent_j.client.order.MyMyOrderDetailActivity;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class MyOrdersActivity extends BaseActivity implements IMyOrderListCallback, MyOrderListAdapter.IOrderItemClickCallback {

    private MainTitleBar mainTitleBar;
    private RecyclerView mOrderListView;
    private MyEmptyView myEmptyView;

    private MyOrderListAdapter mOrderListAdapter;

    private OrderListPresenter mOrderListPresenter;

    @Override
    public int setLayout() {
        return R.layout.activity_my_orders;
    }

    @Override
    public void init() {
        mainTitleBar = findViewById(R.id.title_bar);
        mOrderListView = findViewById(R.id.rlv_order_list);
        myEmptyView = findViewById(R.id.view_empty);

        mOrderListView.setLayoutManager(new LinearLayoutManager(this));
        mOrderListView.setHasFixedSize(true);
        mOrderListView.addItemDecoration(new MyItemDecoration(10));
        mOrderListAdapter = new MyOrderListAdapter(this);
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

        String userId = UserAccountManager.getInstance().getmUser().getObjectId();
        mOrderListPresenter.loadOrderList(userId);
    }

    private void initTitleBar() {
        mainTitleBar.setTitle("订单列表");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity){
        Intent intent = new Intent(activity,MyOrdersActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onGetOrderListSuccess(List<Order> list) {
        MyLog.d("onGetOrderListSuccess ");

        if (list.isEmpty()){
            myEmptyView.setMode(MyEmptyView.MODE_EMPTY);
            myEmptyView.setEmptyContent("没有订单啊");
        }else {
            myEmptyView.setVisibility(View.GONE);
            mOrderListView.setVisibility(View.VISIBLE);
            mOrderListAdapter.setDatas(list);
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

    @Override
    public void onClickOrder(String id) {
        MyMyOrderDetailActivity.openActivityFromList(this,id);
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
