package com.march.quickrv.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.SimpleRvAdapter;
import com.march.quickrvlibs.inter.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<GuideData> mGuideDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportActionBar().setTitle("主页");
        RecyclerView mRv = getView(R.id.recyclerview);
        initDatas();
        SimpleRvAdapter<GuideData> adapter = new SimpleRvAdapter<GuideData>(mContext, mGuideDatas, R.layout.main_guide) {
            @Override
            public void onBindView(RvViewHolder holder, GuideData data, int pos, int type) {
                holder.setText(R.id.guide_title, data.title)
                        .setText(R.id.guide_desc, data.desc);
            }
        };
        adapter.setOnChildClickListener(new OnClickListener<GuideData>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder, GuideData data) {
                if (data.cls == null) {
                    Toast.makeText(mContext, data.desc + "(该项仅用作介绍)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (data.cls.equals(MainActivity.class)) {
                    Toast.makeText(mContext, "当前页面使用单类型数据适配器展示", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(mContext, data.cls));
            }
        });
        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter(adapter);
    }

    private void initDatas() {
        mGuideDatas = new ArrayList<>();
        mGuideDatas.add(new GuideData(
                MainActivity.class,
                "SimpleRvAdapter",
                "单类型数据适配针对大多数使用情况"));
        mGuideDatas.add(new GuideData(
                TypeAdapterTest.class,
                "TypeRvAdapter",
                "分类型进行数据适配，每种类型显示不同视图"));
        mGuideDatas.add(new GuideData(
                ItemHeaderAdapterTest.class,
                "ItemHeaderAdapter",
                "每一项都有一个header的显示效果，类似九宫格展示，是分类适配的一个子分类"));
        mGuideDatas.add(new GuideData(
                ItemHeaderRuleActivity.class,
                "ItemHeaderAdapter",
                "每一项都有一个header的显示效果，使用规则自动生成header"));
        mGuideDatas.add(new GuideData(
                HeaderFooterTest.class,
                "HeaderFooterModule",
                "给RecyclerView添加Header和Footer显示"));
        mGuideDatas.add(new GuideData(
                LoadMoreTest.class,
                "LoadMoreModule",
                "到达底部之前预加载数据，加载更多"));
        mGuideDatas.add(new GuideData(
                null,
                "点击事件",
                "触发点击事件可以准确获取当前点击的数据，不需要获得pos之后在进行获取数据"));
        mGuideDatas.add(new GuideData(
                null,
                "RvViewHolder",
                "简化基本常用操作"));

    }


    class GuideData {
        Class cls;
        String title;
        String desc;

        public GuideData(Class cls, String title, String desc) {
            this.cls = cls;
            this.title = title;
            this.desc = desc;
        }
    }
}
