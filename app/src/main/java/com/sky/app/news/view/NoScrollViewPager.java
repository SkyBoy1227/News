package com.sky.app.news.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created with Android Studio.
 * 描述: 自定义不可以滑动的ViewPager
 * Date: 2018/4/24
 * Time: 9:19
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NoScrollViewPager extends ViewPager {
    /**
     * 通常在代码中实例化的时候用该方法
     *
     * @param context
     */
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类的时候，实例化用该构造方法，这个方法不能少，少的化会崩溃。
     *
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写触摸事件，消费掉
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
