package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.menudetailpager.tabdetailpager.TabDetailPager;
import com.sky.app.news.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 页签页面的数据的集合-数据
     */
    private List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDataList;

    /**
     * 页签页面的集合-页面
     */
    private List<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        childrenDataList = detailPagerData.getChildren();
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
        // 准备新闻详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for (NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData : childrenDataList) {
            tabDetailPagers.add(new TabDetailPager(context, childrenData));
        }
        // 设置ViewPager的适配器
        viewPager.setAdapter(new NewsMenuDetailPagerAdapter());
    }

    class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            // 初始化数据
            tabDetailPager.initData();
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
