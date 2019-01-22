package com.levent_j.client.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.levent_j.baselibrary.utils.MyLog;
import com.levent_j.client.R;
import com.levent_j.client.order.MyMyOrderDetailActivity;
import com.levent_j.client.router.RouterActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @auther : levent_j on 2018/4/22.
 * @desc :
 */
public class MyOrderReceiver extends BroadcastReceiver {

    private static final String TAG = MyOrderReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getExtras().getString("com.avos.avoscloud.Data");
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString("id");
            MyLog.d(TAG, id);
            //此处应该弹出通知

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("提示")
                            .setContentText("您的订单状态有更新")
                            .setDefaults(Notification.DEFAULT_ALL);

            Intent resultIntent = new Intent(context, RouterActivity.class);
            resultIntent.putExtra(RouterActivity.KEY_CHANNEL, RouterActivity.CHANNEL_ORDER_DETAIL);
            resultIntent.putExtra(RouterActivity.KEY_DATA, id);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            Notification notification = mBuilder.build();

            int mNotificationId = 001;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(mNotificationId, notification);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
