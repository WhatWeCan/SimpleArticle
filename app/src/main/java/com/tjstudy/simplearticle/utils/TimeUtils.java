package com.tjstudy.simplearticle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的工具类
 */

public class TimeUtils {
    public static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
}
