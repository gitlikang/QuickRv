package com.march.quickrvlibs.inter;

import android.view.View;

/**
 * QuickRv     com.march.quickrvlibs.inter
 * Created by 陈栋 on 16/4/12.
 * 功能:
 */
public interface HFOperater {

    View getHeader();
    View getFooter();
    void setHeaderEnable(boolean isHeaderEnable);
    void setFooterEnable(boolean isFooterEnable);
}
