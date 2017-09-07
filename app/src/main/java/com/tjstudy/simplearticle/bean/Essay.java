package com.tjstudy.simplearticle.bean;

import android.support.annotation.IntDef;

import cn.bmob.v3.BmobObject;

/**
 * 随笔 实体
 */
public class Essay extends BmobObject {
    public static final int TYPE_TXT = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_RADIO = 3;

    @EssayType
    private Integer eType;//随笔类型
    private String eContent;//随笔文本
    private String eTime;//随笔时间
    private String eUrl;//随笔资源地址 文本没有资源地址

    public Integer geteType() {
        return eType;
    }

    public void seteType(@EssayType Integer eType) {
        this.eType = eType;
    }

    public String geteContent() {
        return eContent;
    }

    public void seteContent(String eContent) {
        this.eContent = eContent;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String geteUrl() {
        return eUrl;
    }

    public void seteUrl(String eUrl) {
        this.eUrl = eUrl;
    }

    @IntDef({TYPE_PIC, TYPE_TXT, TYPE_RADIO})
    public @interface EssayType {
    }
}
