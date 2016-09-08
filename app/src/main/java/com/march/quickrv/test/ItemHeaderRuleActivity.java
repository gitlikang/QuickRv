package com.march.quickrv.test;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.ItemHeaderAdapter;
import com.march.quickrvlibs.RvAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.inter.ItemHeaderRule;
import com.march.quickrvlibs.inter.OnClickListener;
import com.march.quickrvlibs.inter.OnLoadMoreListener;
import com.march.quickrvlibs.inter.RvQuickInterface;
import com.march.quickrvlibs.inter.RvQuickItemHeader;
import com.march.quickrvlibs.model.RvQuickModel;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.ArrayList;
import java.util.List;

public class ItemHeaderRuleActivity extends BaseActivity {

    private List<Content> contents;
    private ItemHeaderAdapter<ItemHeader, Content> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_header_activity);
        RecyclerView mRv = getView(R.id.recyclerview);
        getSupportActionBar().setTitle("每一项都带有Header使用规则匹配Header");
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
//        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mRv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        contents = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            contents.add(new Content("this is " + i, i));
        }

        adapter = new ItemHeaderAdapter<ItemHeader, Content>(
                this,
                contents,
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
            }
        };

        adapter.addItemHeaderRule(new ItemHeaderRule<ItemHeader, Content>() {
            @Override
            public ItemHeader buildItemHeader(int currentPos, Content preData, Content currentData, Content nextData) {
                return new ItemHeader("pre is " + getIndex(preData) + " current is " + getIndex(currentData) + " next is " + getIndex(nextData));
            }

            @Override
            public boolean isNeedItemHeader(int currentPos, Content preData, Content currentData, Content nextData) {
                Log.e("chendong", getString(preData) + "  " + getString(currentData) + "  " + getString(nextData));
                return currentData.index % 5 == 0;
            }
        });

        adapter.addLoadMoreModule(new LoadMoreModule(2, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreModule mLoadMoreModule) {
                for (int i = 0; i < 50; i++) {
                    contents.add(new Content("this is new" + i, i));
                }
                adapter.updateDataAndItemHeader(contents);
                mLoadMoreModule.finishLoad();
            }
        }));
        adapter.setOnChildClickListener(new OnClickListener<RvQuickModel>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder, RvQuickModel data) {
                if (data.getRvType() == RvAdapter.TYPE_ITEM_DEFAULT) {
                    Content content = data.get();
                    Toast.makeText(ItemHeaderRuleActivity.this, content.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRv.setAdapter(adapter);
    }

    private String getString(Content content) {
        if (content == null)
            return "null";
        return content.toString();
    }

    private String getIndex(Content content) {
        if (content == null)
            return "null";
        return content.index + "";
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
        public static final int TYPE_CONTENT = 1;
        String title;
        int index;

        @Override
        public String toString() {
            return "Content{" +
                    "title='" + title + '\'' +
                    ", index=" + index +
                    '}';
        }

        public Content(String title, int index) {
            this.title = title;
            this.index = index;
        }
    }
}
