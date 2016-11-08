package com.march.quickrvlibs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.adapter
 * CreateAt : 2016/11/8
 * Describe : adapter基类，主要负责adapterId区分，数据更新等方法
 *
 * @author chendong
 */
abstract class BaseRvAdapter<D> extends AbsAdapter<D> {

    private long adapterId;

    public BaseRvAdapter(Context context) {
        super(context);
        generateAdapterId();
    }

    public BaseRvAdapter(Context context, List<D> datas) {
        super(context, datas);
        generateAdapterId();
    }

    public BaseRvAdapter(Context context, D[] datas) {
        super(context, datas);
        generateAdapterId();
    }


    ///////////////////////////////////// adapter id /////////////////////////////////////

    private void generateAdapterId() {
        adapterId = System.currentTimeMillis();
    }

    public boolean isThisAdapter(RecyclerView rv) {
        if (rv == null) {
            throw new IllegalArgumentException("RecyclerView没有初始化,为null");
        }
        if (rv.getAdapter() instanceof AbsAdapter) {
            throw new IllegalArgumentException("RecyclerView使用的Adapter不是RvAdapter或其子类");
        }
        return ((BaseRvAdapter) rv.getAdapter()).adapterId == adapterId;
    }


    ///////////////////////////////////// 数据更新 /////////////////////////////////////

    public void insert(int pos, D data) {
        this.dateSets.add(pos, data);
        notifyItemInserted(pos);
    }

    /**
     * 全部更新数据
     *
     * @param data     数据
     * @param isUpdate 是否调用更新
     */
    public void notifyDataSetChanged(List<D> data, boolean isUpdate) {
        this.dateSets = data;
        if (isUpdate) {
            notifyDataSetChanged();
        }
    }

    /**
     * 在尾部追加数据
     *
     * @param data      数据
     * @param isAllData 是不是全部数据
     */
    public void appendTailRangeData(List<D> data, boolean isAllData) {
        int preDataCount = this.dateSets.size();
        if (isAllData)
            this.dateSets = data;
        else
            this.dateSets.addAll(data);
        notifyItemRangeInserted(preDataCount, this.dateSets.size() - preDataCount);
    }
}
