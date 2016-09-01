package com.march.quickrv.example;

import android.content.Context;

import com.march.quickrv.model.Demo;
import com.march.quickrvlibs.TypeRvAdapter;
import com.march.quickrvlibs.RvViewHolder;

import java.util.List;

public class MyAdapter extends TypeRvAdapter<Demo> {

    public MyAdapter(Context context, List<Demo> data) {
        super(context, data);
    }

    @Override
    public void onBindView(RvViewHolder holder, Demo data, int pos, int type) {

    }

    @Override
    public void onBindFooter(RvViewHolder footer) {
        super.onBindFooter(footer);
    }

    @Override
    public void onBindHeader(RvViewHolder header) {
        super.onBindHeader(header);
    }
}
