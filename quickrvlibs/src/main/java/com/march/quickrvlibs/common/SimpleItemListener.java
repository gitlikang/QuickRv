package com.march.quickrvlibs.common;

import com.march.quickrvlibs.adapter.BaseViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.common
 * CreateAt : 2016/11/9
 * Describe :
 *
 * @author chendong
 */

public abstract class SimpleItemListener<D> implements OnItemListener<D> {
    @Override
    public void onClick(int pos, BaseViewHolder holder, D data) {

    }

    @Override
    public void onLongPress(int pos, BaseViewHolder holder, D data) {

    }

    @Override
    public void onDoubleClick(int pos, BaseViewHolder holder, D data) {

    }
}
