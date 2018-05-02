package com.sky.app.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.news.R;

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
     * 初始化头部
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
}
