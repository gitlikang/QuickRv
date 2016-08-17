package com.march.quickrvlibs.model;

import com.march.quickrvlibs.inter.RvQuickInterface;

/**
 * QuickAdapter     com.march.adapterlibs.bean
 * Created by 陈栋 on 16/4/8.
 * 功能:
 */
public class RvQuickModel implements RvQuickInterface {

    private Object t;

    public RvQuickModel(Object t) {
        this.t = t;
    }

    public <T> T get() {
        return (T) t;
    }

    public void put(Object t) {
        this.t = t;
    }

    @Override
    public int getRvType() {
        return 0;
    }
}
