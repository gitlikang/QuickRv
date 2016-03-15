package com.march.quickrv;

import com.march.quickrvlibs.inter.RvQuickInterface;

/**
 * QuickRv     com.march.quickrv
 * Created by 陈栋 on 16/3/15.
 * 功能:
 */
public class Demo implements RvQuickInterface{
    @Override
    public int getRvType() {
        return 0;
    }

    String title;

    public Demo(String title) {
        this.title = title;
    }
}
