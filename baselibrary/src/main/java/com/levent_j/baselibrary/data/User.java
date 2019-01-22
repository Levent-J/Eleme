package com.levent_j.baselibrary.data;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;

/**
 * @auther : levent_j on 2018/3/11.
 * @desc :
 */

@AVClassName("User")
public class User extends AVUser{

    private byte[] avatar;

    public static final String USER_AVATAR = "avatar";

    public User() {
        super();
    }

    public User(Parcel in) {
        super(in);
    }

    public byte[] getAvatar() {
        return getBytes(USER_AVATAR);
    }

    public void setAvatar(byte[] avatar) {
        put(USER_AVATAR,avatar);
    }

    //此处为我们的默认实现，当然你也可以自行实现
    public static final Creator CREATOR = AVObjectCreator.instance;
}
