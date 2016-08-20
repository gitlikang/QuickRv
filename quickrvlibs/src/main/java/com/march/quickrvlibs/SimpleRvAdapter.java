package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.helper.RvConverter;
import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.Collections;
import java.util.List;

/**
 * Created by march on 16/7/2.
 * RvQuickAdapter的简化版本,可用于单类型适配
 */
public abstract class SimpleRvAdapter<D> extends RvAdapter<D> {

    protected int resId;

    /**
     * 单类型适配
     *
     * @param context context
     * @param datas   数据源
     * @param res     layout资源
     */
    public SimpleRvAdapter(Context context, List<D> datas, int res) {
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.resId = res;
    }

    public SimpleRvAdapter(Context context, D[] datas, int res) {
        Collections.addAll(this.datas, datas);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.resId = res;
    }

    /**
     * 当不是Header和Footer时，返回类型
     * @param pos 位置
     * @return 类型
     */
    @Override
    protected int getOriginItemType(int pos) {
        return 0;
    }

    @Override
    protected int getLayout4Type(int viewType) {
        return resId;
    }
}
