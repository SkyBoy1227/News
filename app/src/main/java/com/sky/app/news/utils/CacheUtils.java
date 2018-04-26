package com.sky.app.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with Android Studio.
 * 描述: 缓存软件的一些参数和数据
 * Date: 2018/4/19
 * Time: 11:08
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class CacheUtils {

    /**
     * 得到缓存值
     *
     * @param context 上下文
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 保存软件参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 缓存文本数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
