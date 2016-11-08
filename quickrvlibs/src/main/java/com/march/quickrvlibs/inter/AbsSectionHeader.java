package com.march.quickrvlibs.inter;
import com.march.quickrvlibs.adapter.AbsAdapter;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.inter
 * CreateAt : 16/8/31
 *
 * Describe : 每一项的header
 *
 * @author chendong
 */
public abstract class AbsSectionHeader implements IAdapterModel {
    @Override
    public int getRvType() {
        return AbsAdapter.TYPE_ITEM_HEADER;
    }
}
