package com.sky.app.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sky.app.news.R;
import com.sky.app.news.adapter.TabDetailPagerListAdapter;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.DensityUtil;
import com.sky.app.news.utils.LogUtil;
import com.sky.app.news.view.HorizontalScrollViewPager;
import com.sky.app.news.view.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

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

    private HorizontalScrollViewPager viewPager;
    private TextView tvTitle;
    private LinearLayout llPointGroup;
    private RefreshListView listView;

    private TabDetailPagerListAdapter adapter;
    private NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;

    /**
     * 联网请求地址
     */
    private String url;

    /**
     * 顶部轮播图部分的集合
     */
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    /**
     * 新闻列表的数据集合
     */
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    /**
     * 上一次红点的位置
     */
    private int prePosition;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tab_detail_pager, null);
        listView = view.findViewById(R.id.listView);

        View topNewsView = View.inflate(context, R.layout.top_news, null);
        viewPager = topNewsView.findViewById(R.id.view_pager);
        tvTitle = topNewsView.findViewById(R.id.tv_title);
        llPointGroup = topNewsView.findViewById(R.id.ll_point_group);

        // 把顶部轮播图部分视图，以头的方式添加到ListView中
        listView.addHeaderView(topNewsView);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenData.getUrl();
        LogUtil.e(childrenData.getTitle() + "的联网地址：" + url);
        // 得到缓存的文本数据
        String cacheJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(cacheJson)) {
            processData(cacheJson);
        }
        getDataFromNet();
    }

    /**
     * 解析json数据并且显示数据
     *
     * @param json
     */
    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);
        LogUtil.e(childrenData.getTitle() + "解析成功==" + bean.getData().getNews().get(0).getTitle());
        topnews = bean.getData().getTopnews();
        // 设置ViewPager的适配器
        viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());

        addPoint();

        // 监听页面的改变，设置红点变化和文本变化
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(prePosition);
        tvTitle.setText(topnews.get(prePosition).getTitle());

        // 准备ListView的集合数据
        news = bean.getData().getNews();

        // 设置ListView的适配器
        adapter = new TabDetailPagerListAdapter(news);
        listView.setAdapter(adapter);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 1.设置文本
            tvTitle.setText(topnews.get(position).getTitle());
            // 2.对应页面的点高亮-红色
            for (int i = 0; i < topnews.size(); i++) {
                View pointView = llPointGroup.getChildAt(i);
                if (i == position) {
                    pointView.setEnabled(true);
                } else {
                    pointView.setEnabled(false);
                }
            }
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 添加红点
     */
    private void addPoint() {
        // 移除所有的红点
        llPointGroup.removeAllViews();
        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            // 设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                params.leftMargin = DensityUtil.dip2px(context, 8);
                imageView.setEnabled(false);
            }
            imageView.setLayoutParams(params);
            llPointGroup.addView(imageView);
        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            // 设置图片默认背景
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            // X轴和Y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // 把图片添加到容器(ViewPager)中
            container.addView(imageView);

            // 图片请求地址
            String imageUrl = Constants.BASE_URL + topnews.get(position).getTopimage();
            // 联网请求图片
            // 请求图片使用Glide
            RequestOptions options = new RequestOptions()
                    // 正在加载中的图片
                    .placeholder(R.drawable.home_scroll_default)
                    // 加载失败的图片
                    .error(R.drawable.home_scroll_default)
                    // 磁盘缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    // 图片地址
                    .load(imageUrl)
                    // 参数
                    .apply(options)
                    // 需要显示的ImageView控件
                    .into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    /**
     * 解析json数据
     *
     * @param json
     * @return
     */
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }

    /**
     * 联网请求数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功==" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenData.getTitle() + "-onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-onFinished");
            }
        });
    }
}
