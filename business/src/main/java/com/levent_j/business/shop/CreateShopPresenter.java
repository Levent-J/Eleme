package com.levent_j.business.shop;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.levent_j.baselibrary.base.BasePresenter;
import com.levent_j.baselibrary.data.Shop;
import com.levent_j.baselibrary.manager.UserAccountManager;
import com.levent_j.baselibrary.net.ApiService;
import com.levent_j.baselibrary.utils.ImageUtil;

import java.io.File;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class CreateShopPresenter extends BasePresenter{

    private ICreateCallBack mCreateCallBack;

    public CreateShopPresenter(ICreateCallBack mCreateCallBack) {
        this.mCreateCallBack = mCreateCallBack;
    }

    /**
     * 创建一家商铺
     * @param name
     * @param address
     * @param phone
     */
    public void createShop(String name, String address, String phone, File avatar){
        //添加商铺老板
        ApiService.createShop(name,address,phone, ImageUtil.File2Bytes(avatar),
                UserAccountManager.getInstance().getmUser().getObjectId(),
                new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mCreateCallBack.onCreateSuccess();
                }else {
                    mCreateCallBack.onCreateFailed(e);
                }
            }
        });
    }

    /**
     * 编辑店铺信息
     * @param shopId
     * @param name
     * @param address
     * @param phone
     * @param avatar
     */
    public void editShop(String shopId,String name,String address,String phone,File avatar){
        ApiService.editShop(shopId, name, address, phone, ImageUtil.File2Bytes(avatar), new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    mCreateCallBack.onEditSuccess();
                }else {
                    mCreateCallBack.onEditFailed(e);
                }
            }
        });
    }

    public void createUserShop(Shop shop){

    }

    @Override
    public void onDestroy() {

    }
}
