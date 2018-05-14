package com.sky.app.news.menudetailpager;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.sky.app.news.R;
import com.sky.app.news.base.MenuDetailBasePager;
import com.sky.app.news.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created with Android Studio.
 * 描述: 组图详情页面
 * Date: 2018/4/25
 * Time: 12:23
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.gridView)
    GridView gridView;

    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_mene_detail_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("组图详情页面数据被初始化了..");
    }
}
