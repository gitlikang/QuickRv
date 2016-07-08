package com.march.quickrvlibs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.march.quickrvlibs.R;
import com.march.quickrvlibs.helper.QuickLoad;


/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/3/15.
 * 功能:
 */
public class RvQuick {
    public static QuickLoad imgLoadTool;

    public static void init(QuickLoad tool) {
        imgLoadTool = tool;
    }

    public static QuickLoad get() {
        if (imgLoadTool == null) {
            throw new IllegalStateException("if u want to load online img,u must invoke Quick.init() at the first!");
        }
        return imgLoadTool;
    }
}
