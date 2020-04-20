package com.zhuchao.android.tianpu.data.json.regoem;

/**
 * Created by Oracle on 2017/12/13.
 */

public class RegOemCls {

    private int code;
    private Data data;
    private Client client;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RegOemCls:");
        if (data != null && data.getApp() != null) {
            App app = data.getApp();
            stringBuilder
                    .append("\ncontact:[ ").append(app.getContact()).append(" ]")
                    .append("\nnotice:[ ").append(app.getNotice()).append(" ]")
                    .append("\nnotice_time:[ ").append(app.getNotice_time()).append(" ]")
                    .append("\nupgrade_version:[ ").append(app.getUpgrade_version()).append(" ]")
                    .append("\nupgrade_url:[ ").append(app.getUpgrade_url()).append(" ]")
                    .append("\nupgrade_notes:[ ").append(app.getUpgrade_notes()).append(" ]")
                    .append("\nextension:[ ").append(app.getExtension()).append(" ]")
                    .append("\nreference_add:[ ").append(app.getReference_add()).append(" ]")
                    .append("\nregister_add:[ ").append(app.getRegister_add()).append(" ]");
        }
        if (client != null) {
            stringBuilder
                    .append("\nclient_id:[ ").append(String.valueOf(client.getClient_id())).append(" ]")
                    .append("\nexpire_time:[ ").append(String.valueOf(client.getExpire_time())).append(" ]")
                    .append("\npassword:[ ").append(String.valueOf(client.getPassword())).append(" ]")
                    .append("\ntoken:[ ").append(client.getToken()).append(" ]");
        }
        return stringBuilder.toString();
    }
}
