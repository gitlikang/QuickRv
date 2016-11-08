package com.march.quickrvlibs.module;

import android.support.v7.widget.RecyclerView;

import com.march.quickrvlibs.adapter.AbsAdapter;

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

    protected AbsAdapter mAttachAdapter;

    public abstract void onAttachedToRecyclerView(RecyclerView recyclerView);

    public void onAttachAdapter(AbsAdapter adapter) {
        this.mAttachAdapter = adapter;
    }
}
