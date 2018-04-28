package com.sky.app.news.viewholder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sky.app.news.R;
import com.sky.app.news.base.BaseViewHolder;
import com.sky.app.news.domain.TabDetailPagerBean;
import com.sky.app.news.utils.Constants;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import butterknife.BindView;

/**
 * Created with Android Studio.
 * 描述: 新闻页签ViewHolder
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

    private Context context;
    private ImageOptions imageOptions;

    public TabDetailPagerListViewHolder(Context context) {
        super(context);
        this.context = context;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(90), DensityUtil.dip2px(90))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                // 很多时候设置了合适的scaleType也不需要它.
                .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    protected void refreshData() {
        TabDetailPagerBean.DataBean.NewsBean data = getData();
        String imageUrl = Constants.BASE_URL + data.getListimage();
        // 请求图片使用xUtils3
//        x.image().bind(ivIcon, imageUrl, imageOptions);
        // 请求图片使用Glide
        RequestOptions options = new RequestOptions()
                // 正在加载中的图片
                .placeholder(R.drawable.news_pic_default)
                // 加载失败的图片
                .error(R.drawable.news_pic_default)
                // 磁盘缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                // 图片地址
                .load(imageUrl)
                // 参数
                .apply(options)
                // 需要显示的ImageView控件
                .into(ivIcon);
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
