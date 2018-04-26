package com.sky.app.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;

/**
 * Created with Android Studio.
 * 描述: 页签详情页面
 * Date: 2018/4/26
 * Time: 15:05
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class TabDetailPager extends MenuDetailBasePager {

    private NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;
    private TextView textView;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
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
        textView.setText(childrenData.getTitle());
    }
}
