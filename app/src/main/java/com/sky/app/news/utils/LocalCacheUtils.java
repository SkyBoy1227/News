package com.sky.app.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with Android Studio.
 * 描述: 本地缓存工具类
 * Date: 2018/6/29
 * Time: 14:27
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class LocalCacheUtils {

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 根据Url获取图片
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        // 判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                File parentFile = new File(Environment.getExternalStorageDirectory(), "news");
                File file = new File(parentFile, fileName);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bitmap != null) {
                        memoryCacheUtils.putBitmap(imageUrl, bitmap);
                        LogUtil.e("把图片从本地保存到内存中");
                        return bitmap;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片获取失败");
            }
        }
        return null;
    }

    /**
     * 根据Url保存图片
     *
     * @param imageUrl url
     * @param bitmap   图片
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                File parentFile = new File(Environment.getExternalStorageDirectory(), "news");
                if (!parentFile.exists()) {
                    // 创建目录
                    parentFile.mkdirs();
                }
                File file = new File(parentFile, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                // 保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地缓存失败");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
