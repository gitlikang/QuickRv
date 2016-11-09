package com.march.quickrv;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Project  : QuickRv
 * Package  : com.march.quickrv
 * CreateAt : 16/9/1
 * Describe :
 *
 * @author chendong
 */
public class BaseActivity extends AppCompatActivity{

    protected <V> V getView(int id){
        return (V) findViewById(id);
    }

    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
}
