package com.zhuchao.android.tianpu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.utils.MyApplication;
import com.zhuchao.android.tianpu.utils.GlideMgr;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends BaseAdapter {

    private List<AppInfor> apps = new ArrayList<>();

    private int listMode = 0;

    public Context mContext;

    public AppAdapter(Context context) {
        mContext = context;
    }

    /**
     * 0 GridView
     * 1 ListView
     * */
    public void setListMode(int listMode) {
        this.listMode = listMode;
    }

    public void setApps(List<AppInfor> apps) {
            this.apps = apps;
    }

    @Override
    public int getCount() {
        return this.apps != null ? this.apps.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (this.listMode) {
                case 0:
                    convertView = LayoutInflater.from(MyApplication.ctx()).inflate(R.layout.apps_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.app_icon);
                    viewHolder.tv = convertView.findViewById(R.id.app_title);
                    break;
                case 1:
                    convertView = LayoutInflater.from(MyApplication.ctx()).inflate(R.layout.chooseactivity_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.activity_icon);
                    viewHolder.tv = convertView.findViewById(R.id.activity_title);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            //if(position<0) return null;
            //if(position >= apps.size()) return null;
            AppInfor app = apps.get(position);
            //Log.d("xiaolp","app = "+app+" viewHolder = "+viewHolder);
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), viewHolder.iv);
            viewHolder.tv.setText(app.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

}
