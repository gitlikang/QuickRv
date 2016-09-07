package com.march.quickrvlibs.helper;

import com.march.quickrvlibs.model.RvQuickModel;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickAdapter     com.march.adapterlibs.helper
 * Created by 陈栋 on 16/4/8.
 * 功能:
 */
public class RvConverter {

    /**
     *  将Integer,String等类型的list转换为可识别类型
     * @param list 需要转换的list
     * @param <T> 范型
     * @return 列表
     */
    public static <T> List<RvQuickModel> convert(List<T> list) {
        List<RvQuickModel> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(new RvQuickModel(t));
        }
        return quicks;
    }
    public static <T> List<RvQuickModel> convert(List<T> list,int type) {
        List<RvQuickModel> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(new RvQuickModel(t,type));
        }
        return quicks;
    }


    /**
     * 将Integer,String等类型的数组转换为可识别类型
     * @param list 需要转换的list
     * @param <T> 范型
     * @return 列表
     */
    public static <T> List<RvQuickModel> convert(T[] list) {
        List<RvQuickModel> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(new RvQuickModel(t));
        }
        return quicks;
    }
}
