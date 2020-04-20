package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.R;

/**
 * 菜单键的弹窗
 * Created by Oracle on 2017/12/2.
 */

public class MusicDialog extends Dialog implements SeekBar.OnSeekBarChangeListener{

    private Dialog dialog;
    private View view;
    private SeekBar sb_music;
    private AudioManager mAudioMgr;
    private int MUSIC = AudioManager.STREAM_MUSIC;
    private int mMaxVolume;
    private int mNowVolume;
    private String types = "";
    private TextView textView;
    private ImageView imageView;

    private byte[] SetMusicVolume =    {0x02, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0E, 0x7E};//设置音乐音量  K70 //
    private byte[] SetMusicVolumeK50 = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0D, 0x7E};//设置音乐音量  K50

    private byte[] SetMicVolume =    {0x02, 0x01, 0x05, 0x00, 0x00, 0x03, 0x00, 0x04, 0x00, 0x0E, 0x7E};//设置话筒音量  K70 //
    private byte[] SetMicVolumeK50 = {0x01, 0x01, 0x05, 0x00, 0x00, 0x03, 0x00, 0x04, 0x00, 0x0D, 0x7E};//设置话筒音量  K50


    public MusicDialog(Context context) {
        super(context);
        mAudioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = 60;
        mNowVolume = mAudioMgr.getStreamVolume(MUSIC);
        view = LayoutInflater.from(context).inflate(R.layout.music_dialog, null);
        dialog = new Dialog(context,R.style.music_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        textView = view.findViewById(R.id.tv_value);
        imageView = view.findViewById(R.id.iv_mic);
        sb_music = view.findViewById(R.id.sb_music);
        sb_music.setOnSeekBarChangeListener(this);
        sb_music.setProgress(sb_music.getMax() * mNowVolume / mMaxVolume);

    }

    public void show() {
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        
        if (!"".equals(types) && types.equals("0200")) {
            if (mNowVolume == 0){
                imageView.setImageResource(R.drawable.u14);
                textView.setText(String.valueOf(mNowVolume));
                dialog.setCancelable(false);
            }else {
                imageView.setImageResource(R.drawable.u2);
                textView.setText(String.valueOf(mNowVolume));
            }
        }else if (!"".equals(types) && types.equals("0300")){
            imageView.setImageResource(R.drawable.u9);
            textView.setText(String.valueOf(mNowVolume));
        }
        else
        {
            textView.setText(String.valueOf(mNowVolume));
        }
        dialog.show();
        close();
    }
    public void setImageView(int resId)
    {
        imageView.setImageResource(resId);
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
        sb_music.setProgress(sb_music.getMax() * mNowVolume / mMaxVolume);
        try {
            mAudioMgr.adjustStreamVolume(MUSIC, direction, AudioManager.FLAG_PLAY_SOUND);
        } catch (IllegalArgumentException  e) {
            e.printStackTrace();
        }

        if (mListener != null && fromActivity != true) {
            mListener.onVolumeAdjust(mNowVolume);
        }

        types = type;
        Log.e("music","types="+types+"     mNowVolume="+mNowVolume+"/"+direction);
        show();
    }

    private void close() {
        mHandler.removeCallbacks(mClose);
        if (mNowVolume == 0 && types.equals("0200")) {
        }else {
            mHandler.postDelayed(mClose, 2000);
        }
    }


    private Handler mHandler = new Handler();
    private Runnable mClose = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

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


