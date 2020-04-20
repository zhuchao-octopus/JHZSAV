package com.zhuchao.android.tianpu.data.marquee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Oracle on 2018/1/6.
 */

public class Marquee {
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("txtSize")
    @Expose
    private String txtSize;
    @SerializedName("optional_clr")
    @Expose
    private String optionalClr;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(String txtSize) {
        this.txtSize = txtSize;
    }

    public String getOptionalClr() {
        return optionalClr;
    }

    public void setOptionalClr(String optionalClr) {
        this.optionalClr = optionalClr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
