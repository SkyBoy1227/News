package com.sky.app.news.adapter;

import android.content.Context;

import com.sky.app.news.base.AbstractAdapter;
import com.sky.app.news.base.BaseViewHolder;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.viewholder.TabDetailPagerListViewHolder;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/4/28
 * Time: 11:00
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class TabDetailPagerListAdapter extends AbstractAdapter<TabDetailPagerBean.DataBean.NewsBean> {
    public TabDetailPagerListAdapter(List<TabDetailPagerBean.DataBean.NewsBean> list) {
        super(list);
    }

    @Override
    protected BaseViewHolder<TabDetailPagerBean.DataBean.NewsBean> getViewHolder(Context context) {
        return new TabDetailPagerListViewHolder(context);
    }
}
