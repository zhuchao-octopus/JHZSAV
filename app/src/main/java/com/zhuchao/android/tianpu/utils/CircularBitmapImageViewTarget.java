package com.zhuchao.android.tianpu.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by Oracle on 2017/12/30.
 */

public class CircularBitmapImageViewTarget extends BitmapImageViewTarget {

    public CircularBitmapImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        super.setResource(resource);
    }
}
