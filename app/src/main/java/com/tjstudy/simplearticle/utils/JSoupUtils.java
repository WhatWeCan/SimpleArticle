package com.tjstudy.simplearticle.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Jsoup的工具类
 */

public class JSoupUtils {
    private static Document document;

    /**
     * 获取meta的内容
     *
     * @param document
     * @param attr
     * @return
     */
    public static String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        return null;
    }

    /**
     * 获取document
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDocument(String url) throws IOException {
        document = Jsoup.connect(url)
                .timeout(5 * 60 * 1000)
                .data("query", "java")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2896.3 Safari/537.36")
                .get();
        return document;
    }
}
