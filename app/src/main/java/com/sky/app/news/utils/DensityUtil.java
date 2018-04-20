package com.sky.app.news.utils;

import android.content.Context;

/**
 * Created with Android Studio.
 * 描述: 单位转换工具
 * dp和px互换工具
 * Date: 2018/4/20
 * Time: 9:36
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
