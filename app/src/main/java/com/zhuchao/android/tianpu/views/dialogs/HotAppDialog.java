package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.tianpu.R;

import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.databinding.MenuDialogBinding;
import com.zhuchao.android.tianpu.utils.ShareAdapter;

/**
 * 菜单键的弹窗
 * Created by Oracle on 2017/12/2.
 */

public class HotAppDialog extends Dialog implements View.OnClickListener,
            View.OnTouchListener {

    private static final String TAG = HotAppDialog.class.getSimpleName();
    private MenuDialogBinding binding;
    private Context context;
    private String packageName;
    private int rId;

    public HotAppDialog(@NonNull Context context) {
        this(context, 0, null, -1);
    }

    public HotAppDialog(@NonNull Context context, int themeResId, String packageName, int vId) {
        super(context, themeResId);
        this.context = context;
        this.packageName = packageName;
        this.rId = vId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.menu_dialog,
                null, false);
        setContentView(binding.getRoot());

        if (TextUtils.isEmpty(this.packageName)) {
            binding.del.setClickable(false);
            binding.remove.setClickable(false);
            binding.replace.setClickable(false);
            binding.del.setFocusable(false);
            binding.del.setFocusableInTouchMode(false);
            binding.remove.setFocusable(false);
            binding.remove.setFocusableInTouchMode(false);
            binding.replace.setFocusable(false);
            binding.replace.setFocusableInTouchMode(false);
            binding.del.setEnabled(false);
            binding.remove.setEnabled(false);
            binding.replace.setEnabled(false);
            binding.add.setOnClickListener(this);
        } else {
            binding.add.setEnabled(false);
            binding.add.setClickable(false);
            binding.add.setFocusable(false);
            binding.add.setFocusableInTouchMode(false);
            binding.del.setOnClickListener(this);
            binding.remove.setOnClickListener(this);
            binding.replace.setOnClickListener(this);
            binding.del.setOnClickListener(this);
        }
    }

    public static HotAppDialog showHotAppDialog(Activity activity, String packageName, int vId) {
        HotAppDialog hotAppDialog = new HotAppDialog(activity, R.style.MenuDialog,
                packageName, vId);
        hotAppDialog.show();
        return hotAppDialog;
    }

    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        handleClick(v);
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        packageName = null;
    }

    private void handleClick(View v) {
        switch (v.getId()) {
            case R.id.add:
            case R.id.replace:
                HotAppsDialog.showHotAppsDialog(context,rId);
                break;
            case R.id.del:
                ((MainActivity) context).getmMyAppsManager().uninstall(packageName);
                break;
            case R.id.remove:
                AppInfor app = ((MainActivity) context) .getmMyAppsManager().getAppInfor(packageName);
                ((MainActivity) context) .getmMyAppsManager().delFromMyApp(app);
                ((MainActivity) context) .getmMyAppsManager().delFromMyApp(app);
                clearCache();
                //Intent intent = new Intent(AppListHandler.CLEAR_ACTION);
                //intent.setData(Uri.parse("package:www"));
                //intent.putExtra("vId", rId);
                //context.sendBroadcast(intent);
                break;
        }
        dismiss();
    }

    private void clearCache() {
        ShareAdapter.getInstance().remove(packageName);
        ShareAdapter.getInstance().remove(String.valueOf(rId));
    }
}
