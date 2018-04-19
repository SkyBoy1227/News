package com.sky.app.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
                Toast.makeText(SplashActivity.this, "动画播放完成！", Toast.LENGTH_SHORT).show();
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
