package com.march.quickrvlibs;
import com.march.quickrvlibs.inter.ILoadImage;

/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/3/15.
 * 功能:配置初始化参数使用
 */
public class RvQuick {
    public static ILoadImage imgLoadTool;

    public static void init(ILoadImage tool) {
        imgLoadTool = tool;
    }

    public static ILoadImage get() {
        if (imgLoadTool == null) {
            throw new IllegalStateException("if u want to load online img,u must invoke Quick.init() at the first!");
        }
        return imgLoadTool;
    }
}
