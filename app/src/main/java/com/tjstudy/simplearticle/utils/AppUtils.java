package com.tjstudy.simplearticle.utils;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.tjstudy.simplearticle.base.MyApplication;

import static android.content.Context.WINDOW_SERVICE;

/**
 * app级别的工具
 */

public class AppUtils {
    /**
     * 获取屏幕的宽高
     *
     * @return
     */
    public static Point getWindowPoint() {
        //获取屏幕的宽
        WindowManager windowManager = (WindowManager) MyApplication.getAppContext().getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }
}
