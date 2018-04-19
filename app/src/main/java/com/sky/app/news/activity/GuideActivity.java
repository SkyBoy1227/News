package com.sky.app.news.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.app.news.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 引导页面
 * Date: 2018/4/19
 * Time: 11:12
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_start_main)
    Button btnStartMain;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;

    private List<ImageView> imageViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        // 准备数据
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };
        imageViews = new ArrayList<>();
        for (int id : ids) {
            ImageView imageView = new ImageView(this);
            // 设置背景
            imageView.setBackgroundResource(id);

            // 添加到集合中
            imageViews.add(imageView);
        }
        // 设置ViewPager的适配器
        viewPager.setAdapter(new MyPagerAdapter());
    }

    class MyPagerAdapter extends PagerAdapter {
        /**
         * 返回数据的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 作用，getView
         *
         * @param container ViewPager
         * @param position  要创建页面的位置
         * @return 返回和创建当前页面有关系的值
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            // 添加到容器中
            container.addView(imageView);
            return imageView;
//            return position;
        }

        /**
         * 判断
         *
         * @param view   当前创建的视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
//            return view == imageViews.get(Integer.parseInt((String) object));
        }

        /**
         * 销毁页面
         *
         * @param container ViewPager
         * @param position  要销毁页面的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}

