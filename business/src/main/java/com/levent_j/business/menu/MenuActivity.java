package com.levent_j.business.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.data.Food;
import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.baselibrary.utils.ToastUtils;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.baselibrary.views.MyEmptyView;
import com.levent_j.baselibrary.views.MyLoadingDialog;
import com.levent_j.business.R;
import com.levent_j.business.food.EditFoodActivity;

import java.util.List;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class MenuActivity extends BaseActivity implements IMenuCallBack, MenuAdapter.IFoodCallBack, View.OnClickListener {

    private MainTitleBar titleBar;
    private RecyclerView mMenuListView;
    private ImageView mAdd;
    private MyEmptyView mEmptyView;
    private MyLoadingDialog myLoadingDialog;

    private MenuAdapter mMenuAdapter;

    private MenuPresenter mMenuPresenter;
    private AlertDialog dialog;
    private Food selectedFood;

    public static final int REQUSET_CODE_CREATE = 1;
    public static final int REQUSET_CODE_EDIT = 2;

    @Override
    public int setLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void init() {

        titleBar = findViewById(R.id.title_bar);
        mMenuListView = findViewById(R.id.rlv_menu);
        mAdd = findViewById(R.id.btn_edit_closed);
        mEmptyView = findViewById(R.id.view_empty);

        mMenuAdapter = new MenuAdapter(this);
        mMenuListView.setLayoutManager(new LinearLayoutManager(this));
        mMenuListView.setHasFixedSize(true);
        mMenuListView.setItemAnimator(new DefaultItemAnimator());
        mMenuListView.addItemDecoration(new MyItemDecoration(20));
        mMenuListView.setAdapter(mMenuAdapter);

        mAdd.setOnClickListener(this);

        mMenuPresenter = new MenuPresenter(this);
        addPresenter(mMenuPresenter);


        initTitleBar();
        initDialog();
        loadData();
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否删除该食物？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedFood != null) {
                            myLoadingDialog.setmTips("删除中……");
                            myLoadingDialog.show();
                            mMenuPresenter.delFood(selectedFood);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();


        myLoadingDialog = new MyLoadingDialog(this);
        myLoadingDialog.setCancelable(false);
    }


    private void loadData() {
        //拉取所有食品
        mMenuPresenter.loadMenu();

        mEmptyView.setMode(MyEmptyView.MODE_LOADING);
        mEmptyView.setVisibility(View.VISIBLE);
        mMenuListView.setVisibility(View.GONE);
        myLoadingDialog.dismiss();
    }

    private void initTitleBar() {
        titleBar.getTitle().setText("菜单管理");
        titleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,MenuActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.d("MenuActivity onActivityResult : requestCode = " + requestCode + " resultCode " + resultCode );
        if (requestCode == REQUSET_CODE_CREATE && resultCode == RESULT_OK){
            loadData();
        }else if (requestCode == REQUSET_CODE_EDIT && resultCode == RESULT_OK){
            loadData();
        }
    }

    @Override
    public void onLoadFoodsSuccess(List<Food> foods) {
        MyLog.d("onLoadFoodsSuccess "+foods.size());
        if (foods!=null&&foods.size()>0){
            mMenuAdapter.loadData(foods);

            mMenuListView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }else {
            //数据为空
            mMenuListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setEmptyContent("没有食物诶");
        }
    }

    @Override
    public void onLoadFoodsFailed(AVException e) {
        MyLog.e(" onLoadFoodsFailed " + e.getMessage());
        mEmptyView.setMode(MyEmptyView.MODE_RETRY);
        mEmptyView.setRetryCallvack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    public void onAddFoodSuccess() {

    }

    @Override
    public void onAddFoodFailed() {

    }

    @Override
    public void onDelFoodSuccess() {
        MyLog.d("onDelFoodSuccess");
        loadData();
    }

    @Override
    public void onDelFoodFailed(AVException e) {
        ToastUtils.avException(this,e.getCode());
        myLoadingDialog.dismiss();
        MyLog.e("onDelFoodFailed : " + e.getMessage());
    }

    @Override
    public void onEditFoodSuccess() {

    }

    @Override
    public void onEditFoodFailed() {

    }

    @Override
    public void onClick(Food food) {
        EditFoodActivity.openActivity(this,food);
    }

    @Override
    public void onLongClick(Food food) {
        selectedFood = food;
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_closed:
                EditFoodActivity.openActivity(this);
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

            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
}
