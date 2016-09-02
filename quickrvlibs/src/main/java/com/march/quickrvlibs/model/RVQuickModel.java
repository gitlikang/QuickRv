package com.march.quickrvlibs.model;

import com.march.quickrvlibs.inter.RvQuickInterface;

/**
 * QuickAdapter     com.march.adapterlibs.bean
 * Created by 陈栋 on 16/4/8.
 * 功能:
 */
public class RvQuickModel implements RvQuickInterface {

    private Object t;
    private int type;

    public RvQuickModel(Object t) {
        this.t = t;
        if (t instanceof RvQuickInterface) {
            this.type = ((RvQuickInterface) t).getRvType();
        }
    }

    public RvQuickModel(Object t, int type) {
        this.t = t;
        if (t instanceof RvQuickInterface) {
            this.type = ((RvQuickInterface) t).getRvType();
        } else {
            this.type = type;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public <T> T get() {
        return (T) t;
    }

    public void put(Object t) {
        this.t = t;
    }

    @Override
    public int getRvType() {
        return type;
    }
}
