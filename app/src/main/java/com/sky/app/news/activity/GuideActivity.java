package com.sky.app.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sky.app.news.R;
import com.sky.app.news.utils.CacheUtils;
import com.sky.app.news.utils.DensityUtil;

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
    private static final String TAG = GuideActivity.class.getSimpleName();

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
    private int widthdpi;

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
        widthdpi = DensityUtil.dip2px(this, 10);
        Log.e(TAG, "widthdpi = " + widthdpi);

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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);
            if (i != 0) {
                // 不包括第0个，所有的点距离左边有10个像素
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            // 添加到线性布局里面
            llPointGroup.addView(point);
        }

        // 设置ViewPager的适配器
        viewPager.setAdapter(new MyPagerAdapter());

        // 根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距就都有了
        ivPointRed.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        // 得到屏幕滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 设置按钮的点击事件
        btnStartMain.setOnClickListener(v -> {
            // 1.保存曾经进入过主页面
            CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN, true);

            // 2.跳转到主页面
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);

            // 3.关闭引导页面
            finish();
        });
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
            // 两点间移动的距离 = 屏幕滑动百分比 * 间距
//            int leftmargin = (int) (positionOffset * leftmax);
//            Log.e(TAG, "position = " + position + " , positionOffset = " + positionOffset + " , positionOffsetPixels = " + positionOffsetPixels);
            // 两点间滑动距离对应的坐标 = 原来的起始位置 +  两点间移动的距离
            int leftmargin = (int) (position * leftmax + positionOffset * leftmax);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPointRed.getLayoutParams();
            // params.leftMargin = 两点间滑动距离对应的坐标
            params.leftMargin = leftmargin;
            ivPointRed.setLayoutParams(params);
        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中的页面对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                // 最后一个页面
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                // 其他页面
                btnStartMain.setVisibility(View.GONE);
            }
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

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            // 该接口的方法执行不只一次，为了避免执行多次，要加上如下代码：
            ivPointRed.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
            // 间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftmax = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
            Log.e(TAG, "leftmax = " + leftmax);
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

