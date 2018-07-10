package com.sky.app.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.app.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
    private int tempSize = 2;
    private int realSize = tempSize;
    private WebSettings settings;

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

        settings = webview.getSettings();
        // 设置支持javaScript
        settings.setJavaScriptEnabled(true);
        // 设置双击变大变小
        settings.setUseWideViewPort(true);
        // 增加缩放按钮
        settings.setBuiltInZoomControls(true);
        // 改变字体大小
        settings.setTextZoom(100);
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
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:
//                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                showShare();
                break;
            default:
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        // oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.atguigu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("世界上最遥远的距离，是我在if里你在else里，似乎一直相伴又永远分离；\n" +
                "     世界上最痴心的等待，是我当case你是switch，或许永远都选不上自己；\n" +
                "     世界上最真情的相依，是你在try我在catch。无论你发神马脾气，我都默默承受，静静处理。到那时，再来期待我们的finally。");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.atguigu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("word天哪，太精辟了");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.atguigu.com");

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 显示改变字体大小的对话框
     */
    private void showChangeTextSizeDialog() {
        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        new AlertDialog.Builder(this)
                .setTitle("设置字体大小")
                .setSingleChoiceItems(items, realSize, (dialog, which) -> tempSize = which)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    realSize = tempSize;
                    changeTextSize(realSize);
                })
                .show();
    }

    /**
     * 改变字体大小
     *
     * @param textSize
     */
    private void changeTextSize(int textSize) {
        switch (textSize) {
            case 0:
                // 超大字体
                settings.setTextZoom(200);
                break;
            case 1:
                // 大字体
                settings.setTextZoom(150);
                break;
            case 2:
                // 正常字体
                settings.setTextZoom(100);
                break;
            case 3:
                // 小字体
                settings.setTextZoom(75);
                break;
            case 4:
                // 超小字体
                settings.setTextZoom(50);
                break;
            default:
                break;
        }
    }
}
