package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.sky.app.news.R;
import com.sky.app.news.activity.ShowImageActivity;
import com.sky.app.news.adapter.PhotosMenuDetailPagerAdapter;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.domain.PhotosMenuDetailPagerBean;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;
import com.sky.app.news.volley.VolleyManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created with Android Studio.
 * 描述: 组图详情页面
 * Date: 2018/4/25
 * Time: 12:23
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.gridView)
    GridView gridView;

    private NewsCenterPagerBean2.DetailPagerData detailPagerData;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPagerAdapter adapter;

    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowListView = true;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menu_detail_pager, null);
        ButterKnife.bind(this, view);
        // 设置点击某条的item的监听
        listView.setOnItemClickListener(new MyOnItemClickListener());
        gridView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PhotosMenuDetailPagerBean.DataBean.NewsBean data = news.get(position);
            String imageUrl = Constants.BASE_URL + data.getSmallimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("url", imageUrl);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("组图详情页面数据被初始化了..");
        url = Constants.BASE_URL + detailPagerData.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNetByOkHttpUtils();
//        getDataFromNet();
    }

    /**
     * 使用OkHttpUtils联网请求数据
     */
    private void getDataFromNetByOkHttpUtils() {
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
        }

        @Override
        public void onAfter(int id) {
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            LogUtil.e("使用OkHttp联网请求失败==" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse：complete");
            LogUtil.e("使用OkHttp联网请求成功==" + response);
            // 缓存数据
            CacheUtils.putString(context, url, response);

            processData(response);
            switch (id) {
                case 100:
                    Toast.makeText(context, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(context, "https", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            LogUtil.e("inProgress:" + progress);
        }
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
        adapter = new PhotosMenuDetailPagerAdapter(news);
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
            adapter = new PhotosMenuDetailPagerAdapter(news);
            gridView.setAdapter(adapter);
            // 按钮显示--ListView
            imageButton.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            // 显示ListView，隐藏GridView
            isShowListView = true;
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter(news);
            listView.setAdapter(adapter);
            // 按钮显示--GridView
            imageButton.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
