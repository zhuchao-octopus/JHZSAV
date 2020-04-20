package com.zhuchao.android.tianpu.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.zhuchao.android.libfilemanager.AppsChangedCallback;
import com.zhuchao.android.libfilemanager.MyAppsManager;
import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.adapter.AppAdapter;
import com.zhuchao.android.tianpu.databinding.ActivityMyApplicationBinding;
import com.zhuchao.android.tianpu.utils.PageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oracle on 2017/12/1.
 */

public class AppsActivity extends Activity implements AppsChangedCallback {

    private static final String TAG = AppsActivity.class.getSimpleName();
    private ActivityMyApplicationBinding binding;
    private AppAdapter appAdapter;
    //private PageType pageType;
    private static List<AppInfor> MyAppInfors = new ArrayList<AppInfor>();
    private static MyAppsManager mMyAppsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_application);

        //String pageTypeStr = getIntent().getStringExtra("type");
        //pageType = TextUtils.isEmpty(pageTypeStr) ? PageType.MY_APP_TYPE : PageType.valueOf(pageTypeStr);
        appAdapter = new AppAdapter(this);
        appAdapter.setApps(MyAppInfors);
        binding.allapps.setAdapter(appAdapter);

        if (mMyAppsManager != null) {
            mMyAppsManager.setmAppsChangedCallback(this);
        }

        LoadData();

        binding.allapps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfor app = (AppInfor) appAdapter.getItem(position);
                if (app != null) {
                    String packageName = app.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        mMyAppsManager.startTheApp(packageName);
                    }
                }
            }
        });

        binding.allapps.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfor app = (AppInfor) appAdapter.getItem(position);
                if (app != null) {
                    String packageName = app.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        mMyAppsManager.uninstall(packageName);
                    }
                }
                return true;
            }
        });
    }

    public  void LoadData() {
        MyAppInfors = mMyAppsManager.getUserApps();
        appAdapter.notifyDataSetChanged();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                appAdapter.setApps(MyAppInfors);
//                binding.allapps.setAdapter(appAdapter);
//                appAdapter.notifyDataSetChanged();
//            }
//        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_TV_INPUT) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        //if (mMyAppsManager != null) {
        //    mMyAppsManager.setmAppsChangedCallback(this);
        //    LoadData();
        //}

        appAdapter.setApps(MyAppInfors);
        binding.allapps.setAdapter(appAdapter);
        super.onResume();
        handler.sendEmptyMessage(0x11);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.sendEmptyMessage(0x11);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //appAdapter = null;
        //binding = null;
    }

    public static void lunchAppsActivity(Context context, PageType pageType) {
        Intent intent = new Intent(context, AppsActivity.class);
        intent.putExtra("type", pageType.name());
        if (context instanceof MainActivity) {
            mMyAppsManager = ((MainActivity) context).getmMyAppsManager();
            //MyAppInfors = mMyAppsManager.getApps();
        }
        context.startActivity(intent);
    }

    @Override
    public void OnAppsChanged(String s, AppInfor appInfor) {
        if (s.equals(MyAppsManager.SCANING_COMPLETE_ACTION)) {
            //LoadData();
            handler.sendEmptyMessage(0x11);
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0x11) {
                LoadData();
                //Bundle bundle = msg.getData();
                //String date = bundle.getString("msg");
            }
        }
    };

}
