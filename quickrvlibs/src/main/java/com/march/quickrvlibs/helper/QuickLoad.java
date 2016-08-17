package com.march.quickrvlibs.helper;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by march on 16/7/4.
 */
public abstract class QuickLoad {
    public abstract void loadImg(Context context, String url, ImageView view);

    public void loadSizeImg(Context context, String url, ImageView view, int w, int h, int placeHolder) {
    }
}
