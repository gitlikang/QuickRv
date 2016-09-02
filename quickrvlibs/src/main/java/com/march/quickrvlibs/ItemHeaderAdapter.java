package com.march.quickrvlibs;

import android.content.Context;

import com.march.quickrvlibs.helper.RvConverter;
import com.march.quickrvlibs.inter.ItemHeaderRule;
import com.march.quickrvlibs.inter.RvQuickInterface;
import com.march.quickrvlibs.inter.RvQuickItemHeader;
import com.march.quickrvlibs.model.RvQuickModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs
 * CreateAt : 16/8/31
 * Describe :
 *
 * @author chendong
 */
public abstract class ItemHeaderAdapter<IH extends RvQuickItemHeader, ID> extends TypeRvAdapter<RvQuickModel> {

    private ItemHeaderRule<IH, ID> mItemHeaderRule;
    private List<ID> mOriginDatas;
    private int headerLayoutId;
    private int contentLayoutId;

    //使用自动匹配插入header
    public ItemHeaderAdapter(Context context,
                             List<ID> originDatas,
                             int headerLayoutId, int contentLayoutId) {
        super(context);
        this.mOriginDatas = originDatas;
        this.headerLayoutId = headerLayoutId;
        this.contentLayoutId = contentLayoutId;
        addType(RvAdapter.TYPE_ITEM_DEFAULT, contentLayoutId);
        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public ItemHeaderAdapter(Context context, ID[] originDatas,
                             int headerLayoutId, int contentLayoutId) {
        super(context);
        List<ID> middleList = new ArrayList<>();
        Collections.addAll(middleList, originDatas);
        this.mOriginDatas = middleList;
        this.headerLayoutId = headerLayoutId;
        this.contentLayoutId = contentLayoutId;
        addType(RvAdapter.TYPE_ITEM_DEFAULT, contentLayoutId);
        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public ItemHeaderAdapter(Context context, List<ID> originDatas,
                             int headerLayoutId) {
        super(context);
        this.mOriginDatas = originDatas;
        this.headerLayoutId = headerLayoutId;
        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public ItemHeaderAdapter(Context context,
                             ID[] originDatas,
                             int headerLayoutId) {
        super(context);
        List<ID> middleList = new ArrayList<>();
        Collections.addAll(middleList, originDatas);
        this.mOriginDatas = middleList;
        this.headerLayoutId = headerLayoutId;
        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用键值插入header
    public ItemHeaderAdapter(Context context) {
        super(context);
    }


    public void addItemHeaderRule(ItemHeaderRule<IH, ID> mItemHeaderRule) {
        this.mItemHeaderRule = mItemHeaderRule;
        this.datas.addAll(secondaryPackData(mOriginDatas));
    }

    protected List<RvQuickModel> secondaryPackData(List<ID> data) {
        if (mItemHeaderRule == null)
            throw new IllegalStateException("please add item header rule first");
        mOriginDatas = data;
        List<RvQuickModel> tempList = new ArrayList<>();
        if (this.datas == null)
            this.datas = new ArrayList<>();
        ID preData;
        ID nextData;
        ID tempData;
        IH itemHeader;
        for (int i = 0; i < data.size(); i++) {
            tempData = data.get(i);
            preData = i == 0 ? null : data.get(i - 1);
            nextData = i == data.size() - 1 ? null : data.get(i + 1);
            //如果需要在当前数据之前插入header,就在插入当前数据之前构建一个header
            if (mItemHeaderRule.isNeedItemHeader(i, preData, tempData, nextData)) {
                itemHeader = mItemHeaderRule.buildItemHeader(i, preData, tempData, nextData);
                tempList.add(new RvQuickModel(itemHeader));
            }
            tempList.add(new RvQuickModel(tempData, RvAdapter.TYPE_ITEM_DEFAULT));
        }
        return tempList;
    }


    @Override
    public void updateData(List<RvQuickModel> data) {
        super.updateData(data);
        throw new IllegalArgumentException("please use updateDataAndItemHeader() to update show");
    }

    public void updateDataAndItemHeader(List<ID> data) {
        this.datas.addAll(secondaryPackData(data));
        notifyLayoutManagerChanged();
    }

    @Override
    public void onBindView(RvViewHolder holder, RvQuickModel data, int pos, int type) {
        if (type == RvAdapter.TYPE_ITEM_HEADER) {
            onBindItemHeader(holder, (IH) (data.get()), pos, type);
        } else {
            onBindContent(holder, (ID) (data.get()), pos, type);
        }
    }

    public ItemHeaderAdapter addData(IH headerData, List<ID> realData) {
        if (this.datas == null)
            this.datas = new ArrayList<>();
        this.datas.add(new RvQuickModel(headerData));
        this.datas.addAll(RvConverter.convert(realData));
        return this;
    }

    public void addHeaderLayout(int layoutId) {
        if (headerLayoutId != 0)
            throw new IllegalArgumentException("u have already set the header layout in the constructor");
        addType(RvAdapter.TYPE_ITEM_HEADER, layoutId);
    }

    protected abstract void onBindItemHeader(RvViewHolder holder, IH data, int pos, int type);

    protected abstract void onBindContent(RvViewHolder holder, ID data, int pos, int type);

    @Override
    protected boolean isFullSpan4GridLayout(int viewType) {
        return viewType == RvAdapter.TYPE_ITEM_HEADER;
    }
}
