package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
import com.march.quickrvlibs.module.HFModule;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.List;

/**
 * com.march.quickrvlibs
 * Created by chendong on 16/7/19.
 * desc :基类
 */
public abstract class RvAdapter<D>
        extends RecyclerView.Adapter<RvViewHolder> {

    //基本数据适配功能
    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;
    //监听事件
    protected OnItemClickListener<D> mClickLis;
    protected OnItemLongClickListener<D> mLongClickLis;
    //由于可以更方便的使用匿名内部类构建Adapter,无法使用instant_of来区分适配器类型,使用此标志来判断当前adapter的类型
    protected int adapterId;
    protected RecyclerView mAttachRv;

    //加载更多模块
    protected LoadMoreModule mLoadMoreModule;
    protected HFModule mHFModule;


    public void updateData(List<D> data) {
        this.datas = data;
        notifyDataSetChanged();
    }


    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    public int getAdapterId() {
        return adapterId;
    }

    public boolean isUseThisAdapter(RecyclerView rv) {
        return ((RvAdapter) rv.getAdapter()).getAdapterId() == adapterId;
    }


    public void setOnItemClickListener(OnItemClickListener<D> mClickLis) {
        this.mClickLis = mClickLis;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<D> mLongClickLis) {
        this.mLongClickLis = mLongClickLis;
    }


    public View getInflateView(int resId, ViewGroup parent) {
        if (resId <= 0)
            return null;
        return mLayoutInflater.inflate(resId, parent, false);
    }


    public HFModule getHFModule() {
        return mHFModule;
    }

    public LoadMoreModule getLoadMoreModule() {
        return mLoadMoreModule;
    }

    public void addHFModule(HFModule hfModule) {
        this.mHFModule = hfModule;
        mHFModule.onAttachAdapter(this);
    }

    public void addLoadMoreModule(LoadMoreModule loadMoreModule) {
        this.mLoadMoreModule = loadMoreModule;
        mLoadMoreModule.onAttachAdapter(this);
    }


    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder holder = null;
        if (mHFModule != null)
            holder = mHFModule.getHFViewHolder(viewType);
        if (holder == null) {

            holder = new <D>RvViewHolder(getInflateView(getLayout4Type(viewType), parent));
            int headerCount = mHFModule == null || !mHFModule.isHasHeader() ? 0 : 1;
            holder.setOnItemClickListener(headerCount, mClickLis, mLongClickLis);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (mHFModule == null) {
            int pos = judgePos(position);
            holder.getParentView().setTag(datas.get(pos));
            onBindView(holder, datas.get(pos), pos, getItemViewType(position));
        } else if (mHFModule.isHasFooter() && position == getItemCount() - 1) {
            onBindFooter(holder);
        } else if (mHFModule.isHasHeader() && position == 0) {
            bindHeader(holder);
        } else {
            int pos = judgePos(position);
            holder.getParentView().setTag(datas.get(pos));
            onBindView(holder, datas.get(pos), pos, getItemViewType(position));
        }
    }

    private int judgePos(int pos) {
        if (mHFModule != null && mHFModule.isHasHeader()) {
            return pos - 1;
        } else {
            return pos;
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachRv = recyclerView;
        if (mLoadMoreModule != null)
            mLoadMoreModule.onAttachedToRecyclerView(recyclerView);
        if (mHFModule != null)
            mHFModule.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        int pos = this.datas.size();
        if (mHFModule == null)
            return pos;
        if (mHFModule.isHasHeader())
            pos++;
        if (mHFModule.isHasFooter())
            pos++;
        return pos;
    }


    @Override
    public int getItemViewType(int position) {

        if (mHFModule == null)
            return getOriginItemType(position);

        //如果没有header没有footer直接返回
        if (!mHFModule.isHasHeader() && !mHFModule.isHasFooter())
            return getOriginItemType(position);

        //有header且位置0
        if (mHFModule.isHasHeader() && position == 0)
            return HFModule.TYPE_HEADER;

        //pos超出
        if (mHFModule.isHasFooter() && position == getItemCount() - 1)
            return HFModule.TYPE_FOOTER;

        //如果有header,下标减一个
        if (mHFModule.isHasHeader())
            return getOriginItemType(position - 1);
        else
            //没有header 按照原来的
            return getOriginItemType(position);
    }


    /**
     * 当是不是Header和Footer时，返回类型
     *
     * @param pos 位置
     * @return 类型
     */
    protected abstract int getOriginItemType(int pos);

    /**
     * @param viewType 根据类型返回不同的
     * @return 根据类型返回该类型的资源id
     */
    protected abstract int getLayout4Type(int viewType);

    /**
     * 绑定数据
     *
     * @param holder ViewHolder数据持有者
     * @param data   数据集
     * @param pos    数据集中的位置
     */
    public abstract void onBindView(RvViewHolder holder, D data, int pos, int type);

    /**
     * 绑定header的数据 和  监听
     *
     * @param header header holder
     */
    public void bindHeader(RvViewHolder header) {
    }

    /**
     * 绑定footer的数据和监听
     *
     * @param footer footer holder
     */
    public void onBindFooter(RvViewHolder footer) {
    }
}
