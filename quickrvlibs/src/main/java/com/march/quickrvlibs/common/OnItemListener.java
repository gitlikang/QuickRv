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

public interface OnItemListener<D> {
    void onClick(int pos, BaseViewHolder holder, D data);

    void onLongPress(int pos, BaseViewHolder holder, D data);

    void onDoubleClick(int pos, BaseViewHolder holder, D data);
}
