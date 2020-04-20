package com.zhuchao.android.tianpu.data.json.regoem;

import java.util.List;

/**
 * Created by ZTZ on 2018/3/20.
 */

public class RemoveAppBean {

    /**
     * status : 0
     * msg : 需要卸载应用的包名列表
     * data : ["com.cy.mypriclound","com.tobyyaa.jine","com.example.myapplication","com.dangbeimarket","tv.tool.netspeedtest","com.moretv.android","tv.beemarket","com.cibn.tv","com.shafa.market","com.gitvdemo.video","com.example.myapplication","com.starcor.mango","com.android.settings","com.applidium.nickelodeon"]
     * data2 : null
     */

    private int status;
    private String msg;
    private Object data2;
    private List<String> data;

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

    public Object getData2() {
        return data2;
    }

    public void setData2(Object data2) {
        this.data2 = data2;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
