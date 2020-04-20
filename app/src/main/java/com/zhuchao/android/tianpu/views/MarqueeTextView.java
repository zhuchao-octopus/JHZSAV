package com.zhuchao.android.tianpu.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context) {
        super(context);
    }

    @Override

    public boolean isFocused() {
        //就是把这里返回true即可
        return true;
    }
}