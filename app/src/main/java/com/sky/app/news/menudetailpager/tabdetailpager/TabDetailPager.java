package com.sky.app.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sky.app.news.R;
import com.sky.app.news.activity.NewsDetailActivity;
import com.sky.app.news.adapter.TabDetailPagerListAdapter;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.DensityUtil;
import com.sky.app.news.utils.LogUtil;
import com.sky.app.news.view.HorizontalScrollViewPager;
import com.sky.lib.refreshlistview.RefreshListView;

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

    public static final String READ_ID_ARRAY = "read_id_array";
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

    /**
     * 是否加载更多
     */
    private boolean isLoadMore;

    /**
     * 下一页的联网地址
     */
    private String moreUrl;
    private InternalHandler handler;

    /**
     * 是否拖拽顶部轮播图
     */
    private boolean isDragging;

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
//        listView.addHeaderView(topNewsView);
        listView.addTopNewsView(topNewsView);

        // 设置监听下拉刷新
        listView.setOnRefreshListener(new MyOnRefreshListener());

        // 设置ListView的item的点击监听
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            int realPosition = position - 1;
            TabDetailPagerBean.DataBean.NewsBean bean = news.get(realPosition);
//            Toast.makeText(context, "newsBean==id==" + bean.getId() + ",newsBean_title==" + bean.getTitle(), Toast.LENGTH_SHORT).show();
            LogUtil.e("newsBean==id==" + bean.getId() + ",newsBean_title==" + bean.getTitle() + ",url====" + bean.getUrl());
            // 1.取出保存的id的集合
            String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
            // 2.判断是否存在，如果不存在，才保存，并且刷新适配器
            if (!idArray.contains(String.valueOf(bean.getId()))) {
                CacheUtils.putString(context, READ_ID_ARRAY, idArray + bean.getId() + ",");
                // 刷新适配器
                adapter.notifyDataSetChanged();
            }

            // 跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constants.BASE_URL + bean.getUrl());
            context.startActivity(intent);
        });
        return view;
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
//            Toast.makeText(context, "加载更多被回调了", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(moreUrl)) {
                // 没有更多数据
                Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                listView.onRefreshFinish(false);
            } else {
                getMoreDataFromNet();
            }
        }
    }

    /**
     * 联网请求更多数据
     */
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多数据成功==" + result);
                listView.onRefreshFinish(false);
                isLoadMore = true;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多数据失败onError==" + ex.getMessage());
                listView.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多数据失败onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多数据失败onFinished==");
            }
        });
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
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }
        if (!isLoadMore) {
            // 默认
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
        } else {
            // 加载更多
            isLoadMore = false;
            // 添加到原来的集合中
            news.addAll(bean.getData().getNews());
            // 刷新适配器
            adapter.notifyDataSetChanged();
        }

        // 发消息每隔4000切换一次ViewPager页面
        if (handler == null) {
            handler = new InternalHandler();
        }
        // 把消息队列所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(0, 4000);
    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 切换ViewPager的下一个页面
            int item = (viewPager.getCurrentItem() + 1) % topnews.size();
            viewPager.setCurrentItem(item);
            handler.sendEmptyMessageDelayed(0, 4000);
        }
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
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                // 拖拽
                LogUtil.e("拖拽");
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                // 惯性
                LogUtil.e("惯性");
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 4000);
            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                // 静止状态
                LogUtil.e("静止状态");
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 4000);
            }
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

            imageView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 按下
                        LogUtil.e("按下");
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 离开
                        LogUtil.e("离开");
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(0, 4000);
                        break;
                    default:
                        break;
                }
                return true;
            });
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
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功==" + result);
                processData(result);

                // 隐藏下拉刷新控件-重新显示数据，更新时间
                listView.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败==" + ex.getMessage());
                // 隐藏下拉刷新控件 - 不更新时间，只是隐藏
                listView.onRefreshFinish(false);
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
