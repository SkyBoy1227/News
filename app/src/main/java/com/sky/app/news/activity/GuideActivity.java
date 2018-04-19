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
    @BindView(R.id.iv_point_red)
    ImageView ivPointRed;

    private List<ImageView> imageViews;

    /**
     * 两点的间距
     */
    private int leftmax;

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

        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            // 设置背景
            imageView.setBackgroundResource(ids[i]);

            // 添加到集合中
            imageViews.add(imageView);

            // 创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            /*
             * 单位是像素
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                // 不包括第0个，所有的点距离左边有10个像素
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            // 添加到线性布局里面
            llPointGroup.addView(point);
        }

        // 设置ViewPager的适配器
        viewPager.setAdapter(new MyPagerAdapter());

        // 根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距就都有了
        ivPointRed.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            // 间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftmax = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
        });
        // 得到屏幕滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滑动了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中的页面对应的位置
         */
        @Override
        public void onPageSelected(int position) {

        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
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

