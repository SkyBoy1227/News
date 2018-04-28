package com.sky.app.news.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/4/28
 * Time: 10:35
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {
    private List<T> list;

    public AbstractAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder<T> viewHolder;
        if (convertView == null) {
            viewHolder = getViewHolder(parent.getContext());
        } else {
            viewHolder = (BaseViewHolder<T>) convertView.getTag();
        }
        T data = list.get(position);
        viewHolder.setData(data);
        return viewHolder.getRootView();
    }

    /**
     * 得到ViewHolder
     *
     * @param context
     * @return
     */
    protected abstract BaseViewHolder<T> getViewHolder(Context context);
}
