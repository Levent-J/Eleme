package com.levent_j.business.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.business.R;
import com.levent_j.business.order.orderlist.OrderListActivity;
import com.levent_j.business.seat.seatlist.SeatListActivity;
import com.levent_j.business.shop.CreateShopActivity;
import com.levent_j.business.login.LoginActivity;
import com.levent_j.business.menu.MenuActivity;
import com.squareup.picasso.Picasso;

import java.io.File;


public class MainActivity extends BaseActivity implements View.OnClickListener,MainCallBack{

    private MainTitleBar mMainTitleBar;

    private LinearLayout mShopCard;
    private LinearLayout mShopNull;

    private Button mCreateShop;
    private ImageView mShopAvatar;
    private TextView mShopName;
    private TextView mShopAddr;
    private TextView mShopPhone;

    private RelativeLayout mMenu;
    private RelativeLayout mSeat;
    private RelativeLayout mOrder;
    private RelativeLayout mInfo;

    private Shop mShop;
    private MainPresenter mainPresenter;

    public static int REQUEST_CODE_CREATE_SHOP = 1;
    public static int REQUEST_CODE_EDIT_SHOP = 2;


    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        if (!UserAccountManager.getInstance().isLogged(this)){
            // 未登陆 则打开登陆页面
            LoginActivity.openActivity(this);
            finish();
            return;
        }


        mMainTitleBar = findViewById(R.id.title_bar);
        mShopCard = findViewById(R.id.ll_shop_card);
        mShopNull = findViewById(R.id.ll_shop_null);
        mCreateShop = findViewById(R.id.btn_shop_create);
        mShopAvatar = findViewById(R.id.iv_shop_avatar);
        mShopName = findViewById(R.id.tv_shop_name);
        mShopAddr = findViewById(R.id.tv_shop_address);
        mShopPhone = findViewById(R.id.tv_shop_phone);
        mMenu = findViewById(R.id.rl_menu);
        mSeat = findViewById(R.id.rl_seat);
        mOrder = findViewById(R.id.rl_order);
        mInfo = findViewById(R.id.rl_info);

        mCreateShop.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mSeat.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mInfo.setOnClickListener(this);

        mainPresenter = new MainPresenter(this);

        addPresenter(mainPresenter);

        loadData();
        initTitleBar();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.d("onActivityResult: requestCode " + requestCode + " resultCode : " + resultCode);
       if ((requestCode == REQUEST_CODE_CREATE_SHOP || requestCode == REQUEST_CODE_EDIT_SHOP)
               && resultCode == RESULT_OK){
           loadData();
        }
    }

    //拉取数据
    private void loadData() {
        //如果用户为空，则重新拉取用户数据
        if (UserAccountManager.getInstance().getmUser()==null){
            mainPresenter.loadUserData(UserAccountManager.getInstance().getUserId(this));
        }else {
            //用户不为空，说明已经成功登陆 开始拉取商铺信息
            mainPresenter.loadShopData();
        }
    }

    private void initTitleBar() {
        mMainTitleBar.getTitle().setText("商家管理");
        mMainTitleBar.getBackBtn().setVisibility(View.GONE);
    }

    public static void openActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_shop_create:
                CreateShopActivity.openActivity(this);
                break;
            case R.id.rl_menu:
                if (checkShop()){
                    MenuActivity.startActivity(this);
                }
                break;
            case R.id.rl_seat:
                if (checkShop()){
                    SeatListActivity.openActivity(this);
                }
                break;
            case R.id.rl_order:
                if (checkShop()){
                    OrderListActivity.openActivity(this);
                }
                break;
            case R.id.rl_info:
                if (checkShop()){
                    CreateShopActivity.openActivity(this,true);
                }
                break;

        }
    }

    private boolean checkShop() {
        boolean able =UserAccountManager.getInstance().getmShop()!=null;
        if (!able){
            Toast.makeText(this,"没有商铺信息",Toast.LENGTH_SHORT).show();
        }
        return able;
    }


    //刷新商铺显示数据
    private void refreshShopData(Shop shop) {
        mShopName.setText(shop.getName());
        mShopAddr.setText(shop.getAddress());
        mShopPhone.setText(shop.getPhone());

        File file = ImageUtil.Bytes2File(shop.getAvatar());
        Picasso.get().load(file).placeholder(R.drawable.add).into(mShopAvatar);
    }

    @Override
    public void onGetShopSuccess(Shop shop) {
        MyLog.d("getShop:"+shop);

        if (shop!=null){
            //有商铺信息 则加载
            mShopCard.setVisibility(View.VISIBLE);
            mShopNull.setVisibility(View.GONE);

            //填充数据
            refreshShopData(shop);

            //保存
            UserAccountManager.getInstance().setmShop(shop);

            mShop = shop;
        }else {
            //没有，则创建
            mShopCard.setVisibility(View.GONE);
            mShopNull.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void onGetShopFailed(AVException e) {
        MyLog.e("onGetShopFailed "+e.getCode());
    }

    @Override
    public void onGetUserDataSuccess() {
        //获取到用户信息
        loadData();
    }

    @Override
    public void onGetUSerDataFailed(AVException e) {
        MyLog.e("onGetUSerDataFailed"+e.getMessage());
    }


}
