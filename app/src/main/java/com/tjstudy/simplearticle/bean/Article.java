package com.tjstudy.simplearticle.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 文章 实体
 */

@Entity
public class Article {
    @Id
    private Long id;
    @Property
    private String title;
    @Property
    private String des;
    @Property
    private String url;
    @Property
    private String fonds;
    @Property
    private String time;
    @Generated(hash = 1561426187)
    public Article(Long id, String title, String des, String url, String fonds,
            String time) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.url = url;
        this.fonds = fonds;
        this.time = time;
    }
    @Generated(hash = 742516792)
    public Article() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDes() {
        return this.des;
    }
    public void setDes(String des) {
        this.des = des;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getFonds() {
        return this.fonds;
    }
    public void setFonds(String fonds) {
        this.fonds = fonds;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
