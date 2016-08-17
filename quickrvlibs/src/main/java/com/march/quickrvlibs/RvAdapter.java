package com.march.quickrvlibs;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.march.quickrvlibs.inter.OnItemClickListener;
import com.march.quickrvlibs.inter.OnItemLongClickListener;
import com.march.quickrvlibs.module.LoadMoreModule;

import java.util.List;

/**
 * com.march.quickrvlibs
 * Created by chendong on 16/7/19.
 * desc :
 */
public abstract class RvAdapter<D,VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public abstract void setAdapterId(int adapterId);

    public abstract int getAdapterId();

    public abstract boolean isThisAdapter(RvAdapter adapter);

    public abstract void setOnItemClickListener(OnItemClickListener<RvViewHolder> listener);

    public abstract void setOnItemLongClickListener(OnItemLongClickListener<RvViewHolder> mLongClickLis);

    public abstract void setFooterEnable(boolean footerEnable);

    public abstract void setHeaderEnable(boolean headerEnable);

    public abstract void addHeaderOrFooter(View mHeader, View mFooter);

    public abstract void addHeaderOrFooter(int mHeaderRes, int mFooterRes, RecyclerView recyclerView);

    public abstract int getHeaderCount();

    public abstract void addLoadMoreModule(int preLoadNum, LoadMoreModule.OnLoadMoreListener loadMoreListener);

    public abstract void finishLoad();

    public abstract void updateData(List<D> data);
}
