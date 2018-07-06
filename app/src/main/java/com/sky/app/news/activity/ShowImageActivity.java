package com.sky.app.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.sky.app.news.R;
import com.squareup.picasso.Picasso;

/**
 * Created with Android Studio.
 * 描述: 图片详情页面
 * Date: 2018/7/6
 * Time: 11:05
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class ShowImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        PhotoView photoView = findViewById(R.id.iv_photo);
        String imageUrl = getIntent().getStringExtra("url");
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.home_scroll_default)
                .error(R.drawable.home_scroll_default)
                .into(photoView);
    }
}
