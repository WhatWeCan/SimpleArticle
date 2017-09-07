package com.tjstudy.simplearticle.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tjstudy.simplearticle.bean.DaoMaster;
import com.tjstudy.simplearticle.bean.DaoSession;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.bmob.v3.Bmob;

/**
 * 自定义MyApplication
 */

public class MyApplication extends Application {

    private static Context mAppContext;
    private static String DB_NAME = "article.db";
    private static DaoSession daoSession;

    /*
    * 配置appkey
    */ {
        PlatformConfig.setWeixin("wx0d160eeff9174c04", "a017c5d827ce00de3028297e0158ee1f");
        PlatformConfig.setQQZone("1105976048", "X8EuExoOXYeIsdoe");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        mAppContext = this;
        initDb();
        UMShareAPI.get(this);
        Bmob.initialize(this, "352da04c636df84520de6532ebb0bd46");
    }

    /**
     * 提供ApplicationContext
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        //创建一个 College.db的数据库
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取DaoSession 通过这个DaoSession就能获取到其他各个表的所对应的dao了
     *
     * @return
     */
    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
