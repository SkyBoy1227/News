package com.sky.app.news.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sky.app.news.base.BasePager;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: ContentFragmentAdapter
 * Date: 2018/4/24
 * Time: 15:28
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class ContentFragmentAdapter extends PagerAdapter {
    private List<BasePager> basePagers;

    public ContentFragmentAdapter(List<BasePager> basePagers) {
        this.basePagers = basePagers;
    }

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
//            basePager.initData();
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