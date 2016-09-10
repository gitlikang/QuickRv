package com.march.quickrv.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.SimpleRvAdapter;
import com.march.quickrvlibs.inter.OnLoadMoreListener;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreTest extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_more_activity);
        getSupportActionBar().setTitle("加载更多模块");
        RecyclerView mRv = getView(R.id.recyclerview);
//        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRv.setLayoutManager(new GridLayoutManager(mContext, 2));

        final List<LoadMoreModel> datas = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            datas.add(new LoadMoreModel("this is " + i));
        }

        final SimpleRvAdapter<LoadMoreModel> adapter = new SimpleRvAdapter<LoadMoreModel>(mContext, datas, R.layout.load_more_item) {
            @Override
            public void onBindView(RvViewHolder holder, LoadMoreModel data, int pos, int type) {
                holder.setText(R.id.info, data.desc);
            }
        };

        LoadMoreModule loadMoreM = new LoadMoreModule(4, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreModule mLoadMoreModule) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 9; i++) {
                            datas.add(new LoadMoreModel("new is " + i));
                        }
                        adapter.updateRangeInsert(datas);
                        adapter.getLoadMoreModule().finishLoad();
                    }
                }, 1500);
            }
        });
        adapter.addLoadMoreModule(loadMoreM);
        mRv.setAdapter(adapter);
    }

    class LoadMoreModel {
        String desc;

        public LoadMoreModel(String desc) {
            this.desc = desc;
        }
    }
}
