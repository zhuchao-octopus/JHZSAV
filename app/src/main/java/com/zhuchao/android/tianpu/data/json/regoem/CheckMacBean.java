package com.zhuchao.android.tianpu.data.json.regoem;

/**
 * Created by ZTZ on 2018/3/20.
 */

public class CheckMacBean {

    /**
     * status : 1
     * msg : 该mac码不存在
     * data : null
     * data2 : null
     */

    private int status;
    private String msg;
    private Object data;
    private Object data2;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData2() {
        return data2;
    }

    public void setData2(Object data2) {
        this.data2 = data2;
    }
}
