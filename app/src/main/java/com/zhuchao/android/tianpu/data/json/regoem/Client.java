package com.zhuchao.android.tianpu.data.json.regoem;

/**
 * Created by Oracle on 2017/12/13.
 */

public class Client {

    private int client_id;
    private int password;
    private int expire_time;
    private String token;

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
