package com.sky.app.news.utils;

/**
 * Created with Android Studio.
 * 描述: 常量类，配置联网请求地址
 * Date: 2018/4/24
 * Time: 16:10
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class Constants {
    /**
     * ip地址
     */
    public static final String HOST = "172.16.0.40";

    /**
     * 联网请求服务器的基本地址
     */
    public static final String BASE_URL = "http://" + HOST + ":8080/web_home";

    /**
     * 新闻中心的网络地址
     */
    public static final String NEWSCENTER_PAGER_URL = BASE_URL + "/static/api/news/categories.json";
}
