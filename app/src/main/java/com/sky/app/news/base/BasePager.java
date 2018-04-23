package com.sky.app.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sky.app.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 基类或者说公共类
 * HomePager,NewsCenterPager,
 * SmartServicePager,GovaffairPager
 * SettingPager都继承BasePager
 * Date: 2018/4/23
 * Time: 11:50
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class BasePager {
    /**
     * 上下文 MainActivity
     */
    public Context context;

    /**
     * 视图，代表各个不同的页面
     */
    public View rootView;

    /**
     * 显示标题
     */
    @BindView(R.id.tv_title)
    public TextView tvTitle;

    /**
     * 侧滑菜单按钮
     */
    @BindView(R.id.iv_menu)
    public ImageButton ivMenu;

    /**
     * 加载各个子页面
     */
    @BindView(R.id.fl_content)
    public FrameLayout flContent;

    public BasePager(Context context) {
        this.context = context;
        // 构造方法一执行，视图就被初始化了
        rootView = initView();
    }

    /**
     * 用于初始化公共部分视图，并且初始化加载子视图的FrameLayout
     *
     * @return
     */
    public View initView() {
        View view = View.inflate(context, R.layout.base_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 初始化数据;当孩子需要初始化数据;或者绑定数据;联网请求数据并且绑定的时候，重写该方法
     */
    public void initData() {

    }
}
