package com.sky.app.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sky.app.news.R;
import com.sky.app.news.fragment.ContentFragment;
import com.sky.app.news.fragment.LeftMenuFragment;

/**
 * Created with Android Studio.
 * 描述: 主页
 * Date: 2018/4/19
 * Time: 17:03
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFT_MENU_TAG = "left_menu_tag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSlidingMenu();

        initFragments();
    }

    /**
     * 初始化侧滑菜单
     */
    private void initSlidingMenu() {
        // 1.设置主页
        setContentView(R.layout.activity_main);

        // 2.设置左侧菜单
        setBehindContentView(R.layout.activity_left_menu);

        // 3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_right_menu);

        // 4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        // 5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 得到屏幕的宽度
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        // 6.设置主页占据的宽度：屏幕的2/3
        slidingMenu.setBehindOffset(width * 2 / 3);
    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        // 1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        // 2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        // 3.替换
        // 主页
        ft.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);
        // 左侧菜单
        ft.replace(R.id.fl_left_menu, new LeftMenuFragment(), LEFT_MENU_TAG);
        // 4.提交
        ft.commit();
        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG)
                .replace(R.id.fl_left_menu, new LeftMenuFragment(), LEFT_MENU_TAG)
                .commit();*/
    }
}
