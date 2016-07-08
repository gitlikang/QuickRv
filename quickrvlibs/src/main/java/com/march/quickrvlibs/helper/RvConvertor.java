package com.march.quickrvlibs.helper;

import com.march.quickrvlibs.model.RvQuickModel;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickAdapter     com.march.adapterlibs.helper
 * Created by 陈栋 on 16/4/8.
 * 功能:
 */
public class RvConvertor {

    public static <T> List<RvQuickModel> convert(List<T> list) {
        List<RvQuickModel> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(new RvQuickModel(t));
        }
        return quicks;
    }


    public static <T> List<RvQuickModel> convert(T[] list) {
        List<RvQuickModel> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(new RvQuickModel(t));
        }
        return quicks;
    }


    public static <T> List<T> convert2List(T[] list) {
        List<T> quicks = new ArrayList<>();
        for (T t : list) {
            quicks.add(t);
        }
        return quicks;
    }


}
