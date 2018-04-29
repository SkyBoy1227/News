package com.sky.app.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
    private int llPullDownRefreshHeight;
    private float startY = -1;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
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
        llPullDownRefreshHeight = llPullDownRefresh.getMeasuredHeight();
        // 完全隐藏
        llPullDownRefresh.setPadding(0, -llPullDownRefreshHeight, 0, 0);

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
                // 2.来到新的坐标
                float endY = ev.getY();
                // 3.记录滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    // 下拉
                    int paddingTop = (int) (-llPullDownRefreshHeight + distanceY);
                    llPullDownRefresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
