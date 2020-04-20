package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.R;

/**
 * 菜单键的弹窗
 * Created by Oracle on 2017/12/2.
 */

public class Mac_Dialog extends Dialog  {

    private Dialog dialog;
    private String types = "";
    private TextView tv;
    private View view;

    public Mac_Dialog(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.mac_dialog, null);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tv = view.findViewById(R.id.tv_middles);
    }



    public void show() {
        dialog.getWindow().setContentView(view);
//        dialog.getWindow().setLayout(470, 255);
        if (!"".equals(types)) {
            tv.setText("MAC:"+types);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    public void adjustVolume(boolean fromActivity, String type) {
        if (mListener != null && fromActivity != true) {
            mListener.onVolumeAdjust(type);
        }
        types = type;
        show();
    }

    private void close() {
        mHandler.removeCallbacks(mClose);
        mHandler.postDelayed(mClose, 2000);
    }


    private Handler mHandler = new Handler();
    private Runnable mClose = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };


    private VolumeAdjustListener mListener;

    public void setVolumeAdjustListener(VolumeAdjustListener listener) {
        mListener = listener;
    }

    public void setVolumeAdjustListener(MainActivity mainActivity) {
    }

    public static interface VolumeAdjustListener {
        public abstract void onVolumeAdjust(String volume);
    }

}


