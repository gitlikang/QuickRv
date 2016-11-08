package com.march.quickrv.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.adapter.BaseViewHolder;
import com.march.quickrvlibs.adapter.SimpleRvAdapter;
import com.march.quickrvlibs.module.HFModule;

import java.util.ArrayList;
import java.util.List;

public class HeaderFooterTest extends BaseActivity {

    public static final int LM_LINEAR = 0;
    public static final int LM_GRID = 1;
    public static final int LM_STAGGERED = 2;
    private RecyclerView mRv;
    private SimpleRvAdapter<HFModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_footer_activity);
        getSupportActionBar().setTitle("添加Header和Footer模块");
        mRv = getView(R.id.recyclerview);
        changeLayoutManager(LM_STAGGERED);
        List<HFModel> hfModels = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            hfModels.add(new HFModel(i));
        }


        adapter = new SimpleRvAdapter<HFModel>(mContext, hfModels, R.layout.header_footer_item) {
            @Override
            public void onBindView(BaseViewHolder holder, HFModel data, int pos, int type) {
                if (pos % 5 == 0) {
                    holder.getParentView().getLayoutParams().height = 300;
                    holder.setText(R.id.info, "高度" + 300 + "px");
                    return;
                }
                if (pos % 3 == 0) {
                    holder.getParentView().getLayoutParams().height = 250;
                    holder.setText(R.id.info, "高度" + 250 + "px");
                    return;
                }
                if (pos % 2 == 0) {
                    holder.getParentView().getLayoutParams().height = 200;
                    holder.setText(R.id.info, "高度" + 200 + "px");
                    return;
                }
                holder.getParentView().getLayoutParams().height = 350;
                holder.setText(R.id.info, "高度" + 350 + "px");
            }
        };
        HFModule hfModule = new HFModule(mContext,
                R.layout.header_footer_headerly,
                R.layout.header_footer_footerly, mRv);
        adapter.addHFModule(hfModule);

        mRv.setAdapter(adapter);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               adapter.getHFModule().setFooterEnable(false);
            }
        },2000);
    }

    public void clickLinear(View view) {
        changeLayoutManager(LM_LINEAR);
        adapter.notifyLayoutManagerChanged();
    }

    public void clickGrid(View view) {
        changeLayoutManager(LM_GRID);
        adapter.notifyLayoutManagerChanged();
    }

    public void clickStaggered(View view) {
        changeLayoutManager(LM_STAGGERED);
        mRv.requestLayout();
        // 中途切换layout manager需要更新，保证header和footer可以转换
        adapter.notifyLayoutManagerChanged();
    }

    class HFModel {
        int index;
        public HFModel(int index) {
            this.index = index;
        }
    }

    private void changeLayoutManager(int type) {
        switch (type) {
            case LM_LINEAR:
                mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                break;
            case LM_GRID:
                mRv.setLayoutManager(new GridLayoutManager(mContext, 2));
                break;
            case LM_STAGGERED:
                mRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
    }
}
