package com.march.quickrvlibs.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.RvAdapter;
import com.march.quickrvlibs.RvViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.module
 * CreateAt : 16/8/20
 * Describe : Header模块
 *
 * @author chendong
 */
public class HFModule {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    private View mHeaderView;
    private View mFooterView;
    private boolean isStaggeredGridLayoutManager = false;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;

    private RvAdapter mAttachAdapter;

    public HFModule(View mHeader, View mFooter) {
        this.mHeaderView = mHeader;
        this.mFooterView = mFooter;
    }

    public HFModule(Context context,int mHeaderRes, int mFooterRes, RecyclerView recyclerView) {
        mHeaderView = LayoutInflater.from(context).inflate(mHeaderRes,recyclerView,false);
        mFooterView = LayoutInflater.from(context).inflate(mFooterRes,recyclerView,false);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (!isHasFooter() && !isHasFooter())
            return;
        //如果是GridLayoutManager
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAttachAdapter.getItemViewType(position) == TYPE_HEADER
                            || mAttachAdapter.getItemViewType(position) == TYPE_FOOTER
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

    public void attachAdapter(RvAdapter adapter) {
        mAttachAdapter = adapter;
    }

    public boolean isHasHeader() {
        return mHeaderView != null && isHeaderEnable;
    }

    public boolean isHasFooter() {
        return mFooterView != null && isFooterEnable;
    }

    public void setFooterEnable(boolean footerEnable) {
        isFooterEnable = footerEnable;
    }

    public void setHeaderEnable(boolean headerEnable) {
        isHeaderEnable = headerEnable;
    }

    public RvViewHolder getHFViewHolder(int viewType) {
        RvViewHolder holder = null;
        boolean isFooter = isHasFooter() && viewType == TYPE_FOOTER;
        boolean isHeader = isHasHeader() && viewType == TYPE_HEADER;
        if (isFooter) {
            holder = new RvViewHolder(mFooterView);
        } else if (isHeader) {
            holder = new RvViewHolder(mHeaderView);
        }
        if (isStaggeredGridLayoutManager && (isFooter || isHeader)) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    new StaggeredGridLayoutManager.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            holder.getParentView().setLayoutParams(layoutParams);
        }
        return holder;
    }

}
