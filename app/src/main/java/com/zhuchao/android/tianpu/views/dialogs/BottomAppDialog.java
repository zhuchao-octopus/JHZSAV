package com.zhuchao.android.tianpu.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.activities.MainActivity;
import com.zhuchao.android.tianpu.bridge.SelEffectBridge;

import com.zhuchao.android.tianpu.databinding.ActivityPullBinding;
import com.zhuchao.android.tianpu.utils.MyApplication;

public class BottomAppDialog extends Dialog implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = BottomAppDialog.class.getSimpleName();
    private ActivityPullBinding binding;
    private Context context;
    private SelEffectBridge selEffectBridge;
    private RequestOptions glideOptions;

    public BottomAppDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_pull,
                null, false);
        setContentView(binding.getRoot());

        glideOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.pulldown_add)
                .placeholder(R.drawable.pulldown_add);

        //((MainActivity) context).scanBottom();

        selEffectBridge = (SelEffectBridge) binding.pullMianup.getEffectBridge();
        selEffectBridge.setUpRectResource(R.drawable.home_sel_btn0);

        binding.pullTag9301.setOnClickListener(this);
        binding.pullTag9302.setOnClickListener(this);
        binding.pullTag9303.setOnClickListener(this);
        binding.pullTag9304.setOnClickListener(this);
        binding.pullTag9305.setOnClickListener(this);
        binding.pullTag9306.setOnClickListener(this);
        binding.pullTag9307.setOnClickListener(this);
        binding.pullTag9308.setOnClickListener(this);
        binding.pullTag9309.setOnClickListener(this);
        binding.pullTag9310.setOnClickListener(this);
        binding.pullTag9311.setOnClickListener(this);
        binding.pullTag9312.setOnClickListener(this);
        binding.pullTag9313.setOnClickListener(this);
        binding.pullTag9314.setOnClickListener(this);
        binding.pullTag9315.setOnClickListener(this);
        binding.pullTag9316.setOnClickListener(this);

        binding.pullTag9301.setOnKeyListener(this);
        binding.pullTag9302.setOnKeyListener(this);
        binding.pullTag9303.setOnKeyListener(this);
        binding.pullTag9304.setOnKeyListener(this);
        binding.pullTag9305.setOnKeyListener(this);
        binding.pullTag9306.setOnKeyListener(this);
        binding.pullTag9307.setOnKeyListener(this);
        binding.pullTag9308.setOnKeyListener(this);
        binding.pullTag9309.setOnKeyListener(this);
        binding.pullTag9310.setOnKeyListener(this);
        binding.pullTag9311.setOnKeyListener(this);
        binding.pullTag9312.setOnKeyListener(this);
        binding.pullTag9313.setOnKeyListener(this);
        binding.pullTag9314.setOnKeyListener(this);
        binding.pullTag9315.setOnKeyListener(this);
        binding.pullTag9316.setOnKeyListener(this);

        binding.activityPull.getViewTreeObserver()
                .addOnGlobalFocusChangeListener(onGlobalFocusChangeListener);
    }

    @Override
    public void onClick(View v) {
        handleClickAndKey(v, true);
    }

    private ViewTreeObserver.OnGlobalFocusChangeListener onGlobalFocusChangeListener
            = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            Log.d(TAG, "onGlobalFocusChanged " + newFocus + " " + oldFocus);
            selEffectBridge.setVisibleWidget(false);
            binding.pullMianup.setFocusView(newFocus, oldFocus, 0f);
            newFocus.bringToFront();
        }
    };

    public void updateBottom(int vId, AppInfor app) {
        Log.d(TAG, String.format("updateBottom %d %s", vId, app));
        switch (vId) {
            case R.id.pull_tag_9301: {
                loadImage(binding.pullTag9301, app);
                return;
            }
            case R.id.pull_tag_9302: {
                loadImage(binding.pullTag9302, app);
                return;
            }
            case R.id.pull_tag_9303: {
                loadImage(binding.pullTag9303, app);
                return;
            }
            case R.id.pull_tag_9304: {
                loadImage(binding.pullTag9304, app);
                return;
            }
            case R.id.pull_tag_9305: {
                loadImage(binding.pullTag9305, app);
                return;
            }
            case R.id.pull_tag_9306: {
                loadImage(binding.pullTag9306, app);
                return;
            }
            case R.id.pull_tag_9307: {
                loadImage(binding.pullTag9307, app);
                return;
            }
            case R.id.pull_tag_9308: {
                loadImage(binding.pullTag9308, app);
                return;
            }
            case R.id.pull_tag_9309: {
                loadImage(binding.pullTag9309, app);
                return;
            }
            case R.id.pull_tag_9310: {
                loadImage(binding.pullTag9310, app);
                return;
            }
            case R.id.pull_tag_9311: {
                loadImage(binding.pullTag9311, app);
                return;
            }
            case R.id.pull_tag_9312: {
                loadImage(binding.pullTag9312, app);
                return;
            }
            case R.id.pull_tag_9313: {
                loadImage(binding.pullTag9313, app);
                return;
            }
            case R.id.pull_tag_9314: {
                loadImage(binding.pullTag9314, app);
                return;
            }
            case R.id.pull_tag_9315: {
                loadImage(binding.pullTag9315, app);
                return;
            }
            case R.id.pull_tag_9316: {
                loadImage(binding.pullTag9316, app);
                return;
            }
        }
    }

    private void loadImage(ImageView imageView, AppInfor app) {
        imageView.setTag(null);
        Glide.with(context).load(app.getIcon()).apply(glideOptions)
                .into(imageView);
        imageView.setTag(app.getPackageName());
    }

    public static BottomAppDialog showBottomAppDialog(Context context) {
        BottomAppDialog bottomAppDialog = new BottomAppDialog(context, R.style.MenuDialog);
        bottomAppDialog.show();
        Window window = bottomAppDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = (int) MyApplication.res().getDimension(R.dimen.bottom_app_root_bottom_margin);
        lp.width = (int) MyApplication.res().getDimension(R.dimen.bottom_app_root_w);
        lp.height = (int) MyApplication.res().getDimension(R.dimen.bottom_app_root_h);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.BOTTOM_DIALOG_ANIM);
//        bottomAppDialog.getWindow()
//                .setLayout(
//                        (int) MyApplication.res().getDimension(R.dimen.recent_app_root_w),
//                        (int) MyApplication.res().getDimension(R.dimen.recent_app_root_h));
        return bottomAppDialog;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_MENU:

                    handleClickAndKey(v, false);
                    break;
            }
        }

        return false;
    }

    private void handleClickAndKey(View v, boolean isClick) {
        int vId = v.getId();
        Object tag = v.getTag();
        if (tag == null || !isClick) {
            ((MainActivity) context).ShowHotAppDialog(tag, vId);
        } else {
            ((MainActivity) context).launchApp(tag.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.activityPull.getViewTreeObserver()
                .removeOnGlobalFocusChangeListener(onGlobalFocusChangeListener);
    }
}
