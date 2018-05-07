package com.sky.app.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.app.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created with Android Studio.
 * 描述: 新闻浏览页面
 * Date: 2018/5/7
 * Time: 10:11
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class NewsDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @BindView(R.id.ib_share)
    ImageButton ibShare;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra("url");

        WebSettings settings = webview.getSettings();
        // 设置支持javaScript
        settings.setJavaScriptEnabled(true);
        // 设置双击变大变小
        settings.setUseWideViewPort(true);
        // 增加缩放按钮
        settings.setBuiltInZoomControls(true);
        // 不让当前网页跳转到系统的浏览器中
        webview.setWebViewClient(new WebViewClient() {
            // 当加载页面完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }

    private void initView() {
        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                Toast.makeText(this, "设置字体大小", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}