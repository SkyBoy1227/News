package com.sky.app.news.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sky.app.news.base.BaseFragment;
import com.sky.app.news.domain.NewsCenterPagerBean;
import com.sky.app.news.utils.LogUtil;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 左侧菜单Fragment
 * Date: 2018/4/20
 * Time: 14:24
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class LeftMenuFragment extends BaseFragment {
    private TextView textView;
    private List<NewsCenterPagerBean.DataBean> data;

    @Override
    protected View initView() {
        LogUtil.e("左侧菜单Fragment视图被初始化了");
        textView = new TextView(context);
        textView.setTextSize(26);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单Fragment数据被初始化了");
        textView.setText("左侧菜单页面");
    }

    /**
     * 接收数据
     *
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (NewsCenterPagerBean.DataBean bean : data) {
            LogUtil.e("title = " + bean.getTitle());
        }
    }
}
