package com.zhuchao.android.tianpu.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhuchao.android.tianpu.R;

/**
 * Created by Oracle on 2017/12/30.
 */

public class GlideMgr {

    public static RequestOptions addGlideOptions = new RequestOptions().centerCrop().error(R.drawable.pulldown_add).placeholder(R.drawable.pulldown_add);

    public static RequestOptions normalGlideOptions = new RequestOptions().centerCrop();

    public static void loadAddDrawableImg(Context context, Drawable drawable, ImageView imageView) {
        Glide.with(context).load(drawable).apply(addGlideOptions).into(imageView);
    }

    public static void loadNormalDrawableImg(Context context, Drawable drawable, ImageView imageView) {
        Glide.with(context).load(drawable).apply(normalGlideOptions).into(imageView);
    }
}
