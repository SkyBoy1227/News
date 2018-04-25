package com.sky.app.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sky.app.news.activity.MainActivity;
import com.sky.app.news.base.BasePager;
import com.sky.app.news.domain.NewsCenterPagerBean;
import com.sky.app.news.fragment.LeftMenuFragment;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
    private List<NewsCenterPagerBean.DataBean> data;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        ivMenu.setVisibility(View.VISIBLE);
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
        getDataFromNet();
    }

    /**
     * 使用xUtils3联网请求数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功==" + result);
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
        NewsCenterPagerBean bean = parseJson(json);
        String title = bean.getData().get(0).getChildren().get(0).getTitle();
        LogUtil.e("使用Gson解析json数据成功，title = " + title);
        // 将得到的数据传递给左侧菜单
        data = bean.getData();
        MainActivity mainActivity = (MainActivity) context;
        // 得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(data);
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
}
