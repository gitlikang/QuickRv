package com.march.quickrv.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.quickrv.BaseActivity;
import com.march.quickrv.R;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.TypeRvAdapter;
import com.march.quickrvlibs.inter.RvQuickInterface;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapterTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_adapter_activity);
        getSupportActionBar().setTitle("多类型适配器");
        RecyclerView mRv = getView(R.id.recyclerview);
        mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        List<TypeModel> typeModels = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            typeModels.add(new TypeModel(i));
        }
        TypeRvAdapter<TypeModel> adapter = new TypeRvAdapter<TypeModel>(mContext, typeModels) {
            @Override
            public void onBindView(RvViewHolder holder, TypeModel data, int pos, int type) {
                holder.setText(R.id.item_common_tv, "id相同可以不区分类型" + data.index);
                switch (type) {
                    case TypeModel.TYPE_OK:
                        holder.setText(R.id.item_ok_tv, "this is ok !");
                        break;
                    case TypeModel.TYPE_NO:
                        holder.setText(R.id.item_no_tv, "this is not ok !");
                        break;
                }
            }
        };
        adapter.addType(TypeModel.TYPE_OK, R.layout.type_adapter_ok)
                .addType(TypeModel.TYPE_NO, R.layout.type_adapter_no);
        mRv.setAdapter(adapter);
    }


    class TypeModel implements RvQuickInterface {

        public static final int TYPE_OK = 1;
        public static final int TYPE_NO = 2;
        int index;

        public TypeModel(int index) {
            this.index = index;
        }

        //当index是3的倍数时，为ok
        @Override
        public int getRvType() {
            return index % 3 == 0 ? TYPE_OK : TYPE_NO;
        }

    }
}
