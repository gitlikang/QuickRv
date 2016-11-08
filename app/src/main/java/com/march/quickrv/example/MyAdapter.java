package com.march.quickrv.example;

import android.content.Context;

import com.march.quickrv.model.Demo;
import com.march.quickrvlibs.adapter.TypeRvAdapter;
import com.march.quickrvlibs.adapter.BaseViewHolder;

import java.util.List;

public class MyAdapter extends TypeRvAdapter<Demo> {

    public MyAdapter(Context context, List<Demo> data) {
        super(context, data);
    }

    @Override
    public void onBindView(BaseViewHolder holder, Demo data, int pos, int type) {

    }

    @Override
    public void onBindFooter(BaseViewHolder footer) {
        super.onBindFooter(footer);
    }

    @Override
    public void onBindHeader(BaseViewHolder header) {
        super.onBindHeader(header);
    }
}
