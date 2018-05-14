package com.sky.app.news;

import android.app.Application;

import com.sky.app.news.volley.VolleyManager;

import org.xutils.x;

/**
 * Created with Android Studio.
 * 描述: 代表整个软件
 * Date: 2018/4/23
 * Time: 10:49
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NewsApplication extends Application {
    /**
     * 所有组件被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(BuildConfig.DEBUG);
        x.Ext.init(this);
        VolleyManager.init(this);
    }
}
