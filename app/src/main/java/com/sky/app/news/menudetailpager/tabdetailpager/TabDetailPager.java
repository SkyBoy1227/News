package com.sky.app.news.menudetailpager.tabdetailpager;

import android.content.Context;
import android.view.View;

import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;

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

    private NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;
    private String url;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tab_detail_pager, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenData.getUrl();
        LogUtil.e(childrenData.getTitle() + "的联网地址：" + url);
    }
}
