package com.tjstudy.simplearticle.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */

public class RegexUtils {
    /**
     * 判断是否是网址
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        String pattern = "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(url);
        return m.find();
    }
}
