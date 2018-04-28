package com.sky.app.news.base;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 通用的ViewHolder
 * Date: 2018/4/28
 * Time: 10:36
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public abstract class BaseViewHolder<T> {
    private T data;
    private View rootView;

    public BaseViewHolder(Context context) {
        rootView = View.inflate(context, getLayoutId(), null);
        rootView.setTag(this);
        ButterKnife.bind(this, rootView);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        refreshData();
    }

    /**
     * 加载数据
     */
    protected abstract void refreshData();

    public View getRootView() {
        return rootView;
    }

    /**
     * 布局文件id
     *
     * @return
     */
    protected abstract int getLayoutId();
}
