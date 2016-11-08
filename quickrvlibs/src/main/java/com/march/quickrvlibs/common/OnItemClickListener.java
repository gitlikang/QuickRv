package com.march.quickrvlibs.common;

import com.march.quickrvlibs.adapter.BaseViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.adapter
 * CreateAt : 2016/1/9
 * Describe : item单击事件
 *
 * @author chendong
 */
public interface OnItemClickListener<D> {
    void onItemClick(int pos, BaseViewHolder holder, D data);
}
