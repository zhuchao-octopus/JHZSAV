package com.zhuchao.android.tianpu.data.json.regoem;

/**
 * Created by ZTZ on 2018/3/27.
 */

public class VersionBean {


    /**
     * status : 0
     * msg : 最新版本
     * data : {"cy_versions_id":25,"cy_versions_name":"JLC01","cy_versions_versions":"1.1","cy_versions_type":"","cy_versions_info":"修复了版本升级的一些问题","cy_versions_struts":"1","cy_versions_createTime":"2018-03-28 23:13:17","cy_versions_path":"http://120.76.100.26:8080/wxxAdmin/ver/2018/03/28/1522249997425487.apk","cy_versions_localPath":"/mnt/wxxAdmin/ver/2018/03/28/1522249997425487.apk","cy_versions_creater":"","cy_versions_size":"6905287","cy_versions_md5":"3191a54c183c3999ecf1e1a13934bf5f","cy_brand_id":702}
     * data2 : null
     * data3 : null
     */

    private int status;
    private String msg;
    /**
     * cy_versions_id : 25
     * cy_versions_name : JLC01
     * cy_versions_versions : 1.1
     * cy_versions_type :
     * cy_versions_info : 修复了版本升级的一些问题
     * cy_versions_struts : 1
     * cy_versions_createTime : 2018-03-28 23:13:17
     * cy_versions_path : http://120.76.100.26:8080/wxxAdmin/ver/2018/03/28/1522249997425487.apk
     * cy_versions_localPath : /mnt/wxxAdmin/ver/2018/03/28/1522249997425487.apk
     * cy_versions_creater :
     * cy_versions_size : 6905287
     * cy_versions_md5 : 3191a54c183c3999ecf1e1a13934bf5f
     * cy_brand_id : 702
     */

    private DataBean data;
    private Object data2;
    private Object data3;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getData2() {
        return data2;
    }

    public void setData2(Object data2) {
        this.data2 = data2;
    }

    public Object getData3() {
        return data3;
    }

    public void setData3(Object data3) {
        this.data3 = data3;
    }

    public static class DataBean {
        private int cy_versions_id;
        private String cy_versions_name;
        private String cy_versions_versions;
        private String cy_versions_type;
        private String cy_versions_info;
        private String cy_versions_struts;
        private String cy_versions_createTime;
        private String cy_versions_path;
        private String cy_versions_localPath;
        private String cy_versions_creater;
        private String cy_versions_size;
        private String cy_versions_md5;
        private int cy_brand_id;

        public int getCy_versions_id() {
            return cy_versions_id;
        }

        public void setCy_versions_id(int cy_versions_id) {
            this.cy_versions_id = cy_versions_id;
        }

        public String getCy_versions_name() {
            return cy_versions_name;
        }

        public void setCy_versions_name(String cy_versions_name) {
            this.cy_versions_name = cy_versions_name;
        }

        public String getCy_versions_versions() {
            return cy_versions_versions;
        }

        public void setCy_versions_versions(String cy_versions_versions) {
            this.cy_versions_versions = cy_versions_versions;
        }

        public String getCy_versions_type() {
            return cy_versions_type;
        }

        public void setCy_versions_type(String cy_versions_type) {
            this.cy_versions_type = cy_versions_type;
        }

        public String getCy_versions_info() {
            return cy_versions_info;
        }

        public void setCy_versions_info(String cy_versions_info) {
            this.cy_versions_info = cy_versions_info;
        }

        public String getCy_versions_struts() {
            return cy_versions_struts;
        }

        public void setCy_versions_struts(String cy_versions_struts) {
            this.cy_versions_struts = cy_versions_struts;
        }

        public String getCy_versions_createTime() {
            return cy_versions_createTime;
        }

        public void setCy_versions_createTime(String cy_versions_createTime) {
            this.cy_versions_createTime = cy_versions_createTime;
        }

        public String getCy_versions_path() {
            return cy_versions_path;
        }

        public void setCy_versions_path(String cy_versions_path) {
            this.cy_versions_path = cy_versions_path;
        }

        public String getCy_versions_localPath() {
            return cy_versions_localPath;
        }

        public void setCy_versions_localPath(String cy_versions_localPath) {
            this.cy_versions_localPath = cy_versions_localPath;
        }

        public String getCy_versions_creater() {
            return cy_versions_creater;
        }

        public void setCy_versions_creater(String cy_versions_creater) {
            this.cy_versions_creater = cy_versions_creater;
        }

        public String getCy_versions_size() {
            return cy_versions_size;
        }

        public void setCy_versions_size(String cy_versions_size) {
            this.cy_versions_size = cy_versions_size;
        }

        public String getCy_versions_md5() {
            return cy_versions_md5;
        }

        public void setCy_versions_md5(String cy_versions_md5) {
            this.cy_versions_md5 = cy_versions_md5;
        }

        public int getCy_brand_id() {
            return cy_brand_id;
        }

        public void setCy_brand_id(int cy_brand_id) {
            this.cy_brand_id = cy_brand_id;
        }
    }
}
