package com.march.quickrvlibs.common;

import com.march.quickrvlibs.adapter.BaseViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.adapter
 * CreateAt : 2016/14/2
 * Describe : 长按Item事件
 *
 * @author chendong
 */
public interface OnItemLongClickListener<D> {
    void onItemLongClick(int pos, BaseViewHolder holder, D data);
}
