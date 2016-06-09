package com.march.quickrv;

import android.content.Context;

import com.march.quickrvlibs.RvQuickAdapter;
import com.march.quickrvlibs.RvViewHolder;

import java.util.List;

/**
 * Created by march on 16/6/9.
 */
public class MyAdapter extends RvQuickAdapter<Demo> {

    public MyAdapter(Context context, List<Demo> data) {
        super(context, data);
    }

    @Override
    public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {

    }

    @Override
    public void bindLisAndData4Footer(RvViewHolder footer) {
        super.bindLisAndData4Footer(footer);
    }

    @Override
    public void bindListener4View(RvViewHolder holder, int type) {
        super.bindListener4View(holder, type);
    }

    @Override
    public void bindLisAndData4Header(RvViewHolder header) {
        super.bindLisAndData4Header(header);
    }
}
