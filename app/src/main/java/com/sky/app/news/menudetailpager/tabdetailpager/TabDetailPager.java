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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.DensityUtil;
import com.sky.app.news.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.listView)
    ListView listView;

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
        ButterKnife.bind(this, view);
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
        tvTitle.setText(topnews.get(prePosition).getTitle());
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
            // 把之前的变成灰色
            llPointGroup.getChildAt(prePosition).setEnabled(false);
            // 把当前设置红色
            llPointGroup.getChildAt(position).setEnabled(true);
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
            x.image().bind(imageView, imageUrl);
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
