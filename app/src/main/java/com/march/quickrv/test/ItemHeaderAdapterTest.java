package com.march.quickrv.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.ItemHeaderAdapter;
import com.march.quickrvlibs.RvAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.inter.OnClickListener;
import com.march.quickrvlibs.inter.OnLoadMoreListener;
import com.march.quickrvlibs.inter.RvQuickItemHeader;
import com.march.quickrvlibs.model.RvQuickModel;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemHeaderAdapterTest extends BaseActivity {

    private int num;
    private SlidingSelectLayout ssl;
    private List<Content> list;
    private ItemHeaderAdapter<ItemHeader, Content> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        setContentView(R.layout.item_header_activity);
        RecyclerView mRv = getView(R.id.recyclerview);
        ssl = getView(R.id.ssl);
        ssl.setTagKey(R.string.app_name,R.string.rvquick_key);
        getSupportActionBar().setTitle("每一项都带有Header的展示");
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
        final List<Content> contents = new ArrayList<>();

        final LinkedHashMap<ItemHeader, List<Content>> map = new LinkedHashMap<>();
        contents.clear();
        for (int i = 0; i < 7; i++) {
            contents.add(new Content("" + i));
        }
        map.put(new ItemHeader("title_1"), contents);

        adapter = new ItemHeaderAdapter<ItemHeader, Content>(this, map,
                R.layout.item_header_header,
                R.layout.item_header_content) {
            @Override
            protected void onBindItemHeader(RvViewHolder holder, ItemHeader data, int pos, int type) {
                holder.setText(R.id.info1, data.getTitle());
            }

            @Override
            protected void onBindContent(RvViewHolder holder, Content data, int pos, int type) {
                ViewGroup.LayoutParams layoutParams = holder.getParentView().getLayoutParams();
                layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels / 3.0f);
                ssl.markView(holder.getParentView(), pos,data);
                TextView tv = (TextView) holder.getView(R.id.tv);
                if (list.contains(data)) {
                    tv.setText("check" + data.title);
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setText("un check" + data.title);
                    tv.setTextColor(Color.GREEN);
                }
            }
        };
        num = 11;
        adapter.addLoadMoreModule(new LoadMoreModule(2, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreModule mLoadMoreModule) {
                Log.e("chendong", "触发");
                map.put(new ItemHeader("new title_" + num++), contents);
                map.put(new ItemHeader("new title_" + num++), contents);
                map.put(new ItemHeader("new title_" + num++), contents);
                adapter.updateDataAndItemHeader(map);
                mLoadMoreModule.finishLoad();
            }
        }));

        adapter.setOnChildClickListener(new OnClickListener<RvQuickModel>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder, RvQuickModel data) {
                if (data.getRvType() == RvAdapter.TYPE_ITEM_DEFAULT) {
                    Content content = data.get();
                    Toast.makeText(ItemHeaderAdapterTest.this, content.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRv.setAdapter(adapter);

        ssl.setOnSlidingSelectListener(new SlidingSelectLayout.OnSlidingSelectListener<Content>() {
            @Override
            public void onSlidingSelect(int pos, View parentView, Content data) {
                Log.e("chendong",data.title);
                if (list.contains(data)) {
                    list.remove(data);
                } else {
                    list.add(data);
                }
                adapter.notifyItemChanged(pos);
            }
        });
    }



    class ItemHeader extends RvQuickItemHeader {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ItemHeader(String title) {
            this.title = title;
        }
    }

    static class Content {
        String title;

        public Content(String title) {
            this.title = title;
        }

    }
}
