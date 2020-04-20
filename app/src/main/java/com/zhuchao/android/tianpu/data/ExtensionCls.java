package com.zhuchao.android.tianpu.data;

/**
 * Created by Oracle on 2017/12/13.
 */

public class ExtensionCls {
    public int code;
    public Data data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    class Data {
        public App app;

        public Data() {}

        public void setApp(App app) {
            this.app = app;
        }

        public App getApp() {
            return this.app;
        }

        class App {
            public int extension;
            public int reference_add;
            public int register_add;

            public App() {}

            public void setExtension(int extension) {
                this.extension = extension;
            }

            public int getExtension() {
                return this.extension;
            }

            public void setReference_add(int reference_add) {
                this.reference_add = reference_add;
            }

            public int getReference_add() {
                return this.reference_add;
            }

            public void setRegister_add(int register_add) {
                this.register_add = register_add;
            }

            public int getRegister_add() {
                return this.register_add;
            }
        }
    }

    @Override
    public String toString() {
        if (data == null || data.app == null) {
            return "ExtensionCls null";
        }
        return String.format("extension = %d reference_add = %d register_add = %d",
                data.getApp().getExtension(), data.getApp().getReference_add(),
                data.getApp().getRegister_add());
    }
}
