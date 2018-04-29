package com.sky.app.news.view;

import android.content.Context;
import android.util.AttributeSet;
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
    @BindView(R.id.ll_pull_down_refresh)
    LinearLayout llPullDownRefresh;

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
        // 添加ListView的头
        addHeaderView(headerView);
    }
}
