package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.OnClickListener;
import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnLongClickListener;
import com.march.quickrvlibs.module.HFModule;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * com.march.quickrvlibs
 * Created by chendong on 16/7/19.
 * desc :基类
 */
public abstract class RvAdapter<D>
        extends RecyclerView.Adapter<RvViewHolder> {

    //每一项的Item的header类型
    public static final int TYPE_ITEM_HEADER = 0x10;
    //默认数据类型
    public static final int TYPE_ITEM_DEFAULT = 0x11;

    //基本数据适配功能
    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;
    //监听事件
    protected OnItemClickListener mClickLis;
    protected OnClickListener<D> mChildClickLis;
    protected OnLongClickListener<D> mLongClickLis;
    //由于可以更方便的使用匿名内部类构建Adapter,无法使用instant_of来区分适配器类型,使用此标志来判断当前adapter的类型
    protected int adapterId;
    protected RecyclerView mAttachRv;
    private int preDataCount;
    //加载更多模块
    protected LoadMoreModule mLoadMoreModule;
    //header+footer
    protected HFModule mHFModule;

    public RvAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public RvAdapter(Context context, List<D> datas) {
        this(context);
        this.datas = datas;
    }

    public RvAdapter(Context context, D[] datas) {
        this(context);
        Collections.addAll(this.datas, datas);
    }


    protected void clearDataIfNotNull() {
        if (this.datas == null)
            this.datas = new ArrayList<>();
        else
            this.datas.clear();
    }

    public List<D> getDatas() {
        return datas;
    }

    /*
     adapterId部分
     */
    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    public int getAdapterId() {
        return adapterId;
    }

    public boolean isUseThisAdapter(RecyclerView rv) {
        if (rv == null) {
            throw new IllegalArgumentException("RecyclerView没有初始化,为null");
        }
        if (rv.getAdapter() instanceof RvAdapter) {
            throw new IllegalArgumentException("RecyclerView使用的Adapter不是RvAdapter或其子类");
        }
        return ((RvAdapter) rv.getAdapter()).getAdapterId() == adapterId;
    }

    // 全部更新数据
    public void updateData(List<D> data) {
        this.datas = data;
        notifyDataSetChanged();
    }

    //  自动计算更新插入的数据
    public void updateRangeInsert(List<D> data) {
        preDataCount = this.datas.size() + 1;
        this.datas = data;
        notifyItemRangeInserted(preDataCount, this.datas.size() - preDataCount - 1);
    }

    // 添加数据并更新
    public void appendDataUpdateRangeInsert(List<D> data) {
        preDataCount = this.datas.size() + 1;
        this.datas.addAll(data);
        notifyItemRangeInserted(preDataCount, this.datas.size() - preDataCount - 1);
    }


    /*
     监听事件部分
     */
    public void setOnItemClickListener(OnItemClickListener mClickLis) {
        this.mClickLis = mClickLis;
    }

    public void setOnChildClickListener(OnClickListener<D> mChildClickLis) {
        this.mChildClickLis = mChildClickLis;
    }

    public void setOnItemLongClickListener(OnLongClickListener<D> mLongClickLis) {
        this.mLongClickLis = mLongClickLis;
    }


    /*
    私有辅助方法
     */
    private View getInflateView(int resId, ViewGroup parent) {
        if (resId <= 0)
            return null;
        return mLayoutInflater.inflate(resId, parent, false);
    }

    private int judgePos(int pos) {
        if (mHFModule != null && mHFModule.isHasHeader()) {
            return pos - 1;
        } else {
            return pos;
        }
    }


    /*
    模块部分
     */
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

    /*
    绑定数据部分
     */
    @Override
    public RvViewHolder<D> onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder holder = null;
        if (mHFModule != null)
            holder = mHFModule.getHFViewHolder(viewType);
        if (holder == null) {
            holder = new <D>RvViewHolder(getInflateView(getLayout4Type(viewType), parent));
            int headerCount = mHFModule == null || !mHFModule.isHasHeader() ? 0 : 1;
            if (!dispatchClickEvent(holder, viewType))
                holder.setOnItemClickListener(headerCount, mClickLis, mChildClickLis, mLongClickLis);
        }
        return holder;
    }


    //子类是否拦截点击事件
    protected boolean dispatchClickEvent(RvViewHolder holder, int viewType) {
        return false;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (mHFModule == null) {
            int pos = judgePos(position);
            holder.getParentView().setTag(datas.get(pos));
            onBindView(holder, datas.get(pos), pos, getItemViewType(position));
            bindData4View(holder, datas.get(pos), pos);
        } else if (mHFModule.isHasFooter() && position == getItemCount() - 1) {
            onBindFooter(holder);
        } else if (mHFModule.isHasHeader() && position == 0) {
            onBindHeader(holder);
        } else {
            int pos = judgePos(position);
            holder.getParentView().setTag(datas.get(pos));
            onBindView(holder, datas.get(pos), pos, getItemViewType(position));
            bindData4View(holder, datas.get(pos), pos);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        handleAttachRecyclerView(recyclerView);
    }

    public void notifyLayoutManagerChanged() {
        mAttachRv.setAdapter(this);
    }

    private void handleAttachRecyclerView(RecyclerView recyclerView) {
        mAttachRv = recyclerView;
        if (mLoadMoreModule != null)
            mLoadMoreModule.onAttachedToRecyclerView(recyclerView);
        if (mHFModule != null)
            mHFModule.onAttachedToRecyclerView(recyclerView);

        // 针对GridLayoutManager处理
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getSpanSizeLookUp(gridLayoutManager, getItemViewType(position));
                }
            });
        }
    }

    private int getSpanSizeLookUp(GridLayoutManager glm, int viewType) {
        if ((mHFModule != null && mHFModule.isFullSpan4GridLayout(viewType))
                || isFullSpan4GridLayout(viewType)) {
            return glm.getSpanCount();
        } else {
            return 1;
        }
    }

    protected boolean isFullSpan4GridLayout(int viewType) {
        return false;
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


    @Deprecated
    public void bindData4View(RvViewHolder holder, D data, int pos) {
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
     * @param type   类型
     */
    public void onBindView(RvViewHolder holder, D data, int pos, int type) {

    }

    /**
     * 绑定header的数据 和  监听
     *
     * @param header header holder
     */
    public void onBindHeader(RvViewHolder header) {
    }

    /**
     * 绑定footer的数据和监听
     *
     * @param footer footer holder
     */
    public void onBindFooter(RvViewHolder footer) {
    }
}
