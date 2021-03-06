package com.sky.app.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.sky.app.news.base.BasePager;
import com.sky.app.news.utils.LogUtil;

/**
 * Created with Android Studio.
 * 描述: 政要指南
 * Date: 2018/4/23
 * Time: 14:43
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class GovaffairPager extends BasePager {
    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要指南数据被初始化了..");
        // 1.设置标题
        tvTitle.setText("政要指南");
        // 2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        // 3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        // 4.绑定数据
        textView.setText("政要指南内容");
    }
}
