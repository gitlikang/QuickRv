package com.march.quickrvlibs.module;

import android.support.v7.widget.RecyclerView;

import com.march.quickrvlibs.RvAdapter;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.module
 * CreateAt : 16/8/20
 * <p>
 * Describe :
 *
 * @author chendong
 */
public abstract class RvModule {
    protected RvAdapter mAttachAdapter;

    public abstract void onAttachedToRecyclerView(RecyclerView recyclerView);

    public void onAttachAdapter(RvAdapter adapter) {
        this.mAttachAdapter = adapter;
    }
}
