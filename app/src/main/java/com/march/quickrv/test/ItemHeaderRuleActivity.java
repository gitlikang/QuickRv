package com.march.quickrv.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
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
    private SlidingSelectLayout ssl;
    private ItemHeaderAdapter<ItemHeader, Content> adapter;
    private int limit = 30;
    private int offset = 0;
    private List<Content> selects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_header_activity);
        RecyclerView mRv = getView(R.id.recyclerview);
        ssl = getView(R.id.ssl);
        ssl.setTagKey(R.string.app_name, R.string.rvquick_key);
        getSupportActionBar().setTitle("每一项都带有Header使用规则匹配Header");
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
//        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mRv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        selects = new ArrayList<>();
        contents = new ArrayList<>();
        for (int i = offset; i < offset + limit; i++) {
            contents.add(new Content("this is new " + i, i));
        }
        offset += limit;
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
                ssl.markView(holder.getParentView(), pos, data);
                ViewGroup.LayoutParams layoutParams = holder.getParentView().getLayoutParams();
                layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels / 3.0f);
                holder.setText(R.id.tv, String.valueOf(data.index));

                if (selects.contains(data)) {
                    holder.getParentView().setBackgroundColor(Color.RED);
                } else {
                    holder.getParentView().setBackgroundColor(Color.GREEN);

                }
            }
        };

        adapter.addItemHeaderRule(new ItemHeaderRule<ItemHeader, Content>() {
            @Override
            public ItemHeader buildItemHeader(int currentPos, Content preData, Content currentData, Content nextData) {
                return new ItemHeader("pre is " + getIndex(preData) + " current is " + getIndex(currentData) + " next is " + getIndex(nextData));
            }

            @Override
            public boolean isNeedItemHeader(int currentPos, Content preData, Content currentData, Content nextData, boolean isCheckAppendData) {
                Log.e("chendong", currentPos + "  " + getString(preData) + "  " + getString(currentData) + "  " + getString(nextData));
                return currentPos == 0 && !isCheckAppendData || currentData.index % 7 == 1;
            }
        });

        adapter.addLoadMoreModule(new LoadMoreModule(2, new OnLoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreModule mLoadMoreModule) {
                List<Content> tempList = new ArrayList<Content>();
                for (int i = offset; i < offset + limit; i++) {
                    tempList.add(new Content("this is new" + i, i));
                }
                offset += limit;
                adapter.appendDataAndUpdate(tempList);
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

        ssl.setOnSlidingSelectListener(new SlidingSelectLayout.OnSlidingSelectListener<Content>() {
            @Override
            public void onSlidingSelect(int pos, View parentView, Content data) {
                if (selects.contains(data)) {
                    selects.remove(data);
                } else {
                    selects.add(data);
                }
                adapter.notifyItemChanged(pos);
            }
        });
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
