package com.march.quickrvlibs.inter;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs.inter
 * CreateAt : 16/9/2
 * Describe : 创建item header的rule
 *
 * @author chendong
 */
public interface ItemHeaderRule<IH, ID> {

    IH buildItemHeader(int currentPos,ID preData, ID currentData, ID nextData);

    boolean isNeedItemHeader(int currentPos,ID preData, ID currentData, ID nextData,boolean isCheckAppendData);
}
