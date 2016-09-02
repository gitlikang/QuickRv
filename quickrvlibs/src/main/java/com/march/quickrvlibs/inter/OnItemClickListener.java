package com.march.quickrvlibs.inter;

/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/2/4.
 * 功能: instead by OnChildClickListener
 */
@Deprecated
public interface OnItemClickListener<VH> {
    void onItemClick(int pos, VH holder);
}
