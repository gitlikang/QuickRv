package com.march.quickrvlibs.inter;

import com.march.quickrvlibs.RvAdapter;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.inter
 * CreateAt : 16/8/31
 *
 * Describe : 每一项的header
 *
 * @author chendong
 */
public abstract class RvQuickItemHeader implements RvQuickInterface {
    @Override
    public int getRvType() {
        return RvAdapter.TYPE_ITEM_HEADER;
    }
}
