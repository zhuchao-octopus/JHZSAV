package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.adapter.AppAdapter;
import com.zhuchao.android.tianpu.databinding.ChooseactivityLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oracle on 2017/12/3.
 */

public class HotAppsDialog extends Dialog {

    private static final String TAG = HotAppsDialog.class.getSimpleName();
    private ChooseactivityLayoutBinding binding;
    private Context context;
    //private SparseArray<App> appSparseArray = null;
    private AppAdapter appAdapter;
    private int vId;
    private List<AppInfor> appInfors = new ArrayList<AppInfor>();

    public HotAppsDialog(@NonNull Context context) {
        this(context, 0, 0);
    }

    public HotAppsDialog(@NonNull Context context, int themeResId, int vId) {
        super(context, themeResId);
        this.context = context;
        this.vId = vId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chooseactivity_layout, null, false);
        setContentView(binding.getRoot());


        if (context instanceof MainActivity) {
            appInfors = ((MainActivity) context).getmMyAppsManager().getUserApps();
            loadAppData();
        }

        binding.chooseappListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.format("onItemClick %s", appInfors.get(position)));

                if (context instanceof MainActivity) {
                    AppInfor app = appInfors.get(position);
                   ((MainActivity) context) .getmMyAppsManager().addToMyApp(app);
                }

                dismiss();
            }
        });
    }

    public void loadAppData() {

        appAdapter = new AppAdapter(context);
        appAdapter.setListMode(1);
        appAdapter.setApps(appInfors);
        binding.chooseappListview.setAdapter(appAdapter);
    }


    public static HotAppsDialog showHotAppsDialog(Context context, int vId) {
        HotAppsDialog hotAppsDialog = new HotAppsDialog(context, R.style.MenuDialog, vId);
        hotAppsDialog.show();
        return hotAppsDialog;
    }
}
