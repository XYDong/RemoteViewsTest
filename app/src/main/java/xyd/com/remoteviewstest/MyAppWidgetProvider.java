package xyd.com.remoteviewstest;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "MyAppWidgetProvider";

    public static final String CLICK_ACTION = "com.xyd.chapter_5.action.CLICK";


    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG,"onReceive : action = " + intent.getAction());

        //这里判断之处理自己的action
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context,"clicked it",Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcbitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_1);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;

                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.image_1,rotateBitmap(context,srcbitmap,degree));
                        Intent intentClick = new Intent();
                        intent.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intentClick,0);
                        remoteViews.setOnClickPendingIntent(R.id.image_1,pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();


        }

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG,"onUpdate");
        int counter = appWidgetIds.length;
        Log.i(TAG,"counter = " +counter );
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onAppWidgetUpate(context,appWidgetManager,appWidgetId);
        }

    }

    /**
     * 桌面小部件更新
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private void onAppWidgetUpate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.i(TAG,"onAppWidgetUpate");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        //桌面小部件 点击事件 发送intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        //若不添加该代码，则在android8.0及以上系统使用时会出现接收不到广播的问题（android8.0 开始广播机制有所变化，sendbroadcast前要指定receiver的类）
        intentClick.setComponent(new ComponentName(context,MyAppWidgetProvider.class));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.image_1,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }

    /**
     * 图片按规律旋转
     * @param context
     * @param srcbitmap
     * @param degree
     * @return
     */
    private Bitmap rotateBitmap(Context context, Bitmap srcbitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcbitmap, 0, 0, srcbitmap.getWidth(), srcbitmap.getHeight(), matrix, true);
        return bitmap;
    }
}
