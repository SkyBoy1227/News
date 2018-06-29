package com.sky.app.news;

import android.app.Application;
import android.support.test.espresso.core.internal.deps.guava.util.concurrent.ThreadFactoryBuilder;

import com.sky.app.news.volley.VolleyManager;

import org.xutils.x;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     * 线程池
     */
    public static ExecutorService singleThreadPool;

    /**
     * 所有组件被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(BuildConfig.DEBUG);
        x.Ext.init(this);
        // 初始化Volley
        VolleyManager.init(this);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        singleThreadPool = new ThreadPoolExecutor(10, 15,
                1L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
