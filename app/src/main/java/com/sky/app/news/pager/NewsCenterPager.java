package com.sky.app.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.sky.app.news.base.BasePager;
import com.sky.app.news.utils.Constants;
import com.sky.app.news.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
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
}
