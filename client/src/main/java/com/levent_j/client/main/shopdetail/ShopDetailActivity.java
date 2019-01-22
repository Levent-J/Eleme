package com.levent_j.client.main.shopdetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.data.Seat;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.client.R;
import com.levent_j.client.common.views.ShopDetailHeader;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/15.
 * @desc :
 */

public class ShopDetailActivity extends BaseActivity implements IShopDetailCallback, View.OnClickListener {

    private ShopDetailHeader mShopDetailHeader;
    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView mMenuListView;
    private TextView mAmountA;
    private TextView mAmountB;
    private TextView mAmountC;
    private TextView mBookA;
    private TextView mBookB;
    private TextView mBookC;

    private String mShopId;
    private Shop mShop;

    private ShopDetailMenuAdapter mShopDetailMenuAdapter;
    private ShopDetailPresenter mShopDetailPresenter;

    private static final String EXTRA_SHOP_ID = "key_shop_id";

    @Override
    public int setLayout() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void init() {

        mShopDetailHeader = findViewById(R.id.view_shop_header);
        appBarLayout = findViewById(R.id.app_layout);
        toolbar = findViewById(R.id.toolbar);
        mMenuListView = findViewById(R.id.rlv_shop_menu);
        mAmountA = findViewById(R.id.tv_seatA_amount);
        mAmountB = findViewById(R.id.tv_seatB_amount);
        mAmountC = findViewById(R.id.tv_seatC_amount);
        mBookA = findViewById(R.id.tv_seatA_book);
        mBookB = findViewById(R.id.tv_seatB_book);
        mBookC = findViewById(R.id.tv_seatC_book);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMenuListView.setLayoutManager(linearLayoutManager);
        mMenuListView.setHasFixedSize(true);
        mShopDetailMenuAdapter = new ShopDetailMenuAdapter();
        mMenuListView.setAdapter(mShopDetailMenuAdapter);

        mBookA.setOnClickListener(this);
        mBookB.setOnClickListener(this);
        mBookC.setOnClickListener(this);
        mShopDetailHeader.getBackBtn().setOnClickListener(this);

        mShopId = getIntent().getStringExtra(EXTRA_SHOP_ID);

        mShopDetailPresenter = new ShopDetailPresenter(this);
        addPresenter(mShopDetailPresenter);

        loadShopDetail();
        loadShopMenu();
    }

    private void loadShopDetail() {
        mShopDetailPresenter.getShopDetail(mShopId);
    }

    private void loadShopMenu(){
        mShopDetailPresenter.getShopMenu(mShopId);
    }

    public static void openActivity(BaseActivity activity, String objectId) {
        Intent intent = new Intent(activity, ShopDetailActivity.class);
        intent.putExtra(EXTRA_SHOP_ID, objectId);
        activity.startActivity(intent);
    }

    @Override
    public void onGetShopDetailSuccess(Shop shop) {
        MyLog.d("onGetShopDetailSuccess");
        mShopDetailHeader.initData(shop);
        String format = "(当前剩余 %s 桌)";
        mAmountA.setText(String.format(format,shop.getSeatAvailableAmountA()));
        mAmountB.setText(String.format(format,shop.getSeatAvailableAmountB()));
        mAmountC.setText(String.format(format,shop.getSeatAvailableAmountC()));
        mShop =shop;
    }

    @Override
    public void onGetShopDetailFailed(AVException e) {
        MyLog.e("onGetShopDetailFailed " + e.getMessage());
    }

    @Override
    public void onGetMenuSuccess(List<Food> foods) {
        MyLog.d("onGetMenuSuccess");
        mShopDetailMenuAdapter.setDatas(foods);
    }

    @Override
    public void onGetMenuFailed(AVException e) {
        MyLog.e("onGetMenuFailed " + e.getMessage());
    }

    @Override
    public void onSeatFindSuccess(final Seat seat) {
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage(String.format("当前选择的座位是 %s ",seat.getType() + seat.getNumber()))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mShopDetailPresenter.takeOrder(mShop,UserAccountManager.getInstance().getmUser(),seat);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onSeatFindFailed(AVException e) {

    }

    @Override
    public void onTakeOrderSuccess(String type) {
        TextView tvBook;
        MyLog.d("onTakeOrderSuccess");
        if (type.equals(Seat.TYPE_SMALL)){
            tvBook = mBookA;
        }else if (type.equals(Seat.TYPE_MEDIUM)){
            tvBook = mBookB;
        }else {
            tvBook = mBookC;
        }

        tvBook.setText("已预订");
        tvBook.setTextColor(getResources().getColor(R.color.color_text_white));
        tvBook.setBackground(getResources().getDrawable(R.drawable.bg_booked));
        tvBook.setClickable(false);

    }

    @Override
    public void onTakeOrderFailed(AVException e) {
        MyLog.e("onTakeOrderFailed " + e.getMessage());
    }

    @Override
    public void onBlockSeatSuccess() {
        MyLog.d("onBlockSeatSuccess");
        loadShopDetail();
    }

    @Override
    public void onBlockSeatFailed(AVException e) {
        MyLog.e("onBlockSeatFailed " + e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_seatA_book:
                mShopDetailPresenter.findAvaliableSeat(mShopId,Seat.TYPE_SMALL);
                break;
            case R.id.tv_seatB_book:
                mShopDetailPresenter.findAvaliableSeat(mShopId,Seat.TYPE_MEDIUM);
                break;
            case R.id.tv_seatC_book:
                mShopDetailPresenter.findAvaliableSeat(mShopId,Seat.TYPE_LARGE);
                break;
        }
    }
}
