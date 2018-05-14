package com.sky.app.news.adapter;

import android.content.Context;

import com.sky.app.news.base.AbstractAdapter;
import com.sky.app.news.base.BaseViewHolder;
import com.sky.app.news.domain.PhotosMenuDetailPagerBean;
import com.sky.app.news.viewholder.PhotosMenuDetailPagerViewHolder;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/5/14
 * Time: 15:07
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class PhotosMenuDetailPagerAdapter extends AbstractAdapter<PhotosMenuDetailPagerBean.DataBean.NewsBean> {
    public PhotosMenuDetailPagerAdapter(List<PhotosMenuDetailPagerBean.DataBean.NewsBean> list) {
        super(list);
    }

    @Override
    protected BaseViewHolder<PhotosMenuDetailPagerBean.DataBean.NewsBean> getViewHolder(Context context) {
        return new PhotosMenuDetailPagerViewHolder(context);
    }
}
