package com.zhuchao.android.tianpu.data.marquee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Oracle on 2018/1/6.
 */

public class Root {

    @SerializedName("marquee")
    @Expose
    private List<Marquee> marquee = null;

    public List<Marquee> getMarquee() {
        return marquee;
    }

    public void setMarquee(List<Marquee> marquee) {
        this.marquee = marquee;
    }
}
