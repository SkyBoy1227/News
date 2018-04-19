package com.sky.app.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.sky.app.news.R;
import com.sky.app.news.utils.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 欢迎页
 * Date: 2018/4/18
 * Time: 16:43
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * 静态常量
     */
    public static final String START_MAIN = "start_main";

    @BindView(R.id.rl_splash_root)
    RelativeLayout rlSplashRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        // 渐变动画，缩放动画，旋转动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(2000);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 当动画开始播放的时候回调这个方法
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * 当动画播放结束的时候回调这个方法
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                // 判断是否进入过主页面
                boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this, START_MAIN);
                if (isStartMain) {
                    // 如果进入过主页面，直接进入主页面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 如果没有进入过主页面，进入引导页面
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                }
                // 关闭Splash页面
                finish();
//                Toast.makeText(SplashActivity.this, "动画播放完成！", Toast.LENGTH_SHORT).show();
            }

            /**
             * 当动画重复播放的时候回调这个方法
             * @param animation
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rlSplashRoot.startAnimation(animationSet);
    }
}
