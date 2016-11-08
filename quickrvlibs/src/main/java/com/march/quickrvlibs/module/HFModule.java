package com.march.quickrvlibs.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.adapter.BaseViewHolder;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.module
 * CreateAt : 16/8/20
 * Describe : Header模块
 *
 * @author chendong
 */
public class HFModule extends AbsModule {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    public static final int NO_RES = 0;

    private View headerView;
    private View footerView;
    private boolean isStaggeredGridLayoutManager = false;
    private boolean isHeaderEnable = true;
    private boolean isFooterEnable = true;


    public HFModule(Context context, int mHeaderRes, int mFooterRes, RecyclerView recyclerView) {
        if (mHeaderRes != NO_RES)
            headerView = LayoutInflater.from(context).inflate(mHeaderRes, recyclerView, false);
        if (mFooterRes != NO_RES)
            footerView = LayoutInflater.from(context).inflate(mFooterRes, recyclerView, false);
    }

    private int getSpanSizeLookUp(GridLayoutManager glm, int viewType) {
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER || mAttachAdapter.isFullSpan4GridLayout(viewType)) {
            return glm.getSpanCount();
        } else {
            return 1;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (!isHasFooter() && !isHasFooter())
            return;

        //如果是StaggeredGridLayoutManager,放在创建ViewHolder里面来处理
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;

        } else if (layoutManager instanceof GridLayoutManager) {
            // 针对GridLayoutManager处理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getSpanSizeLookUp(gridLayoutManager, mAttachAdapter.getItemViewType(position));
                }
            });
        }
    }

    public boolean isHasHeader() {
        return headerView != null && isHeaderEnable;
    }

    public boolean isHasFooter() {
        return footerView != null && isFooterEnable;
    }

    public void setFooterEnable(boolean footerEnable) {
        isFooterEnable = footerEnable;
        footerView.setVisibility(View.GONE);
    }

    public void setHeaderEnable(boolean headerEnable) {
        isHeaderEnable = headerEnable;
        headerView.setVisibility(View.GONE);
    }


    public BaseViewHolder getHFViewHolder(int viewType) {
        BaseViewHolder holder = null;
        boolean isFooter = isHasFooter() && viewType == TYPE_FOOTER;
        boolean isHeader = isHasHeader() && viewType == TYPE_HEADER;
        if (isFooter) {
            holder = new BaseViewHolder(footerView);
        } else if (isHeader) {
            holder = new BaseViewHolder(headerView);
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

    public View getFooterView() {
        return footerView;
    }

    public View getHeaderView() {
        return headerView;
    }
}
