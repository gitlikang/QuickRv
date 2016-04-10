package com.march.quickrvlibs.RvQuick;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.march.quickrvlibs.R;



/**
 * CdLibsTest     com.march.libs.recyclerlibs
 * Created by 陈栋 on 16/3/15.
 * 功能:
 */
public class RvQuick {
    public static QuickLoad imgLoadTool;

    public static void init(QuickLoad tool) {
        imgLoadTool = tool;
    }

    public static QuickLoad get() {
        if (imgLoadTool == null) {
            throw new IllegalStateException("if u want to load online img,u must invoke Quick.init() at the first!");
        }
        return imgLoadTool;
    }


    public static <T> T getTagOutOfAdapter(View view){
        return (T) view.getTag(R.string.rvquick_key);
    }

    public interface QuickLoad {
        void load(Context context, String url, ImageView view);
    }
}
