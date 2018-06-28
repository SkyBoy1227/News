package com.sky.app.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.sky.app.news.activity.MainActivity;
import com.sky.app.news.base.BasePager;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.domain.NewsCenterPagerBean;
import com.sky.app.news.domain.NewsCenterPagerBean2;
import com.sky.app.news.fragment.LeftMenuFragment;
import com.sky.app.news.menudetailpager.InteractMenuDetailPager;
import com.sky.app.news.menudetailpager.NewsMenuDetailPager;
import com.sky.app.news.menudetailpager.PhotosMenuDetailPager;
import com.sky.app.news.menudetailpager.TopicMenuDetailPager;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;
import com.sky.app.news.volley.VolleyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 新闻中心
 * Date: 2018/4/23
 * Time: 14:43
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NewsCenterPager extends BasePager {
    /**
     * 左侧菜单对应的数据集合
     */
    private List<NewsCenterPagerBean2.DetailPagerData> data;

    /**
     * 详情页面的集合
     */
    private List<MenuDetailBasePager> detailBasePagers;
    private long startTime;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        ibMenu.setVisibility(View.VISIBLE);
        // 1.设置标题
        tvTitle.setText("新闻中心");
        // 2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setTextSize(24);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        // 3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        // 4.绑定数据
        textView.setText("新闻中心内容");

        // 得到缓存数据
        String json = CacheUtils.getString(context, Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(json)) {
            processData(json);
        }

        startTime = SystemClock.uptimeMillis();
        getDataFromNet();
//        getDataFromNetByVolley();
    }

    /**
     * 使用Volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        // 请求队列
//        RequestQueue queue = Volley.newRequestQueue(context);
        // String请求
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                Constants.NEWSCENTER_PAGER_URL, response -> {
            long endTime = SystemClock.uptimeMillis();
            long passTime = endTime - startTime;
            LogUtil.e("Volley---passTime======" + passTime);
            LogUtil.e("使用Volley联网请求成功==" + response);
            // 缓存数据
            CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, response);

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
     * 使用xUtils3联网请求数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                long endTime = SystemClock.uptimeMillis();
                long passTime = endTime - startTime;
                LogUtil.e("xUtils3---passTime======" + passTime);
                LogUtil.e("使用xUtils3联网请求成功==" + result);
                // 缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    /**
     * 解析json数据并且显示数据
     *
     * @param json
     */
    private void processData(String json) {
//        NewsCenterPagerBean bean = parseJson(json);
        NewsCenterPagerBean2 bean2 = parseJson2(json);
//        String title = bean.getData().get(0).getChildren().get(0).getTitle();
//        LogUtil.e("使用Gson解析json数据成功，title = " + title);
        String title2 = bean2.getData().get(0).getChildren().get(0).getTitle();
        LogUtil.e("使用Gson解析json数据成功NewsCenterPagerBean2------------------------title2 = " + title2);
        // 将得到的数据传递给左侧菜单
        data = bean2.getData();
        // 添加详情页面
        detailBasePagers = new ArrayList<>();
        // 新闻详情页面
        detailBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));
        // 专题详情页面
        detailBasePagers.add(new TopicMenuDetailPager(context, data.get(0)));
        // 组图详情页面
        detailBasePagers.add(new PhotosMenuDetailPager(context, data.get(2)));
        // 互动详情页面
        detailBasePagers.add(new InteractMenuDetailPager(context));

        MainActivity mainActivity = (MainActivity) context;
        // 得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(data);
    }

    /**
     * 使用Android系统自带的API解析json数据
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean2 parseJson2(String json) {
        NewsCenterPagerBean2 bean = new NewsCenterPagerBean2();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            // retcode字段解析成功
            bean.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");
            if (data != null && data.length() > 0) {
                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                // 设置列表数据
                bean.setData(detailPagerDatas);

                // for循环，解析每条数据
                for (int i = 0; i < data.length(); i++) {
                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
                    // 添加到集合中
                    detailPagerDatas.add(detailPagerData);

                    JSONObject jsonObjectData = data.optJSONObject(i);
                    int id = jsonObjectData.optInt("id");
                    detailPagerData.setId(id);
                    String title = jsonObjectData.optString("title");
                    detailPagerData.setTitle(title);
                    int type = jsonObjectData.optInt("type");
                    detailPagerData.setType(type);
                    String url = jsonObjectData.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObjectData.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObjectData.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObjectData.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObjectData.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObjectData.optJSONArray("children");
                    if (children != null && children.length() > 0) {
                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();
                        // 设置集合-ChildrenData
                        detailPagerData.setChildren(childrenDatas);

                        for (int j = 0; j < children.length(); j++) {
                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();
                            // 添加到集合中
                            childrenDatas.add(childrenData);

                            JSONObject jsonObjectChildren = children.optJSONObject(j);
                            int childrenId = jsonObjectChildren.optInt("id");
                            childrenData.setId(childrenId);
                            String childrenTitle = jsonObjectChildren.optString("title");
                            childrenData.setTitle(childrenTitle);
                            int childrenType = jsonObjectChildren.optInt("type");
                            childrenData.setType(childrenType);
                            String childrenUrl = jsonObjectChildren.optString("url");
                            childrenData.setUrl(childrenUrl);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 解析json数据：1,使用系统的API解析json；2,使用第三方框架解析json数据，例如Gson,fastjson
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean parseJson(String json) {
//        Gson gson = new Gson();
//        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);
        return new Gson().fromJson(json, NewsCenterPagerBean.class);
    }

    /**
     * 根据位置切换详情页面
     *
     * @param position
     */
    public void switchPager(int position) {
        // 1.设置标题
        tvTitle.setText(data.get(position).getTitle());
        // 2.移除之前内容
        flContent.removeAllViews();

        // 3.添加新内容
        MenuDetailBasePager basePager = detailBasePagers.get(position);
        View rootView = basePager.rootView;
        basePager.initData();
        flContent.addView(rootView);
        if (position == 2) {
            // 组图详情页面
            ibSwitchListGrid.setVisibility(View.VISIBLE);
            // 设置点击事件
            ibSwitchListGrid.setOnClickListener(view -> {
                // 1.得到组图详情页面对象
                PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                // 2.调用组图对象的切换ListView和GridView的方法
                detailPager.switchListAndGrid(ibSwitchListGrid);
            });
        } else {
            // 其他页面
            ibSwitchListGrid.setVisibility(View.GONE);
        }
    }
}
