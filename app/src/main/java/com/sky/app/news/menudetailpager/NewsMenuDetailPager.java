package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 新闻详情页面
 * Date: 2018/4/25
 * Time: 11:57
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NewsMenuDetailPager extends MenuDetailBasePager {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_menu_detail_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了..");
    }
}
