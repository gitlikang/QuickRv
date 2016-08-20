package com.march.quickrvlibs.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by march on 16/6/8.
 * 加载更多模块的实现
 */
public class LoadMoreModule {

    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoadingMore;
    private int preLoadNum = 0;
    private boolean isEnding = false;



    public void setLoadMore(int preLoadNum,OnLoadMoreListener loadMoreListener) {
        this.preLoadNum = preLoadNum;
        this.mLoadMoreListener = loadMoreListener;
    }

    public void initLoadMore(final RecyclerView mRecyclerView, final RecyclerView.Adapter inAdapter) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isEnding) {
                    if (null != mLoadMoreListener && !mIsLoadingMore) {
                        mIsLoadingMore = true;
                        mLoadMoreListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mLoadMoreListener && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition(mRecyclerView);
                    isEnding = lastVisiblePosition + 1 + preLoadNum >= inAdapter.getItemCount();
                }
            }
        });
    }


    /**
     * 获取最后一条展示的位置
     *
     * @return pos
     */
    private int getLastVisiblePosition(RecyclerView mRecyclerView) {
        int position;
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = manager.getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions 位置
     * @return pos
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void finishLoad() {
        this.mIsLoadingMore = false;
    }


}
