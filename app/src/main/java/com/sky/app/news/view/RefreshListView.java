package com.sky.app.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.news.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 自定义下拉刷新的ListView
 * Date: 2018/4/29
 * Time: 9:48
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class RefreshListView extends ListView {
    /**
     * 下拉刷新
     */
    private static final int PULL_DOWN_REFRESH = 1;

    /**
     * 释放刷新
     */
    private static final int RELEASE_REFRESH = 2;

    /**
     * 正在刷新
     */
    private static final int REFRESHING = 3;

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.pb_status)
    ProgressBar pbStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_time)
    TextView tvTime;
    /**
     * 下拉刷新和和顶部轮播图（先不加入）
     */
    @BindView(R.id.ll_pull_down_refresh)
    LinearLayout llPullDownRefresh;

    /**
     * 下拉刷新控件的高
     */
    private int pullDownRefreshHeight;
    private float startY = -1;

    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;

    private RotateAnimation downAnimation;
    private RotateAnimation upAnimation;
    private OnRefreshListener mOnRefreshListener;

    /**
     * 加载更多控件
     */
    private View footerView;

    /**
     * 加载更多控件的高
     */
    private int footerViewHeight;

    /**
     * 是否已经加载更多
     */
    private boolean isLoadMore;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    /**
     * 初始化ListView的底部
     */
    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.refresh_footer, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        // ListView添加footer
        addFooterView(footerView);

        // 监听ListView的滚动
        setOnScrollListener(new MyOnScrollListener());
    }

    class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当静止或者惯性滚动的时候
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                // 并且是最后一条可见
                if (getLastVisiblePosition() >= getCount() - 1) {
                    // 1.显示加载更多布局
                    footerView.setPadding(8, 8, 8, 8);
                    // 2.状态改变
                    isLoadMore = true;
                    // 3.回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    /**
     * 初始化ListView的头部
     *
     * @param context
     */
    private void initHeaderView(Context context) {
        View headerView = View.inflate(context, R.layout.refresh_header, null);
        ButterKnife.bind(this, headerView);
        // 测量
        llPullDownRefresh.measure(0, 0);
        pullDownRefreshHeight = llPullDownRefresh.getMeasuredHeight();
        // 默认隐藏下拉刷新控件
        llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);

        // 添加ListView的头
        addHeaderView(headerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 1.记录起始坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }
                if (currentStatus == REFRESHING) {
                    break;
                }
                // 2.来到新的坐标
                float endY = ev.getY();
                // 3.记录滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    // 下拉
                    int paddingTop = (int) (-pullDownRefreshHeight + distanceY);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        // 下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        refreshViewState();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        // 释放刷新状态
                        currentStatus = RELEASE_REFRESH;
                        refreshViewState();
                    }

                    // 动态的显示下拉刷新控件
                    llPullDownRefresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    // 完全隐藏
                    llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    // 设置状态为正在刷新
                    currentStatus = REFRESHING;
                    refreshViewState();
                    // 完全显示
                    llPullDownRefresh.setPadding(0, 0, 0, 0);

                    // 回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉刷新控件的状态
     */
    private void refreshViewState() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:
                // 下拉刷新状态
                ivArrow.startAnimation(downAnimation);
                tvStatus.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH:
                // 释放刷新状态
                ivArrow.startAnimation(upAnimation);
                tvStatus.setText("释放刷新...");
                break;
            case REFRESHING:
                // 正在刷新状态
                tvStatus.setText("正在刷新...");
                ivArrow.clearAnimation();
                pbStatus.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 当联网成功和失败的时候回调该方法
     * 用户刷新状态的还原
     *
     * @param success
     */
    public void onRefreshFinish(boolean success) {
        tvStatus.setText("下拉刷新...");
        currentStatus = PULL_DOWN_REFRESH;
        ivArrow.clearAnimation();
        pbStatus.setVisibility(View.GONE);
        ivArrow.setVisibility(View.VISIBLE);
        // 隐藏下拉刷新控件
        llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
        if (success) {
            // 设置最新更新时间
            tvTime.setText("上次更新时间：" + getSystemTime());
        }
    }

    /**
     * 得到当前Android系统的时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener {
        /**
         * 当下拉刷新的时候回调这个方法
         */
        void onPullDownRefresh();

        /**
         * 当加载更多的时候回调这个方法
         */
        public void onLoadMore();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }
}
