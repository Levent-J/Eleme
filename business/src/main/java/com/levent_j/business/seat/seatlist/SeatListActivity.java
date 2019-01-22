package com.levent_j.business.seat.seatlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.ToastUtils;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyEmptyView;
import com.levent_j.baselibrary.views.MyLoadingDialog;
import com.levent_j.business.QRCodeUtil;
import com.levent_j.business.R;
import com.levent_j.business.views.MyEditFloatingButton;
import com.levent_j.business.views.MyEditSeatDialog;
import com.levent_j.business.seat.seatdetail.MySeatCodeDialog;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/31.
 * @desc :
 */

public class SeatListActivity extends BaseActivity implements View.OnClickListener, ISeatListCallback, SeatListAdapter.ISeatClickCallback, MyEditFloatingButton.IEditFabCallback, MyEditSeatDialog.ISelectCallback {

    private MainTitleBar mainTitleBar;
    private RecyclerView mSeatListView;
    private MyEditFloatingButton mFab;
    private MyEditSeatDialog mEditSeatDialog;
    private MySeatCodeDialog mySeatCodeDialog;
    private MyEmptyView myEmptyView;
    private MyLoadingDialog myLoadingDialog;

    private SeatListAdapter mSeatListAdapter;


    private SeatListPresenter mSeatListPresenter;

    private String mShopId;
    private Shop mShop;


    @Override
    public int setLayout() {
        return R.layout.activity_seat_list;
    }

    @Override
    public void init() {

        mainTitleBar = findViewById(R.id.title_bar);
        mSeatListView = findViewById(R.id.rlv_seat_list);
        mFab = findViewById(R.id.fab);
        myEmptyView = findViewById(R.id.view_empty);

        mFab.setCallBack(this);

        mSeatListView.setLayoutManager(new LinearLayoutManager(this));
        mSeatListView.setHasFixedSize(true);
        mSeatListAdapter = new SeatListAdapter(this);
        mSeatListView.setAdapter(mSeatListAdapter);

        myLoadingDialog = new MyLoadingDialog(this);
        myLoadingDialog.setCancelable(false);

        mSeatListPresenter = new SeatListPresenter(this);
        addPresenter(mSeatListPresenter);

        mShopId = UserAccountManager.getInstance().getmShop().getObjectId();

        initTitleBar();
        initDialog();

        mSeatListView.setVisibility(View.GONE);
        myEmptyView.setVisibility(View.VISIBLE);
        myEmptyView.setMode(MyEmptyView.MODE_LOADING);

        loadSeatData();
        loadShopData();
    }

    private void loadShopData() {
        mSeatListPresenter.getShop(mShopId);
    }

    private void initDialog() {
        mEditSeatDialog = new MyEditSeatDialog(this);
        mEditSeatDialog.setConfirmedCallback(this);
    }

    private void loadSeatData() {
        mSeatListPresenter.loadSeatList(mShopId);
    }

    private void initTitleBar() {
        mainTitleBar.setTitle("座位列表");
        mainTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void openActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, SeatListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadSeatListSuccess(List<Seat> list) {
        MyLog.d("onLoadSeatListSuccess");

        if (list.isEmpty()){
            mSeatListView.setVisibility(View.GONE);
            myEmptyView.setVisibility(View.VISIBLE);
            myEmptyView.setMode(MyEmptyView.MODE_EMPTY);
            myEmptyView.setEmptyContent("没有一个座位");
        }else {
            mSeatListAdapter.setData(list);

            mSeatListView.setVisibility(View.VISIBLE);
            myEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadSeatListFailed(AVException e) {
        MyLog.e("onLoadSeatListFailed " + e.getMessage());

        mSeatListView.setVisibility(View.GONE);
        myEmptyView.setVisibility(View.VISIBLE);
        myEmptyView.setMode(MyEmptyView.MODE_RETRY);
        myEmptyView.setRetryCallvack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSeatData();
            }
        });
    }

    @Override
    public void onGetShopSuccess(Shop shop) {
        MyLog.d("onGetShopSuccess");
        mShop = shop;
    }

    @Override
    public void onGetShopFailed(AVException e) {
        MyLog.e("onGetShopFailed " + e.getMessage());
    }

    @Override
    public void onAddSeatSuccess() {
        loadSeatData();
        myLoadingDialog.dismiss();
    }

    @Override
    public void onAddSeatFailed(AVException e) {
//        loadSeatData();
        ToastUtils.avException(this,e.getCode());
        myLoadingDialog.dismiss();
    }

    @Override
    public void onDelSeatSuccess() {
        loadSeatData();
        myLoadingDialog.dismiss();
    }

    @Override
    public void onDelSeatFailed(AVException e) {
//        loadSeatData();
        ToastUtils.avException(this,e.getCode());
        myLoadingDialog.dismiss();
    }

    @Override
    public void onCantDelSeat() {
        Toast.makeText(this,"当前座位无法删除",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickSeatItem(Seat seat) {
        String str = QRCodeUtil.createContent(UserAccountManager.getInstance().getmShop().getObjectId(), seat.getObjectId());
        Bitmap bitmap = QRCodeUtil.createBitmap(str, 260);

        mySeatCodeDialog = new MySeatCodeDialog(this);
        mySeatCodeDialog.setCodeBitmap(bitmap);
        mySeatCodeDialog.setSeat(seat);
        mySeatCodeDialog.show();
    }

    @Override
    public void onLongClick(String seatId) {

    }

    @Override
    public void onClickAdd() {
        mEditSeatDialog.setMode(MyEditSeatDialog.MODE_ADD);
        mEditSeatDialog.show();
    }

    @Override
    public void onClickDel() {
        mEditSeatDialog.setMode(MyEditSeatDialog.MODE_SUB);
        mEditSeatDialog.show();
    }

    @Override
    public void onAdd(String type) {
        mSeatListPresenter.addSeat(mShopId, type, mShop);
        myLoadingDialog.setmTips("添加中……");
        myLoadingDialog.show();
    }

    @Override
    public void onSub(String type) {
        mSeatListPresenter.subSeat(mShopId, type, mShop);
        myLoadingDialog.setmTips("删除中……");
        myLoadingDialog.show();
    }
}
