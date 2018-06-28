package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sky.app.news.R;
import com.sky.app.news.activity.MainActivity;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.menudetailpager.tabdetailpager.TopicDetailPager;
import com.sky.app.news.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created with Android Studio.
 * 描述: 专题详情页面
 * Date: 2018/4/25
 * Time: 12:23
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ib_tab_next)
    ImageButton ibTabNext;

    /**
     * 页签页面的数据的集合-数据
     */
    private List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDataList;

    /**
     * 页签页面的集合-页面
     */
    private List<TopicDetailPager> tabDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        childrenDataList = detailPagerData.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topic_menu_detail_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 设置点击事件
     */
    @OnClick(R.id.ib_tab_next)
    public void next() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题详情页面数据被初始化了..");
        // 准备专题详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for (NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData : childrenDataList) {
            tabDetailPagers.add(new TopicDetailPager(context, childrenData));
        }
        // 设置ViewPager的适配器
        viewPager.setAdapter(new TopicMenuDetailPager.NewsMenuDetailPagerAdapter());

        // ViewPager和TabPageIndicator关联
//        TabPageIndicator.setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // 注意以后监听页面的变化，由TabPageIndicator监听页面的变化
//        TabPageIndicator.setOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());
        viewPager.addOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());

        // 设置滑动或者固定
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);//导致没法显示
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(getTabView(i));
//        }
    }

    private View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv = view.findViewById(R.id.textView);
        tv.setText(childrenDataList.get(position).getTitle());
        return view;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                // SlidingMenu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                // SlidingMenu不可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 根据传入的参数设置是否让SlidingMenu可以滑动
     *
     * @param touchMode 滑动模式
     */
    private void isEnableSlidingMenu(int touchMode) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchMode);
    }

    class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return childrenDataList.get(position).getTitle();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TopicDetailPager tabDetailPager = tabDetailPagers.get(position);
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
