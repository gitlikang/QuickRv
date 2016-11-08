package com.march.quickrvlibs.adapter;

import android.content.Context;

import com.march.quickrvlibs.helper.EasyConverter;
import com.march.quickrvlibs.inter.ISectionRule;
import com.march.quickrvlibs.inter.AbsSectionHeader;
import com.march.quickrvlibs.model.ItemModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs
 * CreateAt : 16/8/31
 * Describe :
 *
 * @author chendong
 */
public abstract class SectionRvAdapter<IH extends AbsSectionHeader, ID> extends TypeRvAdapter<ItemModel> {
    private ISectionRule<IH, ID> mItemHeaderRule;
    private List<ID> mOriginDatas;
    private int headerLayoutId;
    private int contentLayoutId;



    protected void clearDataIfNotNull() {
        if (this.dateSets == null)
            this.dateSets = new ArrayList<>();
        else
            this.dateSets.clear();
    }



    //使用自动匹配插入header
    public SectionRvAdapter(Context context,
                            List<ID> originDatas,
                            int headerLayoutId, int contentLayoutId) {
        super(context);
        this.mOriginDatas = originDatas;
        this.headerLayoutId = headerLayoutId;
        this.contentLayoutId = contentLayoutId;
        addType(AbsAdapter.TYPE_ITEM_DEFAULT, contentLayoutId);
        addType(AbsAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public SectionRvAdapter(Context context, List<ID> originDatas,
                            int headerLayoutId) {
        super(context);
        this.mOriginDatas = originDatas;
        this.headerLayoutId = headerLayoutId;
        addType(AbsAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public SectionRvAdapter(Context context,
                            LinkedHashMap<IH, List<ID>> originDatas,
                            int headerLayoutId, int contentLayoutId) {
        super(context);
        this.headerLayoutId = headerLayoutId;
        this.contentLayoutId = contentLayoutId;
        clearDataIfNotNull();
        this.dateSets.addAll(secondaryPackData(originDatas));
        addType(AbsAdapter.TYPE_ITEM_DEFAULT, contentLayoutId);
        addType(AbsAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }

    //使用自动匹配插入header
    public SectionRvAdapter(Context context,
                            LinkedHashMap<IH, List<ID>> originDatas,
                            int headerLayoutId) {
        super(context);
        this.headerLayoutId = headerLayoutId;
        clearDataIfNotNull();
        this.dateSets.addAll(secondaryPackData(originDatas));
        addType(AbsAdapter.TYPE_ITEM_HEADER, headerLayoutId);
    }


    //使用自动匹配插入header
//    public ItemHeaderAdapter(Context context, ID[] originDatas,
//                             int headerLayoutId, int contentLayoutId) {
//        super(context);
//        List<ID> middleList = new ArrayList<>();
//        Collections.addAll(middleList, originDatas);
//        this.mOriginDatas = middleList;
//        this.headerLayoutId = headerLayoutId;
//        this.contentLayoutId = contentLayoutId;
//        addType(RvAdapter.TYPE_ITEM_DEFAULT, contentLayoutId);
//        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
//    }


    //使用自动匹配插入header
//    public ItemHeaderAdapter(Context context,
//                             ID[] originDatas,
//                             int headerLayoutId) {
//        super(context);
//        List<ID> middleList = new ArrayList<>();
//        Collections.addAll(middleList, originDatas);
//        this.mOriginDatas = middleList;
//        this.headerLayoutId = headerLayoutId;
//        addType(RvAdapter.TYPE_ITEM_HEADER, headerLayoutId);
//    }

    public void addItemHeaderRule(ISectionRule<IH, ID> mItemHeaderRule) {
        this.mItemHeaderRule = mItemHeaderRule;
        clearDataIfNotNull();
        this.dateSets.addAll(secondaryPackData(mOriginDatas, false));
    }

    protected List<ItemModel> secondaryPackData(Map<IH, List<ID>> data) {
        List<ItemModel> tempList = new ArrayList<>();
        for (IH ih : data.keySet()) {
            tempList.add(new ItemModel(ih));
            tempList.addAll(EasyConverter.convert(data.get(ih), TYPE_ITEM_DEFAULT));
        }
        return tempList;
    }

    protected List<ItemModel> secondaryPackData(List<ID> data, boolean isAppend) {
        if (mItemHeaderRule == null)
            throw new IllegalStateException("please add item header rule first");
        List<ItemModel> tempList = new ArrayList<>();
        ID preData;
        ID nextData;
        ID tempData;
        IH itemHeader;
        for (int i = 0; i < data.size(); i++) {
            if (isAppend && i == 0)
                continue;
            tempData = data.get(i);
            preData = i == 0 ? null : data.get(i - 1);
            nextData = i == data.size() - 1 ? null : data.get(i + 1);
            //如果需要在当前数据之前插入header,就在插入当前数据之前构建一个header
            int middle = isAppend ? mOriginDatas.size() + i : i;
            if (mItemHeaderRule.isNeedItemHeader(middle, preData, tempData, nextData, false)) {
                itemHeader = mItemHeaderRule.buildItemHeader(middle, preData, tempData, nextData);
                if (itemHeader != null)
                    tempList.add(new ItemModel(itemHeader));
            }
            tempList.add(new ItemModel(tempData, AbsAdapter.TYPE_ITEM_DEFAULT));
        }
        return tempList;
    }


    @Override
    public void updateData(List<ItemModel> data) {
        super.updateData(data);
        throw new IllegalArgumentException("please use updateDataAndItemHeader() to update show");
    }

    public void updateDataAndItemHeader(List<ID> data) {
        mOriginDatas.clear();
        mOriginDatas.addAll(data);
        this.dateSets.clear();
        this.dateSets.addAll(secondaryPackData(data, false));
        notifyDataSetChanged();
    }

    public void updateDataAndItemHeader(Map<IH, List<ID>> map) {
        this.dateSets.clear();
        this.dateSets.addAll(secondaryPackData(map));
        notifyDataSetChanged();
    }

    public void appendDataAndUpdate(List<ID> data) {
        if (data == null || data.size() <= 0)
            return;

        int startPos = this.dateSets.size() + 1;

        List<ItemModel> tempList = new ArrayList<>();
        IH itemHeader;
        ID preOne = mOriginDatas.get(mOriginDatas.size() - 1);
        ID currentOne = data.get(0);
        ID nextOne = null;
        if (data.size() > 1)
            nextOne = data.get(1);
        int middle = mOriginDatas.size();
        // 拼接时如果需要加header,就先加一个header再打包数据
        if (mItemHeaderRule.isNeedItemHeader(middle, preOne, currentOne, nextOne, true)) {
            itemHeader = mItemHeaderRule.buildItemHeader(middle, preOne, currentOne, nextOne);
            if (itemHeader != null)
                tempList.add(new ItemModel(itemHeader));
        }
        tempList.add(new ItemModel(currentOne, AbsAdapter.TYPE_ITEM_DEFAULT));
        tempList.addAll(secondaryPackData(data, true));
        mOriginDatas.addAll(data);
        this.dateSets.addAll(tempList);
        notifyItemRangeInserted(startPos, this.dateSets.size() - startPos - 1);
    }

    public List<ID> getOriginDatas() {
        return mOriginDatas;
    }

    @Override
    public void onBindView(BaseViewHolder holder, ItemModel data, int pos, int type) {
        if (type == AbsAdapter.TYPE_ITEM_HEADER) {
            onBindItemHeader(holder, (IH) (data.get()), pos, type);
        } else {
            onBindContent(holder, (ID) (data.get()), pos, type);
        }
    }

    protected abstract void onBindItemHeader(BaseViewHolder holder, IH data, int pos, int type);

    protected abstract void onBindContent(BaseViewHolder holder, ID data, int pos, int type);

    @Override
    protected boolean isFullSpan4GridLayout(int viewType) {
        return viewType == AbsAdapter.TYPE_ITEM_HEADER;
    }
}
