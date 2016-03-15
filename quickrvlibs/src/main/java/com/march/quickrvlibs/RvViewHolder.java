package com.march.quickrvlibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.march.quickrvlibs.RvQuick.RvQuick;
import com.march.quickrvlibs.inter.OnRecyclerItemClickListener;
import com.march.quickrvlibs.inter.OnRecyclerItemLongClickListener;

import java.lang.reflect.Field;


/**
 * BBPP     com.babypat.adapter
 * Created by 陈栋 on 15/12/28.
 * 功能:
 */
public class RvViewHolder extends RecyclerView.ViewHolder {

    protected OnRecyclerItemClickListener<RvViewHolder> clickListener;
    protected OnRecyclerItemLongClickListener<RvViewHolder> longClickListenter;

    private SparseArray<View> cacheViews;
    private View itemView;


    public RvViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        cacheViews = new SparseArray<>(5);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), RvViewHolder.this);
                }
            }
        });


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListenter != null) {
                    longClickListenter.onItemLongClick(getAdapterPosition(), RvViewHolder.this);
                }
                return true;
            }
        });
    }

    public View getParentView() {
        return itemView;
    }


    /**
     * 使用资源id找到view
     *
     * @param resId 资源id
     * @param <T>   泛型,View的子类
     * @return 返回泛型类
     */
    public <T extends View> T getView(int resId) {
        T v = (T) cacheViews.get(resId);
        if (v == null) {
            v = (T) itemView.findViewById(resId);
            if (v != null) {
                cacheViews.put(resId, v);
            }
        }
        return v;
    }

    /**
     * 使用类反射找到字符串id代表的view
     *
     * @param idName String类型ID
     * @return 返回
     */
    public View getView(String idName) {
        View view = null;
        if (idName != null) {
            Class<R.id> idClass = R.id.class;
            try {
                Field field = idClass.getDeclaredField(idName);
                field.setAccessible(true);
                int id = field.getInt(idClass);
                view = getView(id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return view;
    }


    public void setOnItemClickListener(OnRecyclerItemClickListener<RvViewHolder> listener) {
        if (listener != null) {
            this.clickListener = listener;
        }
    }

    public void setOnItemLongClickListener(OnRecyclerItemLongClickListener<RvViewHolder> longClickListener) {
        if (longClickListener != null) {
            this.longClickListenter = longClickListener;
        }
    }


    /**
     * 设置是否可见
     *
     * @param resId    资源ID
     * @param visiable visiable
     * @return this
     */
    public RvViewHolder setVisibility(int resId, int visiable) {
        getView(resId).setVisibility(visiable);
        return this;
    }


    /**
     * 为checkbox设置是否选中
     *
     * @param resId     资源id
     * @param isChecked 是否选中
     * @return VH
     */
    public RvViewHolder setChecked(int resId, boolean isChecked) {
        ((CheckBox) getView(resId)).setChecked(isChecked);
        return this;
    }


    /**
     * 设置背景
     *
     * @param resId 资源id
     * @param bgRes 背景id
     * @return this
     */
    public RvViewHolder setBg(int resId, int bgRes) {
        getView(resId).setBackgroundResource(bgRes);
        return this;
    }

    /**
     * 为textview设置文本
     *
     * @param resId 控件资源id
     * @param txt   设置的文本
     * @return VH
     */
    public RvViewHolder setText(int resId, String txt) {
        ((TextView) getView(resId)).setText(txt);
        return this;
    }

    public RvViewHolder setText(int resId, SpannableString txt) {
        ((TextView) getView(resId)).setText(txt);
        return this;
    }

    /***********imageview*****************************/
    /**
     * 为ImageView使用图片资源id设置图片
     *
     * @param resId    控件资源id
     * @param imgResId 图片资源id
     * @return VH
     */
    public RvViewHolder setImg(int resId, int imgResId) {
        ((ImageView) getView(resId)).setImageResource(imgResId);
        return this;
    }

    /**
     * 为ImageView使用位图设置图片
     *
     * @param resId 控件资源id
     * @param bit   图片资源位图
     * @return VH
     */
    public RvViewHolder setImg(int resId, Bitmap bit) {
        ((ImageView) getView(resId)).setImageBitmap(bit);
        return this;
    }

    public RvViewHolder setImg(Context context, int resId, String url) {
        if (!"".equals(url) && url != null) {
            RvQuick.get().load(context, url, (ImageView) getView(resId));
        }
        return this;
    }

    public RvViewHolder setLis(int resId, View.OnClickListener listener, Object tag) {
        getView(resId).setTag(R.string.rvquick_key, tag);
        getView(resId).setOnClickListener(listener);
        return this;
    }

    public RvViewHolder setLis(int resId, View.OnClickListener listener) {
        getView(resId).setOnClickListener(listener);
        return this;
    }

    public RvViewHolder setTag(int resId, Object tag) {
        getView(resId).setTag(R.string.rvquick_key, tag);
        return this;
    }


    public <T> T getTag(int resId) {
        return (T) getView(resId).getTag(R.string.rvquick_key);
    }
}
