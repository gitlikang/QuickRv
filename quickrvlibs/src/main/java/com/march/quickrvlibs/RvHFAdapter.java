package com.march.quickrvlibs;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.march.quickrvlibs.inter.BaseRvAdapter;

/**
 * QuickRv     com.march.quickrvlibs
 * Created by 陈栋 on 16/4/12.
 * 功能:
 */
public class RvHFAdapter extends RecyclerView.Adapter<RvViewHolder> implements BaseRvAdapter {

    private BaseRvAdapter inAdapter;
    private View mHeaderView;
    private View mFooterView;
    private int TYPE_HEADER = -1;
    private int TYPE_FOOTER = -2;
    private boolean isStaggeredGridLayoutManager = false;

    public RvHFAdapter(BaseRvAdapter in) {
        if (!(in instanceof RvQuickAdapter))
            throw new IllegalArgumentException("u must use RvQucikAdapter");
        this.inAdapter = in;
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
            holder = inAdapter.onCreateViewHolder(parent, viewType);
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
            inAdapter.onBindViewHolder(holder, judgePos(position));
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.e("chendong", "onAttachedToRecyclerView");
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

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isStaggeredGridLayoutManager = true;
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
    public int getItemCount() {
        int pos = inAdapter.getItemCount();
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
            return inAdapter.getItemViewType(position);

        //有header且位置0
        if (isHasHeader() && position == 0)
            return TYPE_HEADER;

        //pos超出
        if (isHasFooter() && position == getItemCount() - 1)
            return TYPE_FOOTER;

        //如果有header,下标减一个
        if (isHasHeader())
            return inAdapter.getItemViewType(position - 1);
        else
            //没有header 按照原来的
            return inAdapter.getItemViewType(position);
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

    public void addHeader(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    public void addFooter(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    public void addHeader(int mHeaderViewRes) {
        this.mHeaderView = ((RvQuickAdapter) inAdapter).getInflateView(mHeaderViewRes, null);
    }

    public void addFooter(int mFooterViewRes) {
        this.mFooterView = ((RvQuickAdapter) inAdapter).getInflateView(mFooterViewRes, null);
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
}
