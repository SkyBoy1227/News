package com.sky.app.news.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created with Android Studio.
 * 描述: 图片三级缓存的工具类
 * Date: 2018/6/28
 * Time: 17:33
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class BitmapCacheUtils {

    /**
     * 网络缓存工具类
     */
    private NetCacheUtils netCacheUtils;

    /**
     * 本地缓存工具类
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler, localCacheUtils, memoryCacheUtils);
    }

    /**
     * 三级缓存设计步骤：
     *   * 从内存中取图片
     *   * 从本地文件中取图片
     *        向内存中保持一份
     *   * 请求网络图片，获取图片，显示到控件上,Hanlder,postion
     *      * 向内存存一份
     *      * 向本地文件中存一份
     *
     * @param imageUrl
     * @param position
     * @return
     */
    public Bitmap getBitmap(String imageUrl, int position) {
        // 1.从内存中取图片
        Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(imageUrl);
        if (bitmap != null) {
            LogUtil.e("内存加载图片成功==" + position);
            return bitmap;
        }

        // 2.从本地文件中取图片
        bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
        if (bitmap != null) {
            LogUtil.e("本地加载图片成功==" + position);
            return bitmap;
        }

        // 3.请求网络图片
        netCacheUtils.getBitmapFromNet(imageUrl, position);
        return null;
    }
}
