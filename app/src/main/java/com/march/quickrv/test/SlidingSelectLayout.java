package com.march.quickrv.test;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Project  : SlidingCheck
 * Package  : com.march.slidingcheck
 * CreateAt : 16/9/6
 * Describe : 仿扣扣滑动选中照片
 *
 * @author chendong
 */
public class SlidingSelectLayout extends FrameLayout {

    public SlidingSelectLayout(Context context) {
        this(context, null);
    }

    public SlidingSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int size = (int) (widthPixels / (itemSpanCount * 1.0f));
        xTouchSlop = yTouchSlop = size * 0.25f * TOUCH_SLOP_RATE;
        scroll_dist = widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
    }

    private static final float TOUCH_SLOP_RATE = 0.25f;
    // 初始化值
    private static final int INVALID_PARAM = -1;
    // 滑动选中监听
    private OnSlidingSelectListener onSlidingSelectListener;

    private int offsetTop = 0;
    // 横轴滑动阈值，超过阈值表示触发横轴滑动
    private float xTouchSlop;
    // 纵轴滑动阈值，超过阈值表示触发纵轴滑动
    private float yTouchSlop;
    // 横向的item数量
    private int itemSpanCount = INVALID_PARAM;
    // 内部的rv
    private RecyclerView mTargetRv;
    private GridLayoutManager mLayoutManager;

    // down 事件初始值
    private float mInitialDownX;
    // down 事件初始值
    private float mInitialDownY;
    // 是否正在滑动
    private boolean isBeingSlide;

    private int tagPosKey;
    private int tagDataKey;

    private int preViewPos = INVALID_PARAM;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled())
            return super.onInterceptTouchEvent(ev);
        ensureTarget();
        ensureLayoutManager();
        if (!isReadyToIntercept())
            return super.onInterceptTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // init
                mInitialDownX = ev.getX();
                mInitialDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                // stop
                isBeingSlide = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // handle
                float xDiff = Math.abs(ev.getX() - mInitialDownX);
                float yDiff = Math.abs(ev.getY() - mInitialDownY);
                if (yDiff < xTouchSlop && xDiff > yTouchSlop) {
                    isBeingSlide = true;
                }
                break;
        }
        return isBeingSlide;
    }


    private float generateX(float x) {
        return x;
    }

    private float generateY(float y) {
        return y - offsetTop;
    }


    public void setTargetRv(RecyclerView mTargetRv) {
        this.mTargetRv = mTargetRv;
    }

    private int height;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // re init
                isBeingSlide = false;
                preViewPos = INVALID_PARAM;
                mHandler.removeMessages(MSG_SCROLL);
                break;
            case MotionEvent.ACTION_MOVE:
                int diff = (int) (height - ev.getY());
                Log.e("chendong", height + " " + ev.getY() + "  diff = " + diff + " " + yTouchSlop);

                if (diff < 300) {
                    mHandler.sendEmptyMessage(MSG_SCROLL);
                }else {
                    mHandler.removeMessages(MSG_SCROLL);
                }
                publishSlidingCheck(ev);
                break;
        }
        return isBeingSlide;
    }


    public void ensureLayoutManager() {
        if (mTargetRv == null || itemSpanCount != INVALID_PARAM)
            return;
        RecyclerView.LayoutManager lm = mTargetRv.getLayoutManager();
        if (lm == null)
            return;
        if (lm instanceof GridLayoutManager) {
            mLayoutManager = (GridLayoutManager) lm;
            GridLayoutManager glm = (GridLayoutManager) lm;
            itemSpanCount = glm.getSpanCount();
        } else {
            itemSpanCount = 4;
        }
        int size = (int) (getResources().getDisplayMetrics().widthPixels / (itemSpanCount * 1.0f));
        xTouchSlop = yTouchSlop = size * TOUCH_SLOP_RATE;
    }

    private void publishSlidingCheck(MotionEvent event) {
        float x = generateX(event.getX());
        float y = generateY(event.getY());
        View childViewUnder = mTargetRv.findChildViewUnder(x, y);
        if (onSlidingSelectListener != null && childViewUnder != null) {
            int pos = getPos(childViewUnder);
            Object data = getData(childViewUnder);
            if (pos != INVALID_PARAM && preViewPos != pos && data != null) {
                try {
                    onSlidingSelectListener.onSlidingSelect(pos, childViewUnder, data);
                    if (pos == mLayoutManager.findLastVisibleItemPosition()) {
                        mHandler.sendEmptyMessage(MSG_SCROLL);
                    }
                    preViewPos = pos;
                } catch (ClassCastException e) {
                    Log.e("SlidingSelect", "ClassCastException:填写的范型有误，无法转换");
                }
            }
        }
    }

    private static final int MSG_SCROLL = 0x12;
    private int scroll_dist;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mTargetRv.smoothScrollBy(0, scroll_dist);
            mHandler.sendEmptyMessage(MSG_SCROLL);
            return false;
        }
    });

    public void setTagKey(int tagPosKey, int tagDataKey) {
        this.tagPosKey = tagPosKey;
        this.tagDataKey = tagDataKey;
    }

    public void markView(View parentView, int pos, Object data) {
        parentView.setTag(tagPosKey, pos);
        parentView.setTag(tagDataKey, data);
    }

    public int getPos(View parentView) {
        int pos = INVALID_PARAM;
        Object tag = parentView.getTag(tagPosKey);
        if (tag != null)
            pos = (int) tag;
        return pos;
    }

    public Object getData(View parentView) {
        return parentView.getTag(tagDataKey);
    }

    /**
     * 是否可以开始拦截处理事件，当recyclerView数据完全ok之后开始
     *
     * @return 是否可以开始拦截处理事件
     */

    private boolean isReadyToIntercept() {
        return mTargetRv != null
                && mTargetRv.getAdapter() != null
                && itemSpanCount != INVALID_PARAM;
    }

    /**
     * 获取RecyclerView
     */
    private void ensureTarget() {
        if (mTargetRv != null)
            return;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RecyclerView) {
                mTargetRv = (RecyclerView) childAt;
                return;
            }
        }
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public <D> void setOnSlidingSelectListener(OnSlidingSelectListener<D> onSlidingCheckListener) {
        this.onSlidingSelectListener = onSlidingCheckListener;
    }

    public interface OnSlidingSelectListener<D> {
        void onSlidingSelect(int pos, View parentView, D data);
    }
}
