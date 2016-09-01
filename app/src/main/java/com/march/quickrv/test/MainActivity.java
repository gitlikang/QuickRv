package com.march.quickrv.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.SimpleRvAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        RecyclerView mRv = getView(R.id.recyclerview);
        List<GuideData> mGuideDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mGuideDatas.add(new GuideData(
                    ItemHeaderTest.class,
                    "ItemHeaderAdapter",
                    "每一项都有一个header的显示效果，类似九宫格展示，是分类适配的一个子分类"));
        }

        SimpleRvAdapter<GuideData> adapter = new SimpleRvAdapter<GuideData>(mContext, mGuideDatas, R.layout.main_guide) {
            @Override
            public void onBindView(RvViewHolder holder, GuideData data, int pos, int type) {
                holder.setText(R.id.guide_title, data.title)
                        .setText(R.id.guide_desc, data.desc);
            }
        };
        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter(adapter);
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
