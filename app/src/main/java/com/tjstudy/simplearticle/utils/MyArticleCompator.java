package com.tjstudy.simplearticle.utils;

import com.tjstudy.simplearticle.bean.Article;

import java.util.Comparator;

/**
 * 个人文章的比较器
 */

public class MyArticleCompator implements Comparator<Article> {
    @Override
    public int compare(Article lhs, Article rhs) {
        String time1 = lhs.getTime();
        String time2 = rhs.getTime();

        String[] time1Split = time1.split(" ");
        String[] time2Split = time2.split(" ");
        int bigTime1 = DataUtils.getBigTime(time1Split[0]);
        int smallTime1 = DataUtils.getSmallTime(time1Split[1]);
        int bigTime2 = DataUtils.getBigTime(time2Split[0]);
        int smallTime2 = DataUtils.getSmallTime(time2Split[1]);

        if (bigTime1 != bigTime2) {
            if (bigTime1 > bigTime2) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (smallTime1 != smallTime2) {
                if (smallTime1 > smallTime2) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }
    }
}
