package com.levent_j.client.main.scan;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.levent_j.baselibrary.base.BaseActivity;
import com.levent_j.baselibrary.views.MainTitleBar;
import com.levent_j.client.R;

/**
 * @auther : levent_j on 2018/3/14.
 * @desc :
 */

public class ScanActivity extends BaseActivity {

    private MainTitleBar mTitleBar;
    private DecoratedBarcodeView mDBView;

    private CaptureManager mCaptureManager;

    @Override
    public int setLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mCaptureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void init() {
        mTitleBar = findViewById(R.id.title_bar);
        mDBView = findViewById(R.id.dbv_scan);

        initTitleBar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCaptureManager = new CaptureManager(this,mDBView);
        mCaptureManager.initializeFromIntent(getIntent(),savedInstanceState);
        mCaptureManager.decode();
    }

    private void initTitleBar() {
        mTitleBar.getTitle().setText("扫码点餐");
        mTitleBar.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
