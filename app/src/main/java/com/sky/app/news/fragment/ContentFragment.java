package com.sky.app.news.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.sky.app.news.R;
import com.sky.app.news.base.BaseFragment;
import com.sky.app.news.base.BasePager;
import com.sky.app.news.pager.GovaffairPager;
import com.sky.app.news.pager.HomePager;
import com.sky.app.news.pager.NewsCenterPager;
import com.sky.app.news.pager.SettingPager;
import com.sky.app.news.pager.SmartServicePager;
import com.sky.app.news.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rg_bottom)
    RadioGroup rgBottom;

    /**
     * 装五个页面的集合
     */
    private List<BasePager> basePagers;

    @Override
    protected View initView() {
        LogUtil.e("正文Fragment视图被初始化了");
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");
        // 初始化五个页面，并且放入集合中
        basePagers = new ArrayList<>();
        // 主页
        basePagers.add(new HomePager(context));
        // 新闻中心
        basePagers.add(new NewsCenterPager(context));
        // 智慧服务
        basePagers.add(new SmartServicePager(context));
        // 政要指南
        basePagers.add(new GovaffairPager(context));
        // 设置中心
        basePagers.add(new SettingPager(context));

        // 设置默认选中首页
        rgBottom.check(R.id.rb_home);

        // 设置ViewPager的适配器
        viewPager.setAdapter(new ContentFragmentAdapter());
    }

    class ContentFragmentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // 各个页面的实例
            BasePager basePager = basePagers.get(position);
            // 调用各个页面的initData()，初始化数据
            basePager.initData();
            // 各个子页面
            View rootView = basePager.rootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
