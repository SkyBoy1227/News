package com.sky.app.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            try {
                String fileName = MD5Encoder.encode(key);
                File parentFile = new File(Environment.getExternalStorageDirectory(), "news/files");
                if (!parentFile.exists()) {
                    // 创建目录
                    parentFile.mkdirs();
                }
                File file = new File(parentFile, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                // 保存文本数据
                fos.write(value.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据缓存失败");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
            sp.edit().putString(key, value).apply();
        }
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        String result = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ByteArrayOutputStream baos = null;
            try {
                String fileName = MD5Encoder.encode(key);
                File parentFile = new File(Environment.getExternalStorageDirectory(), "news/files");
                File file = new File(parentFile, fileName);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, length);
                    }
                    result = baos.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据获取失败");
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            SharedPreferences sp = context.getSharedPreferences("sky", Context.MODE_PRIVATE);
            result = sp.getString(key, "");
        }
        return result;
    }
}
