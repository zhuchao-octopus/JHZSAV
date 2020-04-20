package com.zhuchao.android.tianpu.utils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zhuchao.android.tianpu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时钟更新
 * Created by Oracle on 2017/12/2.
 */

public class TimeHandler {

    private static final String TAG = TimeHandler.class.getSimpleName();
    private Context context;
    private AtomicBoolean netScan = new AtomicBoolean(false);
    private static final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private String[] weekArr;
    private StringBuilder timeBuilder = new StringBuilder();

    public TimeHandler(Context context) {
        this.context = context;
        weekArr = MyApplication.res().getStringArray(R.array.week);
//        new TimeScanTask().start();
//        handleTime();
    }

    public void regTimeReceiver() {
        handleTime();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        context.registerReceiver(timeReceiver, intentFilter);
    }

    public void unRegTimeReceiver() {
        context.unregisterReceiver(timeReceiver);
    }

    public void release() {
        context = null;
        netScan = null;
    }

    public interface OnTimeDateListener {
        void onTimeDate(String time, String date);
    }

    private OnTimeDateListener listener;

    public void setOnTimeDateListener(OnTimeDateListener listener) {
        this.listener = listener;
    }

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
//            new TimeScanTask().start();
            handleTime();

        }
    };

    private synchronized void handleTime() {
        Calendar calendar = Calendar.getInstance();
        timeBuilder.setLength(0);
        String timeDateStr = isTime24(context)?simpleDateFormat1.format(calendar.getTime()):simpleDateFormat2.format(calendar.getTime());
        final String[] timeStr = timeDateStr.split(" ");
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String weekStr = weekIndex < 0 ? weekArr[0] : weekArr[weekIndex];
        timeBuilder.append(timeStr[1]).append("#").append(weekStr);
//        if (context != null) {
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
        if (listener != null) {
            listener.onTimeDate(timeBuilder.toString(), timeStr[0]);
        }
//                }
//            });
//        }
    }

    class TimeScanTask extends Thread {

        @Override
        public void run() {

//            try {
//                Document doc = Jsoup.connect("https://time.is/").get();
//                Elements elements = doc.select("div#twd");
//                Log.d(TAG, "elements size " + elements.size());
//                for (Element element : elements) {
//                    Log.d(TAG, element.text());
//                }
//                elements = doc.select("div#dd");
//                Log.d(TAG, "elements size " + elements.size());
//                for (Element element : elements) {
//                    Log.d(TAG, element.text());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            handleTime();
        }
    }


    public static boolean isTime24(Context ctx) {
        ContentResolver cv = ctx.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);
        if (strTimeFormat != null && strTimeFormat.equals("24")) {//strTimeFormat某些rom12小时制时会返回null
            return true;
        } else return false;

    }




}
