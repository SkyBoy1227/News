package com.sky.app.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
