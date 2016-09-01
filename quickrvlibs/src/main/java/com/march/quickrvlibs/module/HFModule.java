package com.march.quickrvlibs.module;

import android.content.Context;
import android.support.v4.text.BidiFormatter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.RvViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.module
 * CreateAt : 16/8/20
 * Describe : Header模块
 *
 * @author chendong
 */
public class HFModule extends RvModule {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    private View mHeaderView;
    private View mFooterView;
    private boolean isStaggeredGridLayoutManager = false;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;

    public HFModule(View mHeader, View mFooter) {
        this.mHeaderView = mHeader;
        this.mFooterView = mFooter;
    }

    public HFModule(Context context, int mHeaderRes, int mFooterRes, RecyclerView recyclerView) {
        mHeaderView = LayoutInflater.from(context).inflate(mHeaderRes, recyclerView, false);
        mFooterView = LayoutInflater.from(context).inflate(mFooterRes, recyclerView, false);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (!isHasFooter() && !isHasFooter())
            return;
        //如果是StaggeredGridLayoutManager,放在创建ViewHolder里面来处理
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;
        }
    }

    public boolean isHasHeader() {
        return mHeaderView != null && isHeaderEnable;
    }

    public boolean isHasFooter() {
        return mFooterView != null && isFooterEnable;
    }

    public void setFooterEnable(boolean footerEnable) {
        isFooterEnable = footerEnable;
        mAttachAdapter.notifyDataSetChanged();
    }

    public void setHeaderEnable(boolean headerEnable) {
        isHeaderEnable = headerEnable;
        mAttachAdapter.notifyDataSetChanged();
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
            ViewGroup.LayoutParams originLp = holder.getParentView().getLayoutParams();
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    new StaggeredGridLayoutManager.LayoutParams
                            (originLp.width,
                                    originLp.height);
            layoutParams.setFullSpan(true);
            holder.getParentView().setLayoutParams(layoutParams);
        }
        return holder;
    }


    public boolean isFullSpan4GridLayout(int viewType) {
        return viewType == TYPE_HEADER || viewType == TYPE_FOOTER;
    }

}
