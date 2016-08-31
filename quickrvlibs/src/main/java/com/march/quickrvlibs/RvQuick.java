package com.march.quickrvlibs;
import com.march.quickrvlibs.helper.QuickLoad;


/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/3/15.
 * 功能:配置初始化参数使用
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
