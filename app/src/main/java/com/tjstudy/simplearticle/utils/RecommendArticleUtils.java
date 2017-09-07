package com.tjstudy.simplearticle.utils;

import com.tjstudy.simplearticle.bean.Article;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 推荐文章工具类
 */
public class RecommendArticleUtils {

    private Document document;

    /**
     * 获取推荐文章
     *
     * @return
     */
    public List<Article> getRecommendArticles() {
        try {
            document = JSoupUtils.getDocument("http://blog.csdn.net/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Article> articles = new ArrayList<>();
        Elements elements_list_item = document.getElementsByClass("blog_list clearfix");

        int elementsCount = elements_list_item.size();//推荐的文章总数
        for (int i = 0; i < elementsCount; i++) {
            Element element = elements_list_item.get(i);
            Article article = new Article();

            Element element_article_title = element.getElementsByClass("tracking-ad").get(0);
            String article_title_text = element_article_title.text();
            String article_link = element_article_title.getElementsByTag("a").get(0).attr("href");

            Element element_article_description = element.getElementsByClass("blog_list_c").get(0);
            String article_des_text = element_article_description.text();

            Element element_article_fonds = element.getElementsByClass("blog_list_b_l fl").get(0);
            String articleFonds = element_article_fonds.text();

            Element blog_list_b_r = element.getElementsByClass("blog_list_b_r fr").get(0);
            String time = blog_list_b_r.getElementsByTag("label").text();

            article.setTitle(article_title_text);
            String replace = getPhoneUrl(article_link);
            article.setUrl(replace);
            article.setDes(article_des_text);
            String[] split = articleFonds.split(" ");
            article.setFonds(split[0]);

            article.setTime(time);
            articles.add(article);
        }
        return articles;
    }

    private String getPhoneUrl(String url) {
        String[] split = url.split("/");
        String xString = "";
        for (int i = 0; i < 6; i++) {
            if (i == 2) {
                xString += "m." + split[2] + "/";
            } else if (i == 3) {
                continue;
            } else if (i == 5) {
                xString += split[5] + "?id=" + split[6];
            } else {
                xString += split[i] + "/";
            }
        }
        return xString;
    }
}
