package com.tjstudy.simplearticle.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DataUtils {

    /**
     * 返回时间 2017年02月10日 17:04:23
     *
     * @return
     */
    public static String getNowData() {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 2017年02月10日
     * 返回20170210
     *
     * @param time
     * @return
     */
    public static int getBigTime(String time) {
        Log.e("DataUtils", time);
        return Integer.parseInt(time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10));
    }

    /**
     * 17:04:12返回170412
     *
     * @param time
     * @return
     */
    public static int getSmallTime(String time) {
        String[] split = time.split(":");
        return Integer.parseInt(split[0] + split[1] + split[2]);
    }
}
