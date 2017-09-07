package com.tjstudy.simplearticle.db;

import android.database.Cursor;

import com.tjstudy.simplearticle.base.MyApplication;
import com.tjstudy.simplearticle.bean.Article;
import com.tjstudy.simplearticle.bean.ArticleDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 对文章的数据库的操作
 */

public class ArticleDbOperate {

    private final ArticleDao articleDao;

    public ArticleDbOperate() {
        articleDao = MyApplication.getDaoSession().getArticleDao();
    }

    /**
     * 向数据库中写入一个文章实体
     *
     * @param article
     */
    public void insertEntity(Article article) {
        articleDao.insert(article);
    }

    /**
     * 向数据库中写入文章集合
     *
     * @param articles
     */
    public void insertArticles(List<Article> articles) {
        for (Article article : articles) {
            articleDao.insert(article);
        }
    }

    /**
     * 删除指定id的文章
     *
     * @param id
     */
    public void removeArticleById(Long id) {
        articleDao.deleteByKey(id);
    }

    /**
     * 修改标题
     *
     * @param id
     * @param title
     * @return
     */
    public boolean modifyArticleTitleById(Long id, String title) {
        Article article = articleDao.queryBuilder()
                .where(ArticleDao.Properties.Id.eq(id)).build().unique();
        if (article == null) {
            return false;
        } else {
            article.setTitle(title);
            articleDao.update(article);
            return true;
        }
    }

    /**
     * 修改描述
     *
     * @param id
     * @param des
     * @return
     */
    public boolean modifyArticleDesById(Long id, String des) {
        Article article = articleDao.queryBuilder()
                .where(ArticleDao.Properties.Id.eq(id)).build().unique();
        if (article == null) {
            return false;
        } else {
            article.setDes(des);
            articleDao.update(article);
            return true;
        }
    }

    /**
     * 获取整个数据库中数据的个数
     *
     * @return
     */
    public Long getArticleCount() {
        return articleDao.count();
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<Article> getArticles(int start, int count) {
        return articleDao.queryBuilder().offset(start).limit(count).list();
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    public List<Article> getArticles() {
        return articleDao.queryBuilder().list();
    }

    /**
     * 根据条件 获取文章
     *
     * @param article
     * @return
     */
    public List<Article> getArticlesByCondition(String article) {
        List<Article> list = articleDao.queryBuilder().where(ArticleDao.Properties.Title.like("%" + article + "%")).list();
        return list;
    }

    private static final String SQL_DISTINCT_FONDS = "SELECT DISTINCT " + ArticleDao.Properties.Fonds.columnName + " FROM " + ArticleDao.TABLENAME;

    /**
     * 获取文章的所有类别
     *
     * @return
     */
    public List<String> getArticlesFonds() {
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = articleDao.getSession().getDatabase().rawQuery(SQL_DISTINCT_FONDS, null);
        try {
            if (c.moveToFirst()) {
                do {
                    result.add(c.getString(0));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    /**
     * 根据类别来获取对应的文章
     *
     * @param fonds
     * @return
     */
    public List<Article> getArticlesByFonds(String fonds) {
        return articleDao.queryBuilder().where(ArticleDao.Properties.Fonds.eq(fonds)).list();
    }
}
