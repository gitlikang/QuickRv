package com.march.quickrv;

import com.march.quickrvlibs.inter.RvQuickInterface;
/**
 * QuickRv     com.march.quickrv
 * Created by 陈栋 on 16/3/15.
 * 功能:
 */
public class Demo implements RvQuickInterface {
    @Override
    public int getRvType() {
        return index % 3 == 0 ? 0 : 1;
    }

    String title;
    int index;

    public Demo(int index, String title) {
        this.index = index;
        this.title = title;
    }
}
