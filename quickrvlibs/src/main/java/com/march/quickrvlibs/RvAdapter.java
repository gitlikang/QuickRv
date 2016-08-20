package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
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
    protected OnItemClickListener<RvViewHolder> mClickLis;
    protected OnItemLongClickListener<RvViewHolder> mLongClickLis;
    //由于可以更方便的使用匿名内部类构建Adapter,无法使用instant_of来区分适配器类型,使用此标志来判断当前adapter的类型
    protected int adapterId;
    protected RecyclerView mAttachRv;

    //添加Header,Footer功能
    protected View mHeaderView;
    protected View mFooterView;
    protected int TYPE_HEADER = -1;
    protected int TYPE_FOOTER = -2;
    protected boolean isStaggeredGridLayoutManager = false;
    protected boolean isHeaderEnable = true;
    protected boolean isFooterEnable = true;

    //加载更多模块
    protected LoadMoreModule mLoadMoreModule;


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


    public void setOnItemClickListener(OnItemClickListener<RvViewHolder> mClickLis) {
        this.mClickLis = mClickLis;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<RvViewHolder> mLongClickLis) {
        this.mLongClickLis = mLongClickLis;
    }


    public int getHeaderCount() {
        return isHasHeader() ? 1 : 0;
    }

    protected boolean isHasHeader() {
        return mHeaderView != null && isHeaderEnable;
    }

    protected boolean isHasFooter() {
        return mFooterView != null && isFooterEnable;
    }

    public void setFooterEnable(boolean footerEnable) {
        isFooterEnable = footerEnable;
    }

    public void setHeaderEnable(boolean headerEnable) {
        isHeaderEnable = headerEnable;
    }

    public void addHeaderOrFooter(View mHeader, View mFooter) {
        this.mHeaderView = mHeader;
        this.mFooterView = mFooter;
    }

    public void addHeaderOrFooter(int mHeaderRes, int mFooterRes, RecyclerView recyclerView) {
        this.mHeaderView = getInflateView(mHeaderRes, recyclerView);
        this.mFooterView = getInflateView(mFooterRes, recyclerView);
    }

    protected View getInflateView(int resId, ViewGroup parent) {
        if (resId <= 0)
            return null;
        return mLayoutInflater.inflate(resId, parent, false);
    }


    public void addLoadMoreModule(int preLoadNum,
                                  LoadMoreModule.OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreModule = new LoadMoreModule();
        mLoadMoreModule.setLoadMore(preLoadNum, loadMoreListener);
    }

    public void finishLoad() {
        if (mLoadMoreModule != null)
            mLoadMoreModule.finishLoad();
    }


    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (isHasFooter() && position == getItemCount() - 1) {
            bindLisAndData4Footer(holder);
        } else if (isHasHeader() && position == 0) {
            bindLisAndData4Header(holder);
        } else {
            int pos = judgePos(position);
            bindData4View(holder, datas.get(pos), pos);
        }
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
        mAttachRv = recyclerView;
        if (mLoadMoreModule != null) {
            mLoadMoreModule.initLoadMore(recyclerView, this);
        }
        //如果是GridLayoutManager
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
            return;
        }
        //如果是StaggeredGridLayoutManager,放在创建ViewHolder里面来处理
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;
        }
    }

    @Override
    public int getItemCount() {
        int pos = this.datas.size();
        if (isHasHeader())
            pos++;
        if (isHasFooter())
            pos++;
        return pos;
    }


    @Override
    public int getItemViewType(int position) {
        //如果没有header没有footer直接返回
        if (!isHasHeader() && !isHasFooter())
            return getOriItemViewType(position);

        //有header且位置0
        if (isHasHeader() && position == 0)
            return TYPE_HEADER;

        //pos超出
        if (isHasFooter() && position == getItemCount() - 1)
            return TYPE_FOOTER;

        //如果有header,下标减一个
        if (isHasHeader())
            return getOriItemViewType(position - 1);
        else
            //没有header 按照原来的
            return getOriItemViewType(position);
    }


    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder holder;
        boolean isFooter = isHasFooter() && viewType == TYPE_FOOTER;
        boolean isHeader = isHasHeader() && viewType == TYPE_HEADER;
        if (isFooter) {
            holder = new RvViewHolder(mFooterView);
        } else if (isHeader) {
            holder = new RvViewHolder(mHeaderView);
        } else {
            holder = new RvViewHolder(getInflateView(getLayout4Type(viewType), parent));
            if (mClickLis != null) {
                holder.setOnItemClickListener(mClickLis);
            }
            if (mLongClickLis != null) {
                holder.setOnItemLongClickListener(mLongClickLis);
            }
            bindListener4View(holder);
        }

        if (isStaggeredGridLayoutManager && (isFooter || isHeader)) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    new StaggeredGridLayoutManager.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            holder.getParentView().setLayoutParams(layoutParams);
        }
        return holder;
    }


    /**
     * 当是不是Header和Footer时，返回类型
     *
     * @param pos 位置
     * @return 类型
     */
    protected abstract int getOriItemViewType(int pos);

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
    public abstract void bindData4View(RvViewHolder holder, D data, int pos);

    /**
     * 绑定监听器
     *
     * @param holder ViewHolder数据持有者
     */
    public void bindListener4View(RvViewHolder holder) {
    }

    /**
     * 绑定header的数据 和  监听
     *
     * @param header header holder
     */
    public void bindLisAndData4Header(RvViewHolder header) {
    }

    /**
     * 绑定footer的数据和监听
     *
     * @param footer footer holder
     */
    public void bindLisAndData4Footer(RvViewHolder footer) {
    }
}
