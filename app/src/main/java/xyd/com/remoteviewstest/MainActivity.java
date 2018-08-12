package xyd.com.remoteviewstest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    //    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void initNotifycation() {
//        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("Hello world!!")
//                .setContentText("标题")
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true);
//        Intent intent = new Intent(this,DemoActivity_1.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            notification = builder.build();
//        }
//        notificationManager.notify(1,notification);
//
//    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        initNotifycation();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    private void initNotifycation() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
        if (areNotificationsEnabled) {
            Log.d(TAG,"initNotifycation");
            //ChannelId为"1",ChannelName为"Channel1"
            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            showChannel1Notification(MainActivity.this);
        } else {
            Toast.makeText(this,"没有通知权限",0).show();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void showChannel1Notification(Context context) {
//        int notificationId = 0x1234;
//        Notification.Builder builder = new Notification.Builder(context,"1"); //与channelId对应
//        //icon title text必须包含，不然影响桌面图标小红点的展示
//        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setContentTitle("xxx")
//                .setContentText("xxx")
//                .setNumber(3); //久按桌面图标时允许的此条通知的数量
//        Intent intent = new Intent(context,DemoActivity_1.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notificationId, builder.build());
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showChannel1Notification(Context context) {
        int notificationId = 0x1234;
        Notification.Builder builder = new Notification.Builder(context, "1"); //与channelId对应
        //icon title text必须包含，不然影响桌面图标小红点的展示
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("xxx")
                .setContentText("xxx")
                .setNumber(3); //久按桌面图标时允许的此条通知的数量
        Intent intent = new Intent(context, DemoActivity_1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //layout 的跟布局只能使用五种基本布局
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_nofification);
        remoteViews.setTextViewText(R.id.msg,"chapter_5");
        remoteViews.setImageViewResource(R.id.image_icon,R.mipmap.ic_launcher);

        //设置之后点击将会无效
//        remoteViews.setBoolean(R.id.msg,"setEnabled",false);
//        remoteViews.setBoolean(R.id.image_icon,"setEnabled",false);
//        remoteViews.setBoolean(R.id.open_demoActivity_2,"setEnabled",false);

        PendingIntent openDemo2 = PendingIntent.getActivity(context,0,new Intent(context,DemoActivity_2.class),PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.open_demoActivity_2,openDemo2);
        builder.setCustomContentView(remoteViews);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
