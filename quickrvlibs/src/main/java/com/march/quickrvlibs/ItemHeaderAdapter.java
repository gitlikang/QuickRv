package com.march.quickrvlibs;

import android.content.Context;

import com.march.quickrvlibs.helper.RvConverter;
import com.march.quickrvlibs.inter.RvQuickInterface;
import com.march.quickrvlibs.inter.RvQuickItemHeader;
import com.march.quickrvlibs.model.RvQuickModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrvlibs
 * CreateAt : 16/8/31
 * Describe :
 *
 * @author chendong
 */
public abstract class ItemHeaderAdapter<IH extends RvQuickItemHeader, ID extends RvQuickInterface> extends TypeRvAdapter<RvQuickModel> {

    public ItemHeaderAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(RvViewHolder holder, RvQuickModel data, int pos, int type) {
        if (type == RvAdapter.TYPE_ITEM_HEADER) {
            onBindItemHeader(holder, (IH) (data.get()), pos, type);
        } else {
            onBindContent(holder, (ID) (data.get()), pos, type);
        }
    }


    public void addData(IH headerData, List<ID> realData) {
        if (this.datas == null)
            this.datas = new ArrayList<>();
        this.datas.add(new RvQuickModel(headerData));
        this.datas.addAll(RvConverter.convert(realData));
    }

    public void addHeaderLayout(int layoutId) {
        addType(RvAdapter.TYPE_ITEM_HEADER, layoutId);
    }

    protected abstract void onBindItemHeader(RvViewHolder holder, IH data, int pos, int type);

    protected abstract void onBindContent(RvViewHolder holder, ID data, int pos, int type);

    @Override
    protected boolean isFullSpan4GridLayout(int viewType) {
        return viewType == RvAdapter.TYPE_ITEM_HEADER;
    }
}
