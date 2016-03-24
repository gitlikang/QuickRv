package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.OnRecyclerItemClickListener;
import com.march.quickrvlibs.inter.OnRecyclerItemLongClickListener;
import com.march.quickrvlibs.inter.RvQuickInterface;

import java.util.List;


/**
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public abstract class RvQuickAdapter<D extends RvQuickInterface> extends RecyclerView.Adapter<RvViewHolder> {

    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;
    protected SparseArray<RvAdapterConfig> Res4Type;
    private OnRecyclerItemClickListener<RvViewHolder> clickListener;
    private OnRecyclerItemLongClickListener<RvViewHolder> longClickListenter;
    private View mHeaderView;
    private View mFooterView;
    private int TYPE_HEADER = -1;
    private int TYPE_FOOTER = -2;

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


    public void addHeader(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    public void addFooter(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    public void addHeader(int mHeaderViewRes) {
        this.mHeaderView = getInflateView(mHeaderViewRes,null);
    }

    public void addFooter(int mFooterViewRes) {
        this.mFooterView = getInflateView(mFooterViewRes,null);
    }

    public View getInflateView(int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder holder;
        if (isHasFooter() && viewType == TYPE_FOOTER) {
            holder = new RvFooterHolder(mFooterView);
        } else if (isHasHeader() && viewType == TYPE_HEADER) {
            holder = new RvHeaderHolder(mHeaderView);
        } else {
            holder = new RvViewHolder(getInflateView(Res4Type.get(viewType).getResId(), parent));
            if (clickListener != null) {
                holder.setOnItemClickListener(clickListener);
            }
            if (longClickListenter != null) {
                holder.setOnItemLongClickListener(longClickListenter);
            }
        }

        bindListener4View(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (isHasFooter() && position == getItemCount()-1) {
            bindLisAndData4Footer((RvFooterHolder) holder);
        } else if (isHasHeader() && position == 0) {
            bindLisAndData4Header((RvHeaderHolder) holder);
        } else {
            int pos = judgePos(position);
            bindData4View(holder, datas.get(pos), pos, datas.get(pos).getRvType());
        }

        if (holder instanceof RvHeaderHolder || holder instanceof RvFooterHolder) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            holder.getParentView().setLayoutParams(layoutParams);
        }
    }


    @Override
    public int getItemViewType(int position) {
        //如果没有header没有footer直接返回
        if (!isHasHeader() && !isHasFooter())
            return datas.get(position).getRvType();

        //有header且位置0
        if (isHasHeader() && position == 0)
            return TYPE_HEADER;

        //pos超出
        if (isHasFooter()&&position == getItemCount()-1)
            return TYPE_FOOTER;

        //如果有header,下标减一个
        if (isHasHeader())
            return datas.get(position - 1).getRvType();
        else
            //没有header 按照原来的
            return datas.get(position).getRvType();

    }

    private int judgePos(int pos) {
        if (isHasHeader()) {
            return pos - 1;
        } else {
            return pos;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER
                            ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
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

    /**
     * 绑定header的数据 和  监听
     *
     * @param holder header holder
     */
    public void bindLisAndData4Header(RvHeaderHolder holder) {

    }

    /**
     * 绑定footer的数据和监听
     *
     * @param holder footer holder
     */
    public void bindLisAndData4Footer(RvFooterHolder holder) {

    }


    @Override
    public int getItemCount() {
        int pos = datas.size();

        if (isHasHeader())
            pos++;
        if (isHasFooter())
            pos++;

        return pos;
    }

    public int getHeaderCount() {
        return isHasHeader() ? 1 : 0;
    }

    public int getDataPos(int pos) {
        return pos - getHeaderCount();
    }

    private boolean isHasHeader() {
        return mHeaderView != null;
    }

    private boolean isHasFooter() {
        return mFooterView != null;
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

}
