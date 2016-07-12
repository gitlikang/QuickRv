package com.march.quickrvlibs;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.helper.RvConvertor;
import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.Collections;
import java.util.List;

/**
 * Created by march on 16/7/2.
 * RvQuickAdapter的简化版本
 */
public abstract class SimpleRvAdapter <D> extends RecyclerView.Adapter<RvViewHolder>  {

    //基本数据适配功能
    protected List<D> datas;
    protected LayoutInflater mLayoutInflater;
    protected Context context;
    protected int resId;
    private OnItemClickListener<RvViewHolder> mClickLis;
    private OnItemLongClickListener<RvViewHolder> mLongClickLis;
    private int adapterId;//由于可以更方便的使用匿名内部类构建Adapter,无法使用instant_of来区分适配器类型,使用此标志来判断当前adapter的类型


    //添加Header,Footer功能
    private View mHeaderView;
    private View mFooterView;
    private int TYPE_HEADER = -1;
    private int TYPE_FOOTER = -2;
    private boolean isStaggeredGridLayoutManager = false;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;
    
    //加载更多模块
    private LoadMoreModule mLoadMoreMoudle;
    
    /**
     * 单类型适配
     *
     * @param context context
     * @param datas      数据源
     * @param res     layout资源
     */
    public SimpleRvAdapter(Context context, List<D> datas, int res) {
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.resId = res;
    }

    public SimpleRvAdapter(Context context, D[] datas, int res) {
        
        this.datas = RvConvertor.convert2List(datas);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.resId = res;
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
            holder = new RvViewHolder(getInflateView(resId, parent));
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
        if(mLoadMoreMoudle!=null){

            mLoadMoreMoudle.initLoadMore(recyclerView,this);
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


    public void addLoadMoreModule(int preLoadNum,
                                  LoadMoreModule.OnLoadMoreListener loadMoreListener){
        this.mLoadMoreMoudle = new LoadMoreModule();
        mLoadMoreMoudle.setLoadMore(preLoadNum,loadMoreListener);
    }

    public void finishLoad(){
        mLoadMoreMoudle.finishLoad();
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

    private int getOriItemViewType(int pos) {
        return 0;
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

    public void addHeaderOrFooter(int mHeaderRes, int mFooterRes,RecyclerView recyclerView) {
        this.mHeaderView = getInflateView(mHeaderRes, recyclerView);
        this.mFooterView = getInflateView(mFooterRes, recyclerView);
    }

    public int getHeaderCount() {
        return isHasHeader() ? 1 : 0;
    }

    private boolean isHasHeader() {
        return mHeaderView != null && isHeaderEnable;
    }

    private boolean isHasFooter() {
        return mFooterView != null && isFooterEnable;
    }


    /**
     * 设置adapterId标示
     *
     * @param adapterId adapter标示
     */
    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    public int getAdapterId() {
        return adapterId;
    }

    /**
     * 使用标记判断,是否是该adaper
     *
     * @param adapter adapter
     * @return boolean
     */
    public boolean isThisAdapter(SimpleRvAdapter adapter) {
        if (adapter == null) {
            return false;
        } else if (adapter.getAdapterId() == adapterId) {
            return true;
        }
        return false;
    }


    public void setOnItemClickListener(OnItemClickListener<RvViewHolder> listener) {
        if (listener != null) {
            this.mClickLis = listener;
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<RvViewHolder> mLongClickLis) {
        if (mLongClickLis != null) {
            this.mLongClickLis = mLongClickLis;
        }
    }

    private View getInflateView(int resId, ViewGroup parent) {
        if (resId <= 0)
            return null;
        return mLayoutInflater.inflate(resId, parent, false);
    }


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
