package com.march.quickrv.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.adapter.SectionRvAdapter;
import com.march.quickrvlibs.adapter.AbsAdapter;
import com.march.quickrvlibs.adapter.BaseViewHolder;
import com.march.quickrvlibs.common.ISectionRule;
import com.march.quickrvlibs.common.OnItemClickListener;
import com.march.quickrvlibs.common.OnLoadMoreListener;
import com.march.quickrvlibs.common.AbsSectionHeader;
import com.march.quickrvlibs.model.ItemModel;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.ArrayList;
import java.util.List;

public class ItemHeaderRuleActivity extends BaseActivity {

    private List<Content> contents;
    private SlidingSelectLayout ssl;
    private SectionRvAdapter<ItemHeader, Content> adapter;
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
        adapter = new SectionRvAdapter<ItemHeader, Content>(
                this,
                contents,
                R.layout.item_header_header,
                R.layout.item_header_content) {
            @Override
            protected void onBindItemHeader(BaseViewHolder holder, ItemHeader data, int pos, int type) {
                holder.setText(R.id.info1, data.getTitle());
            }

            @Override
            protected void onBindContent(BaseViewHolder holder, Content data, int pos, int type) {
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

        adapter.addItemHeaderRule(new ISectionRule<ItemHeader, Content>() {
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
                adapter.appendSectionTailRangeData(tempList);
                mLoadMoreModule.finishLoad();
            }
        }));
        adapter.setOnItemClickListener(new OnItemClickListener<ItemModel>() {
            @Override
            public void onItemClick(int pos, BaseViewHolder holder, ItemModel data) {
                if (data.getRvType() == AbsAdapter.TYPE_ITEM_DEFAULT) {
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

    class ItemHeader extends AbsSectionHeader {
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
