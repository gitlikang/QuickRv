package com.march.quickrv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.quickrvlibs.RvQuickAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.inter.OnRecyclerItemClickListener;
import com.march.quickrvlibs.inter.OnRecyclerItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rvquick_activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        List<Demo> demos = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            demos.add(new Demo(i, i + " <- this is"));
        }
        RvQuickAdapter rvQuickAdapter;

        //单类型适配
        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos, R.layout.rvquick_item_a) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                holder.setText(R.id.item_a_tv, data.title);
            }
        };

        //多类型适配
        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos, R.layout.rvquick_item_a) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv,  "type2");
            }
        };

        // 设置item监听事件
        rvQuickAdapter.setClickListener(new OnRecyclerItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {

            }
        });
        //设置item长按事件
        rvQuickAdapter.setLongClickListenter(new OnRecyclerItemLongClickListener<RvViewHolder>() {
            @Override
            public void onItemLongClick(int pos, RvViewHolder holder) {

            }
        });
        rvQuickAdapter.addType(0, R.layout.rvquick_item_a).addType(1, R.layout.rvquick_item_b);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerView.setAdapter(rvQuickAdapter);
    }
}
