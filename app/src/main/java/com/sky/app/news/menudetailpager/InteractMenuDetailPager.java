package com.sky.app.news.menudetailpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.domain.PhotosMenuDetailPagerBean;
import com.sky.app.news.utils.BitmapCacheUtils;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;
import com.sky.app.news.utils.NetCacheUtils;
import com.sky.app.news.volley.VolleyManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 互动详情页面
 * Date: 2018/4/25
 * Time: 12:23
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class InteractMenuDetailPager extends MenuDetailBasePager {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.gridView)
    GridView gridView;

    private NewsCenterPagerBean2.DetailPagerData detailPagerData;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private InteractMenuDetailPagerAdapter adapter;

    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowListView = true;

    /**
     * 图片三级缓存工具类：
     */
    private BitmapCacheUtils bitmapCacheUtils;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
                    // 图片请求成功
                    LogUtil.e("请求图片成功==" + position);
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (listView.isShown()) {
                        ImageView imageView = listView.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    if (gridView.isShown()) {
                        ImageView imageView = gridView.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAILURE:
                    // 图片请求失败
                    LogUtil.e("请求图片失败==" + position);
                    break;
                default:
                    break;
            }
        }
    };

    public InteractMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menu_detail_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面数据被初始化了..");
        url = Constants.BASE_URL + detailPagerData.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    /**
     * 使用Volley联网请求数据
     */
    private void getDataFromNet() {
        // String请求
        StringRequest request = new StringRequest(StringRequest.Method.GET, url, response -> {
            LogUtil.e("使用Volley联网请求成功==" + response);
            // 缓存数据
            CacheUtils.putString(context, url, response);

            processData(response);
        }, error -> LogUtil.e("使用Volley联网请求失败==" + error.getMessage())) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        // 添加到队列
        VolleyManager.getRequestQueue().add(request);
    }

    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = parsedJson(json);
        LogUtil.e("组图解析成功====" + bean.getData().getNews().get(0).getTitle());
        // 设置适配器
        news = bean.getData().getNews();
        adapter = new InteractMenuDetailPagerAdapter();
        listView.setAdapter(adapter);
    }

    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }

    /**
     * 切换ListView或GridView
     *
     * @param imageButton
     */
    public void switchListAndGrid(ImageButton imageButton) {
        if (isShowListView) {
            // 显示GridView，隐藏ListView
            isShowListView = false;
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            adapter = new InteractMenuDetailPagerAdapter();
            gridView.setAdapter(adapter);
            // 按钮显示--ListView
            imageButton.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            // 显示ListView，隐藏GridView
            isShowListView = true;
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new InteractMenuDetailPagerAdapter();
            listView.setAdapter(adapter);
            // 按钮显示--GridView
            imageButton.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    class InteractMenuDetailPagerAdapter extends BaseAdapter {

        private DisplayImageOptions options;

        public InteractMenuDetailPagerAdapter() {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.home_scroll_default)
                    .showImageForEmptyUri(R.drawable.home_scroll_default)
                    .showImageOnFail(R.drawable.home_scroll_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // 设置矩形圆角
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_photos_menu_detail_pager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotosMenuDetailPagerBean.DataBean.NewsBean bean = news.get(position);
            holder.tvTitle.setText(bean.getTitle());
            String imageUrl = Constants.BASE_URL + bean.getSmallimage();
            // 1.使用Volley请求图片-设置图片了
//            loaderImager(holder, imageUrl );
            // 2.使用自定义的三级缓存请求图片
//            holder.ivIcon.setTag(position);
            // 内存或者本地
//            Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl, position);
//            if (bitmap != null) {
//                holder.ivIcon.setImageBitmap(bitmap);
//            }

            // 3.使用Picasso请求网络图片
//            Picasso.get()
//                    .load(imageUrl)
//                    .placeholder(R.drawable.home_scroll_default)
//                    .error(R.drawable.home_scroll_default)
//                    .into(holder.ivIcon);

            // 4.使用Glide请求网络图片
//            RequestOptions options = new RequestOptions()
//                    // 正在加载中的图片
//                    .placeholder(R.drawable.home_scroll_default)
//                    // 加载失败的图片
//                    .error(R.drawable.home_scroll_default)
//                    // 磁盘缓存策略
//                    .diskCacheStrategy(DiskCacheStrategy.ALL);
//            Glide.with(context)
//                    .load(imageUrl)
//                    .apply(options)
//                    .into(holder.ivIcon);

            // 5.使用ImageLoader请求网络图片
            ImageLoader.getInstance().displayImage(imageUrl, holder.ivIcon, options);
            return convertView;
        }

    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
