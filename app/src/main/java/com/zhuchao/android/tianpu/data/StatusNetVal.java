package com.zhuchao.android.tianpu.data;

/**
 * Created by Oracle on 2018/1/13.
 */

public class StatusNetVal {
    private boolean modified;
    private Object netVal;

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public Object getNetVal() {
        return netVal;
    }

    public void setNetVal(Object netVal) {
        this.netVal = netVal;
    }
}
