package com.march.quickrv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        List<Demo> demos = new ArrayList<>();
        for(int i=0;i<100;i++){
            demos.add(new Demo(i + " <- this is"));
        }
//        RvQuickAdapter rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this,) {
//
//            @Override
//            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
//
//            }
//        };
    }
}
