package com.march.quickrvlibs.inter;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by march on 16/7/4.
 *
 */
public interface ILoadImage {
    void loadImg(Context context, String url, int w, int h, ImageView view);
}
