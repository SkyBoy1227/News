package com.sky.app.news;

import android.app.Application;
import android.content.Context;
import android.support.test.espresso.core.internal.deps.guava.util.concurrent.ThreadFactoryBuilder;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sky.app.news.volley.VolleyManager;

import org.xutils.x;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

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

        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(this);

        // 初始化ImageLoader
        initImageLoader(getApplicationContext());

        // 初始化ShareSDK
        MobSDK.init(this);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
