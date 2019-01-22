package com.levent_j.client.main.shoplist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.data.User;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.utils.ImageUtil;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.PermissionUtil;
import com.levent_j.client.R;
import com.levent_j.client.common.util.MyCircleImgTransformation;
import com.levent_j.client.info.MyInfoActivity;
import com.levent_j.client.login.LoginActivity;
import com.levent_j.client.orderlist.MyOrdersActivity;
import com.levent_j.client.main.scan.ScanActivity;
import com.levent_j.client.setting.SettingActivity;
import com.levent_j.client.main.shopdetail.ShopDetailActivity;
import com.levent_j.client.shopping.menu.ShopMenuActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IDrawerCallBack, IMainCallback, ShopListAdapter.IItemClickCallback {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton scan;
    private ImageView mAvater;
    private TextView mName;
    private RecyclerView mShopListView;
    private TextView mShopListNull;

    private ShopListAdapter mShopListAdapter;

    private DrawerPresenter mDrawerPresenter;
    private MainPresenter mMainPresenter;

    private boolean hasPermission = false;


    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.d("onResume");
        //默认打开首页
        if (navigationView != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void init() {
        if (!UserAccountManager.getInstance().isLogged(this)) {
            // 未登陆 则打开登陆页面
            LoginActivity.openActivity(this);
            finish();
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        scan = findViewById(R.id.fab_scan);
        mShopListView = findViewById(R.id.rlv_shop_list);
        mShopListNull = findViewById(R.id.tv_shop_list_null);

        //初始化列表
        mShopListView.setLayoutManager(new GridLayoutManager(this,2));
        mShopListView.setHasFixedSize(true);
        mShopListView.addItemDecoration(new MyItemDecoration(20));
        mShopListAdapter = new ShopListAdapter(this);
        mShopListView.setAdapter(mShopListAdapter);

        //初始化drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //初始化头像
        View header = navigationView.getHeaderView(0);
        mAvater = header.findViewById(R.id.iv_user_avater);
        mName = header.findViewById(R.id.tv_user_name);
        //默认打开首页
        navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(this);
        scan.setOnClickListener(this);

        mDrawerPresenter = new DrawerPresenter(this);
        mMainPresenter = new MainPresenter(this);

        addPresenter(mDrawerPresenter);
        addPresenter(mMainPresenter);


        loadData();

    }

    private void loadData() {
        requestPermission();
        //先加载用户数据
        String id = UserAccountManager.getInstance().getUserId(this);
        mDrawerPresenter.loadUserData(id);
        //拉取商家列表
        mMainPresenter.loadShopList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LoginActivity.openActivity(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (!hasPermission){
            requestPermission();
            return false;
        }
        if (id == R.id.nav_shops) {
            // Handle the camera action
        } else if (id == R.id.nav_orders) {
            MyOrdersActivity.openActivity(this);
        } else if (id == R.id.nav_info) {
            MyInfoActivity.openActivity(this);
        } else if (id == R.id.nav_setting) {
            SettingActivity.openActivity(this);
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_scan:
                if (hasPermission){
                    new IntentIntegrator(this)
                            .setOrientationLocked(false)
                            .setCaptureActivity(ScanActivity.class)
                            .initiateScan();
                }else {
                    requestPermission();
                }
//                ShopMenuActivity.openActivity(this,"5ad353a29f545452c0d93fd6/5addc2fb9f5454779e2bc014");
                break;
        }
    }

    public void requestPermission(){
        //先检查权限
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_CAMERA)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_CAMERA);
            return;
        }
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE);
            return;
        }
        if (!PermissionUtil.checkPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(this,PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }
        hasPermission = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
if (intentResult != null) {
    if (intentResult.getContents() == null){
        Toast.makeText(this,"内容为空",Toast.LENGTH_SHORT).show();
    }else {
        Toast.makeText(this,"扫描成功",Toast.LENGTH_SHORT).show();
        String str = intentResult.getContents();
        MyLog.d("str:" + str);
        ShopMenuActivity.openActivity(this,str);
    }
}
    }

    public static void openActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onLoadUserDataSuccess(User user) {
        mName.setText(user.getUsername());

        byte[] bytes = user.getAvatar();
        if (bytes!=null){
            File file = ImageUtil.Bytes2File(bytes);
            Picasso.get()
                    .load(file)
                    .transform(
                            new MyCircleImgTransformation(88,88))
                    .into(mAvater);
        }


        MyLog.d("  getHeaderCount " + navigationView.getHeaderCount());
    }

    @Override
    public void onLoadUserDataFailed(AVException e) {
        MyLog.e("onLoadUserDataFailed " + e.getMessage());
    }

    @Override
    public void onGetShopListSuccess(List<Shop> shops) {
        MyLog.d("onGetShopListSuccess size: " + shops.size());
        if (shops!=null&&shops.size()>0){
            mShopListAdapter.addDatas(shops);

            mShopListView.setVisibility(View.VISIBLE);
            mShopListNull.setVisibility(View.GONE);
        }else {
            mShopListView.setVisibility(View.GONE);
            mShopListNull.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetShopListFailed(AVException e) {
        MyLog.d("onGetShopListFailed e: " + e.getMessage());
    }

    @Override
    public void onItemClick(Shop shop) {
        if (hasPermission){
            ShopDetailActivity.openActivity(this,shop.getObjectId());
        }else {
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtil.PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //已授予权限
                    hasPermission = true;
                    MyLog.d("OK!!!");
                }
                break;
        }
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration{
        private int space;

        public MyItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildPosition(view);
            if (pos>1){
                outRect.top = space;
            }
            if (pos%2==0){
                outRect.right = space/2;
            }else {
                outRect.left = space/2;
            }
        }
    }
}
