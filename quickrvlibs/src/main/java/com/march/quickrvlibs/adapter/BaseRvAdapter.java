package com.march.quickrvlibs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.adapter
 * CreateAt : 2016/11/8
 * Describe :
 *
 * @author chendong
 */
abstract class BaseRvAdapter<D> extends AbsAdapter<D> {

    private int adapterId;
    private int preDataCount;

    public BaseRvAdapter(Context context) {
        super(context);
    }

    public BaseRvAdapter(Context context, List<D> datas) {
        super(context, datas);
    }

    public BaseRvAdapter(Context context, D[] datas) {
        super(context, datas);
    }

    ///////////////////////////////////// adapter id /////////////////////////////////////

    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    public int getAdapterId() {
        return adapterId;
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

    public void add(int pos, D data) {
        this.dateSets.add(pos, data);
        notifyItemInserted(pos);
    }

    public void changeDataNotUpdate(List<D> data) {
        this.dateSets = data;
    }

    public void appendDataNotUpdate(List<D> data) {
        this.dateSets.addAll(data);
    }

    // 全部更新数据
    public void updateData(List<D> data) {
        this.dateSets = data;
        notifyDataSetChanged();
    }

    //  自动计算更新插入的数据
    public void updateRangeInsert(List<D> data) {
        preDataCount = this.dateSets.size() + 1;
        this.dateSets = data;
        notifyItemRangeInserted(preDataCount, this.dateSets.size() - preDataCount - 1);
    }

    // 添加数据并更新
    public void appendDataUpdateRangeInsert(List<D> data) {
        preDataCount = this.dateSets.size() + 1;
        this.dateSets.addAll(data);
        notifyItemRangeInserted(preDataCount, this.dateSets.size() - preDataCount - 1);
    }

}
