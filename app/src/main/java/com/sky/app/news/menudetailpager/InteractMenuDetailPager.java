package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.utils.LogUtil;

/**
 * Created with Android Studio.
 * 描述: 互动详情页面
 * Date: 2018/4/25
 * Time: 12:23
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class InteractMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public InteractMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面数据被初始化了..");
        textView.setText("互动详情页面内容");
    }
}