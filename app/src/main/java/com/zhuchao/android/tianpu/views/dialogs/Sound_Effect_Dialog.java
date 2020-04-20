package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.R;

/**
 * 菜单键的弹窗
 * Created by Oracle on 2017/12/2.
 */

public class Sound_Effect_Dialog extends Dialog  {

    private Dialog dialog;
    private String types = "";
    private int mNowVolume;
    private TextView tv;
    private View view;
    private ImageView iv;

    public Sound_Effect_Dialog(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.sound_effect_dialog, null);
        dialog = new Dialog(context,R.style.sound_effect_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tv = view.findViewById(R.id.tv_no);
        iv = view.findViewById(R.id.iv_persion);
    }



    public void show() {
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().setLayout(200, 120);
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

    public void adjustVolume(final int direction, boolean fromActivity, String type) {
        mNowVolume = direction;
        if (mListener != null && fromActivity != true) {
            mListener.onVolumeAdjust(mNowVolume);
        }
        types = type;
        show();

        close();
    }

    public void setContentValue(int rID,String strValue)
    {
        //if(tv != null)
        tv.setText(strValue);
        //tv.invalidate();
        //if(iv != null)
        iv.setImageResource(rID);

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
        public abstract void onVolumeAdjust(int volume);
    }

}


