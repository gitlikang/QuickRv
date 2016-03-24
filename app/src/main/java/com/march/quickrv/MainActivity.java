package com.march.quickrv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.march.quickrvlibs.RvFooterHolder;
import com.march.quickrvlibs.RvHeaderHolder;
import com.march.quickrvlibs.RvQuickAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.helper.GridDividerDecoration;
import com.march.quickrvlibs.inter.OnRecyclerItemClickListener;
import com.march.quickrvlibs.inter.OnRecyclerItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Context self = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rvquick_activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        List<Demo> demos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            demos.add(new Demo(i, i + " <- this is"));
        }

        final RvQuickAdapter rvQuickAdapter;

        //单类型适配
//        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos, R.layout.rvquick_item_a) {
//            @Override
//            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
//                holder.setText(R.id.item_a_tv, data.title);
//            }
//        };

        //多类型适配
        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv, data.title + "   type2");
            }

            @Override
            public void bindLisAndData4Header(RvHeaderHolder holder) {
                super.bindLisAndData4Header(holder);
                holder.setText(R.id.header_tv, "我真的是header");
            }

            @Override
            public void bindLisAndData4Footer(RvFooterHolder holder) {
                super.bindLisAndData4Footer(holder);
                holder.setText(R.id.footer_tv, "我真的是footer");
            }
        };

        // 设置item监听事件
        rvQuickAdapter.setClickListener(new OnRecyclerItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {
                Toast.makeText(MainActivity.this, "点击" + rvQuickAdapter.getDataPos(pos), Toast.LENGTH_SHORT).show();
            }
        });

        //设置item长按事件
        rvQuickAdapter.setLongClickListener(new OnRecyclerItemLongClickListener<RvViewHolder>() {
            @Override
            public void onItemLongClick(int pos, RvViewHolder holder) {
                Toast.makeText(MainActivity.this, "长按" + rvQuickAdapter.getDataPos(pos), Toast.LENGTH_SHORT).show();
            }
        });

        rvQuickAdapter.addType(0, R.layout.rvquick_item_a).addType(1, R.layout.rvquick_item_b);
//        rvQuickAdapter.addHeader(LayoutInflater.from(this).inflate(R.layout.rvquick_header, null));
//        rvQuickAdapter.addFooter(LayoutInflater.from(this).inflate(R.layout.rvquick_footer, null));

        rvQuickAdapter.addHeader(R.layout.rvquick_header);
        rvQuickAdapter.addFooter(R.layout.rvquick_footer);

//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        //你可以使用shape自定义分割线样式
        Drawable line = ContextCompat.getDrawable(self, R.drawable.shape_line);
//        new GridDividerDecoration(self,line);
//        new LinearDividerDecoration(self, LinearDividerDecoration.VERTICAL_LIST, line)
        recyclerView.addItemDecoration(new GridDividerDecoration(self, line));
        recyclerView.setAdapter(rvQuickAdapter);
    }
}
