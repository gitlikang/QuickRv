package com.march.quickrvlibs.inter;

import com.march.quickrvlibs.adapter.RvViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.inter
 * CreateAt : 16/9/1
 * Describe :
 *
 * @author chendong
 */
public interface OnItemClickListener<D> {
    void onItemClick(int pos, RvViewHolder holder, D data);
}
