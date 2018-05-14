package com.sky.app.news.viewholder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sky.app.news.R;
import com.sky.app.news.base.BaseViewHolder;
import com.sky.app.news.domain.PhotosMenuDetailPagerBean;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.volley.VolleyManager;

import butterknife.BindView;

/**
 * Created with Android Studio.
 * 描述: ${DESCRIPTION}
 * Date: 2018/5/14
 * Time: 15:08
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class PhotosMenuDetailPagerViewHolder extends BaseViewHolder<PhotosMenuDetailPagerBean.DataBean.NewsBean> {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public PhotosMenuDetailPagerViewHolder(Context context) {
        super(context);
    }

    @Override
    protected void refreshData() {
        PhotosMenuDetailPagerBean.DataBean.NewsBean data = getData();
        tvTitle.setText(data.getTitle());
        String imageUrl = Constants.BASE_URL + data.getSmallimage();
        loaderImager(this, imageUrl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_photos_menu_detail_pager;
    }

    /**
     * 使用Volley请求图片
     *
     * @param viewHolder
     * @param imageUrl
     */
    private void loaderImager(PhotosMenuDetailPagerViewHolder viewHolder, String imageUrl) {
        // 设置tag
        viewHolder.ivIcon.setTag(imageUrl);
        // 直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.ivIcon != null) {
                        if (imageContainer.getBitmap() != null) {
                            // 设置图片
                            viewHolder.ivIcon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            // 设置默认图片
                            viewHolder.ivIcon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.ivIcon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageUrl, listener);
    }
}
