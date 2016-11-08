package com.march.quickrvlibs.helper;

import com.march.quickrvlibs.adapter.AbsAdapter;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.helper
 * CreateAt : 2016/11/8
 * Describe :
 *
 * @author chendong
 */

public class CommonHelper {

    /**
     * 检测type是否合法
     * @param type 类型
     * @return 合法
     */
    public static boolean checkTypeValid(int type) {
        if (type == AbsAdapter.TYPE_ITEM_DEFAULT || type == AbsAdapter.TYPE_ITEM_HEADER) {
            throw new IllegalArgumentException("type not valid,type is " + type);
        }
        return true;
    }

}
