package com.zhuchao.android.tianpu.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class ScreenUtils {
    // private static int mRootLayoutHeight =0;
    /**
     * 获取手机屏幕高度
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     */
    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }

    public interface NavigationListener {
        void show();

        void hide();
    }
    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }


}
