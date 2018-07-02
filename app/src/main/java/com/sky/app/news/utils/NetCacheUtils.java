package com.sky.app.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.sky.app.news.NewsApplication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with Android Studio.
 * 描述: 网络缓存工具类
 * Date: 2018/6/29
 * Time: 11:32
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NetCacheUtils {
    /**
     * 请求图片成功
     */
    public static final int SUCCESS = 1;

    /**
     * 请求图片失败
     */
    public static final int FAILURE = 2;

    /**
     * 本地缓存工具类
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    private Handler handler;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 联网请求得到图片
     *
     * @param imageUrl
     * @param position
     */
    public void getBitmapFromNet(String imageUrl, int position) {
        // 子线程
        NewsApplication.singleThreadPool.execute(() -> {
            InputStream is = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 只能大写
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                // 可写可不写
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
                    // 显示到控件上,发消息把Bitmap和position发出去
                    Message message = Message.obtain();
                    message.what = SUCCESS;
                    message.arg1 = position;
                    message.obj = bitmap;
                    handler.sendMessage(message);

                    // 在内存中缓存一份
                    memoryCacheUtils.putBitmap(imageUrl, bitmap);

                    // 在本地中缓存一份
                    localCacheUtils.putBitmap(imageUrl, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = FAILURE;
                message.arg1 = position;
                handler.sendMessage(message);
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
