package com.march.quickrv.model;

import com.march.quickrvlibs.common.IAdapterModel;
/**
 * QuickRv     com.march.quickrv
 * Created by 陈栋 on 16/3/15.
 * 功能:
 */
public class Demo implements IAdapterModel {

    private boolean isSingleType = false;
    @Override
    public int getRvType() {
        if(isSingleType)
            return 0;
        return index % 3 == 0 ? 0 : 1;
    }

    String title;
    int index;

    public Demo(int index, String title) {
        this.index = index;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "isSingleType=" + isSingleType +
                ", title='" + title + '\'' +
                ", index=" + index +
                '}';
    }
}
