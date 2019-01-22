package com.levent_j.client.shopping.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.levent_j.client.shopping.trade.TradeActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther : levent_j on 2018/3/16.
 * @desc :
 */

public class ShopMenuActivity extends BaseActivity implements View.OnClickListener, IMenuCallback, ShopMenuAdapter.IClickMenuItemCallback, CartAdapter.IClickCarkItemCallback {


    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView mMenuListView;//菜单
    private RecyclerView mCartListView;//购物车
    private FloatingActionButton mCart;//购物车按钮
    private FloatingActionButton mTrade;//交易按钮
    private FloatingActionButton mTradeNone;//禁止交易按钮
    private ShopDetailHeader mHeader;
    private TextView mTotalPrice;
    private BottomSheetBehavior mBottomSheetBehavior;//控制购物车面板
    private RelativeLayout mCartParent;

    private ShopMenuAdapter mShopMenuAdapter;//菜单
    private CartAdapter mCartAdapter;//购物车

    private ShopMenuPresenter mShopMenuPresenter;
    private SeatDataPresenter mSeatDataPresenter;

    private String mShopId;
    private String mSeatId;
    private Shop mShop;
    private Seat mSeat;
    public static boolean used = false;//该座位是否被占用
    private Map<Food, Integer> mCartMap;//购物车列表

    private static final String EXTRA_SHOP_ID = "key_shop_id";
    private static final String EXTRA_SEAT_ID = "key_seat_id";

    @Override
    public int setLayout() {
        return R.layout.activity_shop_menu;
    }

    @Override
    public void init() {
        appBarLayout = findViewById(R.id.app_layout);
        toolbar = findViewById(R.id.toolbar);
        mMenuListView = findViewById(R.id.rlv_shop_menu);
        mCartListView = findViewById(R.id.rlv_cart_list);
        mCart = findViewById(R.id.fab_scan);
        mTrade = findViewById(R.id.fab_trade);
        mTradeNone = findViewById(R.id.fab_trade_none);
        mHeader = findViewById(R.id.view_shop_header);
        mTotalPrice = findViewById(R.id.tv_cark_total_price);
        mCartParent = findViewById(R.id.rl_bottom_sheet_layout);
        mBottomSheetBehavior = BottomSheetBehavior.from(mCartParent);

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

        mCart.setOnClickListener(this);
        mTrade.setOnClickListener(this);
        mHeader.getBackBtn().setOnClickListener(this);

        //初始化bottom sheet的监听
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    changeFabMode(newState);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //初始化菜单RecyclerView
        mMenuListView.setLayoutManager(new GridLayoutManager(this, 2));
        mMenuListView.setHasFixedSize(true);
        mMenuListView.addItemDecoration(new MyItemDecoration(20));
        mShopMenuAdapter = new ShopMenuAdapter(this);
        mMenuListView.setAdapter(mShopMenuAdapter);

        //初始化购物车RecyclerView
        mCartListView.setLayoutManager(new LinearLayoutManager(this));
        mCartListView.setHasFixedSize(true);
        mCartAdapter = new CartAdapter(this);
        mCartListView.setAdapter(mCartAdapter);

        //初始化购物车map
        mCartMap = new HashMap<>();

        mShopId = getIntent().getStringExtra(EXTRA_SHOP_ID);
        mSeatId = getIntent().getStringExtra(EXTRA_SEAT_ID);

        MyLog.d("shopid : " + mShopId + " seatId : " + mSeatId);


        mShopMenuPresenter = new ShopMenuPresenter(this);
        mSeatDataPresenter = new SeatDataPresenter(this);

        addPresenter(mShopMenuPresenter);
        addPresenter(mSeatDataPresenter);

        loadMenuData();
    }

    private void loadSeatData() {
        //先检查座位数据
        mSeatDataPresenter.getSeatData(mSeatId);
    }

    private void loadMenuData() {
        //拉取商铺信息
        mShopMenuPresenter.loadShopDetail(mShopId);
        //拉取商铺的菜单
        mShopMenuPresenter.loadMenu(mShopId);
    }

    public static void openActivity(BaseActivity activity, String str) {
        Intent intent = new Intent(activity, ShopMenuActivity.class);
        MyLog.d("result:" + str);
        String[] strings = str.split("/");
        intent.putExtra(EXTRA_SHOP_ID, strings[0]);
        intent.putExtra(EXTRA_SEAT_ID, strings[1]);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_scan:
                MyLog.d("state:" + mBottomSheetBehavior.getState());
                //先计算要打开多高
//                ViewGroup.LayoutParams params = mCartParent.getLayoutParams();
//                params.height = mCartMap.size() * 40 + 92;
//                mCartParent.setLayoutParams(params);
                //如果处于关闭状态，则打开
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    //打开购物车
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //设置购物车数据源
                    mCartAdapter.setDatas(mCartMap);
                }

                break;
            case R.id.fab_trade:
                TradeActivity.openActivity(this, mCartMap, mTotalPrice.getText().toString(), mShop,mSeat);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onLoadShopDetailSuccess(Shop shop) {
        MyLog.d("onLoadShopDetailSuccess");
        mShop = shop;
        mHeader.initData(shop);
        //商铺信息获取成功之后 才可以拉取座位信息
        loadSeatData();
    }

    @Override
    public void onLoadShopDetailFailed(AVException e) {
        MyLog.e("onLoadShopDetailFailed : " + e.getMessage());
    }

    @Override
    public void onLoadMenuSuccess(List<Food> list) {
        MyLog.d("onLoadMenuSuccess");
        mShopMenuAdapter.setDatas(list);
    }

    @Override
    public void onLoadMenuFailed(AVException e) {
        MyLog.e("onLoadMenuFailed : " + e.getMessage());
    }

    @Override
    public void onGetSeatDataSuccess(Seat seat) {
        MyLog.d("onGetSeatDataSuccess");

        mSeat = seat;

        String userId = UserAccountManager.getInstance().getUserId(this);
        if (seat.getSemaphore() == 1 ||
                seat.getBookedId().equals(userId)) {
            if (seat.getSemaphore() == 1){
                //该座位空闲
                //获取信号量
                mSeatDataPresenter.obtainSemaphore(mShop, seat);
            }else {
                //该座位被自己预定
                //修改座位状态为：就餐
                mSeatDataPresenter.updateSeatStatus(seat,Seat.STATUS_DINING);
                //更新对应订单
                mSeatDataPresenter.updateOrderStatus(mShop,userId,seat);
            }
        } else {//该座位被其他人预定
            new AlertDialog.Builder(this)
                    .setTitle("抱歉")
                    .setMessage("该座位已被预订，请换一个座位，谢谢")
                    .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();

        }
    }

    @Override
    public void onGetSeatDataFailed(AVException e) {
        MyLog.e("onGetSeatDataFailed " + e.getMessage());
    }

    @Override
    public void onMenuItemAdd(Food food, int result) {
        //更新购物车map
        mCartMap.put(food, result);
        //更新总价
        refreshTotalPrice();
        //更新购物车
        mCartAdapter.setDatas(mCartMap);
    }

    @Override
    public void onMenuItemSub(Food food, int result) {
        if (result==0){
            //如果为0 则删除这个菜
            mCartMap.remove(food);
        }else {
            //更新购物车map
            mCartMap.put(food, result);
        }
        //更新总价
        refreshTotalPrice();
        //更新购物车
        mCartAdapter.setDatas(mCartMap);
    }

    @Override
    public void onCartItemAdd(Food food, int result, int pos) {
        //更新购物车map
        mCartMap.put(food, result);
        //更新总价
        refreshTotalPrice();
        //更新菜单
        mShopMenuAdapter.updateData(food, result,mMenuListView);
        //更新购物车
        mCartAdapter.updateData(food, result, pos);
    }

    @Override
    public void onCartItemSub(Food food, int result, int pos) {
        if (result==0){
            //如果为0 则删除这个菜
            mCartMap.remove(food);
        }else {
            //更新购物车map
            mCartMap.put(food, result);
        }
        //展示总价
        refreshTotalPrice();
        //更新菜单
        mShopMenuAdapter.updateData(food, result,mMenuListView);
        //更新购物车
        mCartAdapter.updateData(food, result, pos);
    }


    /**
     * 计算总价
     */
    public void refreshTotalPrice() {
        double price = 0.00;
        for (Map.Entry<Food, Integer> entry : mCartMap.entrySet()) {
            price += entry.getKey().getPrice() * entry.getValue();
        }
        mTotalPrice.setText(price + "￥");

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED &&
                (price == 0.00)) {
            mTradeNone.setVisibility(View.VISIBLE);
            mTrade.setVisibility(View.GONE);
            mCart.setVisibility(View.GONE);
        }
    }

    public void changeFabMode(int mBottomSheetState) {
        if (mBottomSheetState == BottomSheetBehavior.STATE_COLLAPSED) {//选餐状态
            mCart.setVisibility(View.VISIBLE);
            mTrade.setVisibility(View.GONE);
            mTradeNone.setVisibility(View.GONE);
        } else if (mBottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {//结算状态
            mCart.setVisibility(View.GONE);
            mTrade.setVisibility(View.VISIBLE);
            mTradeNone.setVisibility(View.GONE);

            refreshTotalPrice();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!used) {
            //如果没有被使用，则还需要释放信号量
            mSeatDataPresenter.freeSemphore(mShop, mSeat);
            used = false;
        }

    }


    class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public MyItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildPosition(view);
            if (pos % 2 == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                outRect.right = space;
                outRect.left = space / 2;
            }

            if (pos > 1) {
                outRect.top = space;
            }
        }
    }

}
