package com.sky.app.news.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sky.app.news.base.BaseFragment;
import com.sky.app.news.utils.LogUtil;

/**
 * Created with Android Studio.
 * 描述: 正文Fragment
 * Date: 2018/4/20
 * Time: 14:27
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class ContentFragment extends BaseFragment {
    private TextView textView;

    @Override
    protected View initView() {
        LogUtil.e("正文Fragment视图被初始化了");
        textView = new TextView(context);
        textView.setTextSize(26);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");
        textView.setText("正文Fragment页面");
    }
}
