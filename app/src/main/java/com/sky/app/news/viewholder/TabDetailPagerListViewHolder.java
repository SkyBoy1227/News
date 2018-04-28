package com.sky.app.news.viewholder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.app.news.R;
import com.sky.app.news.base.BaseViewHolder;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.utils.Constants;

import org.xutils.x;

import butterknife.BindView;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/4/28
 * Time: 10:58
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class TabDetailPagerListViewHolder extends BaseViewHolder<TabDetailPagerBean.DataBean.NewsBean> {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;

    public TabDetailPagerListViewHolder(Context context) {
        super(context);
    }

    @Override
    protected void refreshData() {
        TabDetailPagerBean.DataBean.NewsBean data = getData();
        String imageUrl = Constants.BASE_URL + data.getListimage();
        // 请求图片
        x.image().bind(ivIcon, imageUrl);
        // 设置标题
        tvTitle.setText(data.getTitle());
        // 设置更新时间
        tvTime.setText(data.getPubdate());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_tab_detail_pager;
    }
}
