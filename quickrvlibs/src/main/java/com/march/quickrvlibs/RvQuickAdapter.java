package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.BaseRvAdapter;
import com.march.quickrvlibs.inter.OnRecyclerItemClickListener;
import com.march.quickrvlibs.inter.OnRecyclerItemLongClickListener;
import com.march.quickrvlibs.inter.RvQuickInterface;

import java.util.Collections;
import java.util.List;


/**
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public abstract class RvQuickAdapter<D extends RvQuickInterface> extends RecyclerView.Adapter<RvViewHolder> implements BaseRvAdapter{

    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;
    protected SparseArray<RvAdapterConfig> Res4Type;
    private OnRecyclerItemClickListener<RvViewHolder> clickListener;
    private OnRecyclerItemLongClickListener<RvViewHolder> longClickListenter;
    private int adapterId;//使用此标志来判断当前adapter的类型

    public int getAdapterId() {
        return adapterId;
    }

    /**
     * 设置adapterId标示
     *
     * @param adapterId adapter标示
     */
    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    /**
     * 使用标记判断,是否是该adaper
     *
     * @param adapter adapter
     * @return boolean
     */
    public boolean isThisAdapter(RvQuickAdapter adapter) {
        if (adapter == null) {
            return false;
        } else if (adapter.getAdapterId() == adapterId) {
            return true;
        }
        return false;
    }

    /**
     * 多类型适配,需要调用addType()方法配置参数
     *
     * @param context context
     * @param datas   数据源
     */
    public RvQuickAdapter(Context context, List<D> datas) {
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public RvQuickAdapter(Context context, D[] ds) {
        Collections.addAll(datas, ds);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    /**
     * 单类型适配
     *
     * @param context context
     * @param datas   数据源
     * @param res     layout资源
     */
    public RvQuickAdapter(Context context, List<D> datas, int res) {
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.Res4Type = new SparseArray<>();
        Res4Type.put(0, new RvAdapterConfig(0, res));
    }

    public RvQuickAdapter(Context context, D[] ds, int res) {
        Collections.addAll(datas, ds);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.Res4Type = new SparseArray<>();
        Res4Type.put(0, new RvAdapterConfig(0, res));
    }


    public void setClickListener(OnRecyclerItemClickListener<RvViewHolder> listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    public void setLongClickListener(OnRecyclerItemLongClickListener<RvViewHolder> longClickListenter) {
        if (longClickListenter != null) {
            this.longClickListenter = longClickListenter;
        }
    }

    public View getInflateView(int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder holder = new RvViewHolder(getInflateView(Res4Type.get(viewType).getResId(), parent));
        if (clickListener != null) {
            holder.setOnItemClickListener(clickListener);
        }
        if (longClickListenter != null) {
            holder.setOnItemLongClickListener(longClickListenter);
        }
        bindListener4View(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int pos) {
        bindData4View(holder, datas.get(pos), pos, datas.get(pos).getRvType());
    }


    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getRvType();
    }


    /**
     * 绑定数据
     *
     * @param holder ViewHolder数据持有者
     * @param data   数据集
     * @param pos    数据集中的位置
     * @param type   type
     */
    public abstract void bindData4View(RvViewHolder holder, D data, int pos, int type);

    /**
     * 绑定监听器
     *
     * @param holder ViewHolder数据持有者
     * @param type   type
     */
    public void bindListener4View(RvViewHolder holder, int type) {
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * @param type  数据的类型(如果有n种类型,那么type的值需要是0 ~ n-1)
     * @param resId 该类型对应的资源文件的id
     * @return QuickTypeAdapter
     */
    public RvQuickAdapter<D> addType(int type, int resId) {
        if (this.Res4Type == null)
            this.Res4Type = new SparseArray<>();
        this.Res4Type.put(type, new RvAdapterConfig(type, resId));
        return this;
    }


    public RvViewHolder getHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType);
    }

    public void bindHolder(RvViewHolder holder, int pos) {
        onBindViewHolder(holder, pos);
    }

    public int getRvType(int pos) {
        return getItemViewType(pos);
    }

    public int getDataCount() {
        return getItemCount();
    }

}
