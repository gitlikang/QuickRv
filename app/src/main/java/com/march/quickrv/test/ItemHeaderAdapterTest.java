package com.march.quickrv.test;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.ItemHeaderAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.inter.RvQuickInterface;
import com.march.quickrvlibs.inter.RvQuickItemHeader;

import java.util.ArrayList;
import java.util.List;

public class ItemHeaderAdapterTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_header_activity);
        RecyclerView mRv = getView(R.id.recyclerview);
        getSupportActionBar().setTitle("每一项都带有Header的展示");
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
        ItemHeaderAdapter<ItemHeader, Content> adapter = new ItemHeaderAdapter<ItemHeader, Content>(this) {

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
        adapter.addHeaderLayout(R.layout.item_header_header);
        adapter.addType(Content.TYPE_CONTENT, R.layout.item_header_content);

        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            contents.add(new Content("this is " + i));
        }

        adapter.addData(new ItemHeader("title_1"), contents);
        adapter.addData(new ItemHeader("title_2"), contents);
        adapter.addData(new ItemHeader("title_3"), contents);
        adapter.addData(new ItemHeader("title_4"), contents);
        adapter.addData(new ItemHeader("title_5"), contents);

        mRv.setAdapter(adapter);
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

    static class Content implements RvQuickInterface {
        public static final int TYPE_CONTENT = 1;
        String title;

        public Content(String title) {
            this.title = title;
        }

        @Override
        public int getRvType() {
            return TYPE_CONTENT;
        }
    }
}
