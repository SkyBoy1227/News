package com.sky.app.news.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.app.news.R;
import com.sky.app.news.activity.MainActivity;
import com.sky.app.news.base.BaseFragment;
import com.sky.app.news.domain.NewsCenterPagerBean;
import com.sky.app.news.pager.NewsCenterPager;
import com.sky.app.news.utils.DensityUtil;
import com.sky.app.news.utils.LogUtil;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 左侧菜单Fragment
 * Date: 2018/4/20
 * Time: 14:24
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class LeftMenuFragment extends BaseFragment {
    private List<NewsCenterPagerBean.DataBean> data;

    private ListView listView;

    /**
     * 左侧菜单的适配器
     */
    private LeftMenuFragmentAdapter adapter;

    /**
     * 点击的位置
     */
    private int prePosition;

    @Override
    protected View initView() {
        LogUtil.e("左侧菜单Fragment视图被初始化了");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        // 设置分割线高度为0
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        // 设置按下listView的item不变色
        listView.setSelector(android.R.color.transparent);
        // 设置item的点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 1.记录点击的位置，变成红色
            prePosition = position;
            // getCount()-->getView()
            adapter.notifyDataSetChanged();

            // 2.把左侧菜单关闭
            MainActivity mainActivity = (MainActivity) context;
            // 关<->开
            mainActivity.getSlidingMenu().toggle();

            // 3.切换到对应的详情页面：新闻详情页面，专题详情页面，组图详情页面，互动详情页面
            switchPager(prePosition);
        });
        return listView;
    }

    /**
     * 根据位置切换不同详情页面
     *
     * @param position
     */
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(position);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单Fragment数据被初始化了");
    }

    /**
     * 接收数据
     *
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (NewsCenterPagerBean.DataBean bean : data) {
            LogUtil.e("title = " + bean.getTitle());
        }
        // 设置适配器
        adapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(adapter);
        switchPager(prePosition);
    }

    class LeftMenuFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_left_menu, null);
            textView.setText(data.get(position).getTitle());
//            if (prePosition == position) {
//                textView.setEnabled(true);
//            } else {
//                textView.setEnabled(false);
//            }
            textView.setEnabled(prePosition == position);
            return textView;
        }
    }
}
