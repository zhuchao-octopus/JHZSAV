package com.zhuchao.android.tianpu.data.json.regoem;

/**
 * Created by ZTZ on 2018/3/21.
 */

public class RecommendvideoBean {

    private int status;
    private String msg;
    private String cartoon;
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCartoon() {
        return cartoon;
    }

    public void setCartoon(String cartoon) {
        this.cartoon = cartoon;
    }

    public String getMdFive() {
        return mdFive;
    }

    public void setMdFive(String mdFive) {
        this.mdFive = mdFive;
    }

    private String mdFive;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;

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


    
    public static class DataBean {
        private int syy_special_id;
        private String syy_special_name;
        private String syy_special_type;
        private String syy_special_fileOne;
        private String syy_special_fileTwo;
        private String syy_special_light;
        private String syy_special_creater;
        private String syy_special_state;
        private String marquee;
        private int cy_brand_id;
        private int ts_color;
        private String syy_specail_MD5;
        private int cid;

        public int getSyy_special_id() {
            return syy_special_id;
        }

        public void setSyy_special_id(int syy_special_id) {
            this.syy_special_id = syy_special_id;
        }

        public String getSyy_special_name() {
            return syy_special_name;
        }

        public void setSyy_special_name(String syy_special_name) {
            this.syy_special_name = syy_special_name;
        }

        public String getSyy_special_type() {
            return syy_special_type;
        }

        public void setSyy_special_type(String syy_special_type) {
            this.syy_special_type = syy_special_type;
        }

        public String getSyy_special_fileOne() {
            return syy_special_fileOne;
        }

        public void setSyy_special_fileOne(String syy_special_fileOne) {
            this.syy_special_fileOne = syy_special_fileOne;
        }

        public String getSyy_special_fileTwo() {
            return syy_special_fileTwo;
        }

        public void setSyy_special_fileTwo(String syy_special_fileTwo) {
            this.syy_special_fileTwo = syy_special_fileTwo;
        }

        public String getSyy_special_light() {
            return syy_special_light;
        }

        public void setSyy_special_light(String syy_special_light) {
            this.syy_special_light = syy_special_light;
        }

        public String getSyy_special_creater() {
            return syy_special_creater;
        }

        public void setSyy_special_creater(String syy_special_creater) {
            this.syy_special_creater = syy_special_creater;
        }

        public String getSyy_special_state() {
            return syy_special_state;
        }

        public void setSyy_special_state(String syy_special_state) {
            this.syy_special_state = syy_special_state;
        }

        public String getMarquee() {
            return marquee;
        }

        public void setMarquee(String marquee) {
            this.marquee = marquee;
        }

        public int getCy_brand_id() {
            return cy_brand_id;
        }

        public void setCy_brand_id(int cy_brand_id) {
            this.cy_brand_id = cy_brand_id;
        }

        public int getTs_color() {
            return ts_color;
        }

        public void setTs_color(int ts_color) {
            this.ts_color = ts_color;
        }

        public String getSyy_specail_MD5() {
            return syy_specail_MD5;
        }

        public void setSyy_specail_MD5(String syy_specail_MD5) {
            this.syy_specail_MD5 = syy_specail_MD5;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
