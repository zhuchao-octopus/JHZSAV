package com.zhuchao.android.tianpu.data.json.regoem;

import java.util.List;

/**
 * Created by ZTZ on 2018/3/21.
 */

public class Recommend3Bean {


    private int status;
    private String msg;

    private List<DataBean> data;


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


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean {
        private String cy_advertisement_id;
        private String cy_advertisement_name;
        private Object cy_advertisement_videoAddress;
        private String cy_advertisement_updateTime;
        private String cy_advertisement_category;
        private String cy_advertisement_imgAddress;
        private Object cy_advertisement_localAdAdd;
        private String cy_advertisement_localImgAdd;
        private Object cy_advertisement_attribute;
        private String cy_advertisement_stuts;
        private Object cy_advertisement_info;
        private String cy_advertisement_place;
        private String cy_advertisement_brandId;
        private String cy_advertisement_creater;
        private String advLink;

        public String getAdvLink() {
            return advLink;
        }

        public void setAdvLink(String advLink) {
            this.advLink = advLink;
        }

        public String getCy_advertisement_id() {
            return cy_advertisement_id;
        }

        public void setCy_advertisement_id(String cy_advertisement_id) {
            this.cy_advertisement_id = cy_advertisement_id;
        }

        public String getCy_advertisement_name() {
            return cy_advertisement_name;
        }

        public void setCy_advertisement_name(String cy_advertisement_name) {
            this.cy_advertisement_name = cy_advertisement_name;
        }

        public Object getCy_advertisement_videoAddress() {
            return cy_advertisement_videoAddress;
        }

        public void setCy_advertisement_videoAddress(Object cy_advertisement_videoAddress) {
            this.cy_advertisement_videoAddress = cy_advertisement_videoAddress;
        }

        public String getCy_advertisement_updateTime() {
            return cy_advertisement_updateTime;
        }

        public void setCy_advertisement_updateTime(String cy_advertisement_updateTime) {
            this.cy_advertisement_updateTime = cy_advertisement_updateTime;
        }

        public String getCy_advertisement_category() {
            return cy_advertisement_category;
        }

        public void setCy_advertisement_category(String cy_advertisement_category) {
            this.cy_advertisement_category = cy_advertisement_category;
        }

        public String getCy_advertisement_imgAddress() {
            return cy_advertisement_imgAddress;
        }

        public void setCy_advertisement_imgAddress(String cy_advertisement_imgAddress) {
            this.cy_advertisement_imgAddress = cy_advertisement_imgAddress;
        }

        public Object getCy_advertisement_localAdAdd() {
            return cy_advertisement_localAdAdd;
        }

        public void setCy_advertisement_localAdAdd(Object cy_advertisement_localAdAdd) {
            this.cy_advertisement_localAdAdd = cy_advertisement_localAdAdd;
        }

        public String getCy_advertisement_localImgAdd() {
            return cy_advertisement_localImgAdd;
        }

        public void setCy_advertisement_localImgAdd(String cy_advertisement_localImgAdd) {
            this.cy_advertisement_localImgAdd = cy_advertisement_localImgAdd;
        }

        public Object getCy_advertisement_attribute() {
            return cy_advertisement_attribute;
        }

        public void setCy_advertisement_attribute(Object cy_advertisement_attribute) {
            this.cy_advertisement_attribute = cy_advertisement_attribute;
        }

        public String getCy_advertisement_stuts() {
            return cy_advertisement_stuts;
        }

        public void setCy_advertisement_stuts(String cy_advertisement_stuts) {
            this.cy_advertisement_stuts = cy_advertisement_stuts;
        }

        public Object getCy_advertisement_info() {
            return cy_advertisement_info;
        }

        public void setCy_advertisement_info(Object cy_advertisement_info) {
            this.cy_advertisement_info = cy_advertisement_info;
        }

        public String getCy_advertisement_place() {
            return cy_advertisement_place;
        }

        public void setCy_advertisement_place(String cy_advertisement_place) {
            this.cy_advertisement_place = cy_advertisement_place;
        }

        public String getCy_advertisement_brandId() {
            return cy_advertisement_brandId;
        }

        public void setCy_advertisement_brandId(String cy_advertisement_brandId) {
            this.cy_advertisement_brandId = cy_advertisement_brandId;
        }

        public String getCy_advertisement_creater() {
            return cy_advertisement_creater;
        }

        public void setCy_advertisement_creater(String cy_advertisement_creater) {
            this.cy_advertisement_creater = cy_advertisement_creater;
        }
    }
}
