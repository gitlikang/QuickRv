package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
import com.march.quickrvlibs.inter.RvQuickInterface;
import com.march.quickrvlibs.model.RvAdapterConfig;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.Collections;
import java.util.List;

/**
 * Created by march on 16/6/8.
 * recyclerview快速适配
 */
public abstract class TypeRvAdapter<D extends RvQuickInterface>
        extends RvAdapter<D> {

    protected SparseArray<RvAdapterConfig> Res4Type;

    /**
     * 多类型适配,需要调用addType()方法配置参数
     *
     * @param context context
     * @param data    数据源
     */
    public TypeRvAdapter(Context context, List<D> data) {
        this.datas = data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public TypeRvAdapter(Context context, D[] data) {
        Collections.addAll(this.datas, data);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * @param type  数据的类型(如果有n种类型,那么type的值需要是0 ~ n-1)
     * @param resId 该类型对应的资源文件的id
     * @return QuickTypeAdapter
     */
    public TypeRvAdapter<D> addType(int type, int resId) {
        if (this.Res4Type == null)
            this.Res4Type = new SparseArray<>();
        this.Res4Type.put(type, new RvAdapterConfig(type, resId));
        return this;
    }

    @Override
    protected int getOriItemViewType(int pos) {
        return this.datas.get(pos).getRvType();
    }

    @Override
    protected int getLayout4Type(int viewType) {
        return Res4Type.get(viewType).getResId();
    }
}
