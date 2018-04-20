package com.sky.app.news.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.sky.app.news.R;
import com.sky.app.news.base.BaseFragment;
import com.sky.app.news.utils.LogUtil;

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
        rgBottom.check(R.id.rb_home);
    }
}
