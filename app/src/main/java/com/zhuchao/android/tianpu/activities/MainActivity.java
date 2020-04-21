package com.zhuchao.android.tianpu.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.zhuchao.android.callbackevent.NormalRequestCallback;
import com.zhuchao.android.databaseutil.SPreference;
import com.zhuchao.android.libfilemanager.AppsChangedCallback;
import com.zhuchao.android.libfilemanager.MyAppsManager;
import com.zhuchao.android.libfilemanager.bean.AppInfor;
import com.zhuchao.android.netutil.NetUtils;
import com.zhuchao.android.netutil.NetUtils.NetChangedCallBack;
import com.zhuchao.android.netutil.OkHttpUtils;
import com.zhuchao.android.playsession.SessionCompleteCallback;
import com.zhuchao.android.tianpu.BuildConfig;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.bridge.SelEffectBridge;
import com.zhuchao.android.tianpu.data.PackageName;
import com.zhuchao.android.tianpu.data.json.regoem.CheckMacBean;
import com.zhuchao.android.tianpu.data.json.regoem.Recommend3Bean;
import com.zhuchao.android.tianpu.data.json.regoem.RecommendBean;
import com.zhuchao.android.tianpu.data.json.regoem.RecommendbgBean;
import com.zhuchao.android.tianpu.data.json.regoem.RecommendlogoBean;
import com.zhuchao.android.tianpu.data.json.regoem.RecommendmarqueeBean;
import com.zhuchao.android.tianpu.data.json.regoem.RecommendversionBean;
import com.zhuchao.android.tianpu.data.json.regoem.RemoveAppBean;
import com.zhuchao.android.tianpu.databinding.ActivityMainBinding;
import com.zhuchao.android.tianpu.services.MyService;
import com.zhuchao.android.tianpu.services.iflytekService;
import com.zhuchao.android.tianpu.utils.GlideMgr;
import com.zhuchao.android.tianpu.utils.ScreenUtils;
import com.zhuchao.android.tianpu.utils.TimeHandler;
import com.zhuchao.android.tianpu.utils.WallperHandler;
import com.zhuchao.android.tianpu.views.dialogs.HotAppDialog;
import com.zhuchao.android.tianpu.views.dialogs.Mac_Dialog;
import com.zhuchao.android.video.OMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oplayer.MediaLibrary;

import static android.view.MotionEvent.ACTION_UP;
import static com.zhuchao.android.libfilemanager.MyAppsManager.DELFROMMYAPPS_ACTION;
import static com.zhuchao.android.libfilemanager.MyAppsManager.SCANING_COMPLETE_ACTION;
import static com.zhuchao.android.tianpu.utils.PageType.MY_APP_TYPE;
import static com.zhuchao.android.tianpu.utils.PageType.RECENT_TYPE;
import static oplayer.MediaLibrary.MOVIE_CATEGORY;

public class MainActivity extends Activity implements OnTouchListener, OnGlobalFocusChangeListener, NetChangedCallBack, View.OnLongClickListener,
        View.OnClickListener, TimeHandler.OnTimeDateListener, WallperHandler.OnWallperUpdateListener, AppsChangedCallback, ViewTreeObserver.OnGlobalLayoutListener,
        View.OnKeyListener, ServiceConnection, SessionCompleteCallback {
    private Context mContext;
    public static final String bootVideo = "/system/media/boot.mp4";
    public static final String newBootVideo = "/system/media/new_boot.zip";
    private static final String TAG = "MainActivity";
    private static final String StartDragonTest = "1379";//测试
    private static final String StartDragonAging = "2379";//老化
    private static final String versionInfo = "3379";//版本信息


    private final int CustomId = -1;    //客户号  (国内)  录入版
    private final String DeviceModelNumber = "750";//TVBOX 天谱
    private final String host = "http://www.gztpapp.cn:8976/";    //天谱  （节流）
    private final String lunchname = "SAV";
    private final String appName = "SAV";      //天谱 （国内万利达）


    private HomeWatcherReceiver mHomeKeyReceiver = null;
    private MyReceiver myReceiver = null;
    private BootCompletedReceiver mBootCompletedReceiver = null;

    /**
     * 获取推荐的广告列表
     */
    private HashMap<String, String> web = new HashMap<>();
    private String ClickOnTheAD = null;
    //广告视频链接
    private String AD_Name = null;


    private Mac_Dialog mDialog;
    private ActivityMainBinding binding;
    private SelEffectBridge selEffectBridge;
    private TimeHandler timeHandler;

    private List<String> imgUrls;
    private String mSerialCommand;
    private int nba = 0; //清零计数
    private String TheLastSourceType = null;


    /**
     * 获取相应APP的主页面的图片
     */
    private Map<String, String> MainImgList = new HashMap<>();
    //网络数据/cache
    private RecommendBean recommendBean;
    //private RecommendlogoBean recommendlogoBean;
    //private Recommend3Bean recommend3Bean;
    //private RecommendmarqueeBean recommendmarqueeBean;
    //private RecommendbgBean recommendbgBean;


    private boolean isCheckedVersion = false;
    private FrameLayout[] flItems;
    private ProgressBar[] pbItems;
    private TextView[] tvItems;
    private ImageView[] ImageViewList;


    //private MySerialPort serialPortUtils = new MySerialPort(this);
    private MyService myService;
    //private Handler SerialPortReceiveHandler;

    private byte[] BluetoothClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0D, 0x7E};//蓝牙  K50
    private byte[] CopperShaftClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x01, 0x0A, 0x7E};//同轴     K50
    private byte[] OpticalFiberClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x02, 0x0B, 0x7E};//光纤    K50
    private byte[] SimulationLineInClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, (byte) 0x80, 0x00, (byte) 0x89, 0x7E};//模拟  K50
    private byte[] UsbClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x08, 0x00, 0x11, 0x7E};//USB  K50
    private byte[] LastChanelApp = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x09, 0x7E};//最后使用的app  K50
    //private byte[] QueryStateK50 = {0x01, 0x01, 0x06, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x09, 0x7E};//初始状态  K50


    private long TimeTickCount = 0;
    private String InpputNumStr = "";
    private boolean isCharging = false;
    private boolean blutoothConnected = false;
    private View OldView = null;

    private NetUtils netUtils = null;
    private MyAppsManager mMyAppsManager = null;

    private int mMainLayoutHeight = 0;
    private OMedia oMedia=null;
    //private int mNavigateStatus = 0;
    public static void sendKeyEvent(final int KeyCode) {
        new Thread() {     //不可在主线程中调用
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MediaLibrary.getSessionManager(this).setUserSessionCallback(this);
        //启动服务
        StartMyService();
        netUtils = new NetUtils(MainActivity.this, this);
        mMyAppsManager = new MyAppsManager(MainActivity.this, this);
        mMyAppsManager.getFilter().add("com.zhuchao.android.tianpu");
        mMyAppsManager.getFilter().add("com.wxs.scanner");
        mMyAppsManager.getFilter().add("com.iflytek.xiri");
        mMyAppsManager.getFilter().add("com.softwinner.dragonbox");
        mMyAppsManager.getFilter().add("com.android.camera2");
        mMyAppsManager.getFilter().add("com.android.soundrecorder");

        binding.fl5.setOnClickListener(this);
        //binding.fl5.setOnKeyListener(this);
        binding.fl6.setOnClickListener(this);
        //binding.fl6.setOnKeyListener(this);
        binding.fl3.setOnClickListener(this);
        //binding.fl3.setOnKeyListener(this);
        binding.fl4.setOnClickListener(this);
        //binding.fl4.setOnKeyListener(this);
        //binding.fl2.setOnKeyListener(this);
        binding.fl2.setOnClickListener(this);
        //binding.fl7.setOnKeyListener(this);
        binding.fl7.setOnClickListener(this);
        //binding.fl8.setOnKeyListener(this);
        binding.fl8.setOnClickListener(this);
        binding.fl1.setOnClickListener(this);
        //binding.fl1.setOnKeyListener(this);
        // binding.ad.setOnKeyListener(this);
        binding.ad.setOnClickListener(this);
        binding.fl4.setOnClickListener(this);
        //binding.fl4.setOnKeyListener(this);
        //binding.fl11.setOnKeyListener(this);
        //binding.fl11.setOnClickListener(this);
        //binding.fl12.setOnKeyListener(this);
        //binding.fl12.setOnClickListener(this);
        // binding.fl13.setOnKeyListener(this);
        //binding.fl13.setOnClickListener(this);
        //binding.fl14.setOnKeyListener(this);
        //binding.fl14.setOnClickListener(this);
        //binding.fl15.setOnKeyListener(this);
        //binding.fl15.setOnClickListener(this);
        // binding.fl16.setOnKeyListener(this);
        //binding.fl16.setOnClickListener(this);
        binding.fl2.setOnClickListener(this);
        binding.fl2.setOnLongClickListener(this);

        binding.fl0.setOnClickListener(this);
        binding.fl0.setOnKeyListener(this);


        //binding.fl11.setOnTouchListener(this);
        //binding.fl14.setOnTouchListener(this);
        //binding.fl15.setOnTouchListener(this);
        //binding.fl16.setOnTouchListener(this);

        binding.fl0.setOnTouchListener(this);
        binding.fl1.setOnTouchListener(this);
        binding.fl2.setOnTouchListener(this);
        binding.fl2.setOnLongClickListener(this);

        binding.fl3.setOnTouchListener(this);
        binding.fl4.setOnTouchListener(this);
        binding.fl6.setOnTouchListener(this);
        binding.fl7.setOnTouchListener(this);
        binding.fl8.setOnTouchListener(this);

        binding.ad.setOnTouchListener(this);

        binding.mainRl.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        binding.mainRl.getViewTreeObserver().addOnGlobalLayoutListener(this);

        flItems = new FrameLayout[]{binding.fl1, binding.fl2, binding.fl4};//需要下载k歌，腾讯视频，qq音乐
        pbItems = new ProgressBar[]{binding.progressBar1, binding.progressBar2, binding.progressBar9};
        tvItems = new TextView[]{binding.tvState1, binding.tvState2, binding.tvState9};
        //ImageViewList = new SimpleDraweeView[]{binding.bgIv1, binding.bgIv8};
        ImageViewList = new ImageView[]{binding.bgIv1, binding.bgIv2};

        //时间更新
        timeHandler = new TimeHandler(this);
        timeHandler.setOnTimeDateListener(this);


        binding.scrollTv.setText("欢迎来到天谱！Welcome to Tianpu!欢迎来到天谱！Welcome to Tianpu!欢迎来到天谱！Welcome to Tianpu!");
        binding.scrollTv.setSelected(true);
        //binding.ivFill.setVisibility(View.GONE);
        //binding.bgIv5.setImageResource(R.drawable.bb2);


        //binding.bgIv5.setOnClickListener(this);
        //binding.ivFill.setOnClickListener(this);


        selEffectBridge = (SelEffectBridge) binding.mainUpView.getEffectBridge();
        binding.mainRl.getViewTreeObserver().addOnGlobalFocusChangeListener(this);


        //注册广播接收器
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zhuchao.android.tianpu.services");
        registerReceiver(myReceiver, filter);

        mBootCompletedReceiver = new BootCompletedReceiver();
        filter = new IntentFilter();
        filter.addAction("com.iflytek.xiri.init.start");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(mBootCompletedReceiver, filter);


        requestPermition();
        setupItemBottomTag();
        MediaLibrary.getSessionManager(this).setUserSessionCallback(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "launcher is onStart");
        timeHandler.regTimeReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        switchToOtherChanel("I2s 通道");
        Log.d(TAG, "launcher is onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();

        pauseSystemMusic();
        binding.adBg.startAutoPlay();
        registerHomeKeyReceiver(this);

        View rootview = MainActivity.this.getWindow().getDecorView();
        View v = rootview.findFocus();

        if (OldView != null) {
            OldView.requestFocus();
        } else {
            binding.fl0.setFocusable(true);
            binding.fl0.requestFocus();
        }

        new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            View v = rootview.findFocus();
                            if (v != null) {
                                ViewGroup root = (ViewGroup) rootview;
                                Rect rect = new Rect();
                                root.offsetDescendantRectToMyCoords(v, rect);
                                if (rect.left > 0 && rect.right > 0) {
                                    setFocuseEffect(v);
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }.start();

        if(oMedia ==null) {
            OMedia oMedia = new OMedia("http://ivi.bupt.edu.cn/hls/cctv10.m3u8");
            oMedia.with(this).playOn(binding.surfaceView);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        try {
            timeHandler.release();
            timeHandler.setOnTimeDateListener(null);
            timeHandler = null;
            netUtils.Free();
            unregisterHomeKeyReceiver(this);
            unregisterReceiver(myReceiver);
            timeHandler.unRegTimeReceiver();
            binding.adBg.stopAutoPlay();

            MediaLibrary.ClearOPlayerSessionManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        OnMainPageViewClick(v, -1, true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //v.requestFocus();
        if (event.getAction() == ACTION_UP)
            OnMainPageViewClick(v, -1, true);

        return true;//super.onTouchEvent(event);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == binding.fl2.getId()) {
            ShowHotAppDialog(v.getTag(), R.id.fl2);
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.e("key","onKey>>>>keyCode="+keyCode+"    KeyEvent="+event);
        Log.e(TAG,">>>>>>>>>>>>>>>>>>>>>>>>>>>>"+keyCode);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MENU:
                    //Toast.makeText(mContext, "menu1", Toast.LENGTH_SHORT).show();
                    //OnMainPageViewClick(v, keyCode, false);
                    if (v.getId() == R.id.fl2) {
                        ShowHotAppDialog(v.getTag(), R.id.fl2);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    //handleViewKeyDown(v);
                    break;
                case KeyEvent.KEYCODE_HOME:
                case KeyEvent.KEYCODE_BACK:
                    //binding.ivFill.setVisibility(View.GONE);

                    break;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Log.e("key","onKeyDown>>>>>event="+event);
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            //binding.ivFill.setVisibility(View.GONE);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            //binding.ivFill.setVisibility(View.GONE);
            inputNumber("BACK");
            return true;
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_0:// 0
                    inputNumber("0");
                    break;
                case KeyEvent.KEYCODE_1:// 1
                    inputNumber("1");
                    break;
                case KeyEvent.KEYCODE_2:// 2
                    inputNumber("2");
                    break;
                case KeyEvent.KEYCODE_3:// 3
                    inputNumber("3");
                    break;
                case KeyEvent.KEYCODE_4:// 4
                    inputNumber("4");
                    break;
                case KeyEvent.KEYCODE_5:// 5
                    inputNumber("5");
                    break;
                case KeyEvent.KEYCODE_6:// 6
                    inputNumber("6");
                    break;
                case KeyEvent.KEYCODE_7:// 7
                    inputNumber("7");
                    break;
                case KeyEvent.KEYCODE_8:// 8
                    inputNumber("8");
                    break;
                case KeyEvent.KEYCODE_9:// 9
                    inputNumber("9");
                    break;
                case KeyEvent.KEYCODE_F1: //F1
                    inputNumber("F1");
                    break;
                case KeyEvent.KEYCODE_F2:    //F2
                    inputNumber("F2");
                    break;
                case KeyEvent.KEYCODE_F3:     //F3
                    inputNumber("F3");
                    break;
                case KeyEvent.KEYCODE_MENU:
                case KeyEvent.KEYCODE_F11:    //天普遥控器的设置键
                    //openSettings();
                    break;
                case KeyEvent.KEYCODE_G:      //天普遥控器的USB键
                    //launchApp("com.android.music");
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    View rootview = this.getWindow().getDecorView();
                    int focusId = rootview.findFocus().getId();
                    Log.i(TAG, "id = 0x" + Integer.toHexString(focusId));
                    break;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public void requestPermition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                //preferencesUtility.setString("storage", "true");
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
                //preferencesUtility.setString("storage", "true");
            }

            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    //Toast.makeText(MainActivity.this,"not granted",Toast.LENGTH_SHORT);
                }
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        if (mMainLayoutHeight <= 100)
            mMainLayoutHeight = binding.mainRl.getHeight();

        int height = binding.mainRl.getHeight();
        int sHeight = ScreenUtils.getNavigationBarHeight(this);
        if (Math.abs((mMainLayoutHeight - height)) == sHeight) {
            //mNavigateStatus = 1;
            mMainLayoutHeight = binding.mainRl.getHeight();
            new Thread() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        View rootview = MainActivity.this.getWindow().getDecorView();
                        View v = rootview.findFocus();

                        @Override
                        public void run() {
                            if (v != null) {
                                Rect rect = new Rect();
                                ((ViewGroup) rootview).offsetDescendantRectToMyCoords(v, rect);
                                if (rect.left > 0 && rect.right > 0) {
                                    setFocuseEffect(v);
                                }
                            }
                        }
                    });
                }
            }.start();
        }
    }


    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (newFocus == null)
            return;

        int focusVId = newFocus.getId();

        if (focusVId == R.id.fl7) {
            String mac = netUtils.getMAC().toUpperCase();
            binding.mac.setText(String.format("MAC: %s", mac));
            binding.mac.setVisibility(View.VISIBLE);
        } else {
            binding.mac.setVisibility(View.INVISIBLE);
        }

        OldView = newFocus;
        switch (focusVId) {
            case R.id.ad:
                selEffectBridge.setUpRectResource(R.drawable.home_sel_btn0);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                newFocus.bringToFront();
                break;
            case R.id.fl4:
            case R.id.fl3:
            case R.id.fl6:
            case R.id.fl7:
            case R.id.fl8://最下面一排
                selEffectBridge.setUpRectResource(R.drawable.home_sel_btn0);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.1f);
                /*if(oldFocus !=null)
                oldFocus.animate().scaleX(1.0f).scaleY(1.0f).start();
                newFocus.animate().scaleX(1.2f).scaleY(1.2f).start();*/
                newFocus.bringToFront();
                break;
            case R.id.fl5: //中央
                selEffectBridge.setUpRectResource(R.drawable.but);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                newFocus.bringToFront();
                break;

            case R.id.fl2://最左边
            case R.id.fl0:
            case R.id.fl1:
                selEffectBridge.setUpRectResource(R.drawable.bgmbgm);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                /*if(oldFocus !=null)
                    oldFocus.animate().scaleX(1.0f).scaleY(1.0f).start();
                newFocus.animate().scaleX(1.2f).scaleY(1.2f).start();*/
                newFocus.bringToFront();
                break;

            /*case R.id.fl11: //中央下面的第一按钮
                selEffectBridge.setUpRectResource(R.drawable.left);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                newFocus.bringToFront();
                break;
            case R.id.fl14:
                selEffectBridge.setUpRectResource(R.drawable.home_sel_btn1);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                newFocus.bringToFront();
                break;
            case R.id.fl15:
                selEffectBridge.setUpRectResource(R.drawable.right);
                selEffectBridge.setVisibleWidget(false);
                binding.mainUpView.setFocusView(newFocus, oldFocus, 1.0f);
                newFocus.bringToFront();
                break;*/
            default:
                break;// throw new IllegalStateException("Unexpected value: " + focusVId);
        }
    }

    @SuppressLint("LongLogTag")
    private void registerHomeKeyReceiver(Context context) {
        Log.i(TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(HomeWatcherReceiver.ACTION_BATTERY_CHARGE);
        filter.addAction(HomeWatcherReceiver.ACTION_BATTERY_INFO);
        filter.addAction("BLUTOOLTH_STATUS");
        filter.addAction("COMMAND_DATA");
        context.registerReceiver(mHomeKeyReceiver, filter);
    }

    @SuppressLint("LongLogTag")
    private void unregisterHomeKeyReceiver(Context context) {
        Log.i(TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    private void openSettings() {
        if (mMyAppsManager.isTheAppExist("com.android.tv.settings")) {
            mMyAppsManager.startTheApp("com.android.tv.settings");
        } else {
            mMyAppsManager.startTheApp("com.android.settings");
            //Intent in = new Intent();
            //in.setClassName("com.android.settings", "com.android.settings.Settings");
            //startActivity(in);
        }
    }

    public MyAppsManager getmMyAppsManager() {
        return mMyAppsManager;
    }

    /**
     * 暂停系统播放器
     */
    private void pauseSystemMusic() {
        Intent freshIntent = new Intent();
        freshIntent.setAction("com.android.music.musicservicecommand.pause");
        freshIntent.putExtra("command", "pause");
        sendBroadcast(freshIntent);
    }

    public void setFocuseEffect(View v) {
        onGlobalFocusChanged(null, v);
    }

    public void myServiceSendBytes(byte[] bytes) {
        if (myService != null)
            myService.sendCommand(bytes);
    }

    public void StartMyService() {
        Intent intent = new Intent(this, MyService.class);
        //启动servicce服务
        startService(intent);
        bindService(new Intent(this, MyService.class), this, BIND_AUTO_CREATE);


        Intent iii;
        iii = new Intent(MainActivity.this, iflytekService.class);
        Log.d(TAG, "start iflytekService");
        startService(iii);
    }

    private void switchToOtherChanel(String ChanelName) {
        Log.i(TAG, "切换通道：" + ChanelName);
        myServiceSendBytes(LastChanelApp);
    }

    /**
     * 设置item底部标签
     */
    private void setupItemBottomTag() {
        for (int i = 0; i < pbItems.length; i++) {
            //todo 全部隐藏
            pbItems[i].setVisibility(View.GONE);
            tvItems[i].setVisibility(View.GONE);
        }
    }

    /**
     * @param v
     * @param keyCode
     * @param isClick true:点击 false:菜单
     */
    private Drawable GetDrawable(String packageName)
    {
        AppInfor appInfor=mMyAppsManager.getAppInfor(packageName);
        if(appInfor != null)
            return appInfor.getIcon();
        else
            return null;
    }
    private void OnMainPageViewClick(View v, int keyCode, boolean isClick) {
        Drawable drawable;
        int id = v.getId();
        drawable = GetDrawable(PackageName.qqMusic);
        switch (id) {
            case R.id.fl4:
                //QQ音乐
                binding.fl4.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                drawable = GetDrawable(PackageName.qqMusic);
                //binding.bgIcon.setImageDrawable(drawable);
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor(PackageName.qqMusic).getIcon()).into(binding.bgIcon);
                launchApp(PackageName.qqMusic);
                switchToOtherChanel("QQ音乐");
                break;
            case R.id.fl0:
                //全民k歌
                binding.fl0.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                drawable = GetDrawable(PackageName.qmSing);
                //binding.bgIcon.setImageDrawable(drawable);
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor(PackageName.qmSing).getIcon()).into(binding.bgIcon);
                launchApp(PackageName.qmSing);
                switchToOtherChanel("全民k歌");
                break;
            case R.id.fl6:
                //文件管理器
                binding.fl6.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                drawable = GetDrawable("com.softwinner.TvdFileManager");
                //binding.bgIcon.setImageDrawable(drawable);
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor("com.softwinner.TvdFileManager").getIcon()).into(binding.bgIcon);
                launchApp("com.softwinner.TvdFileManager");
                switchToOtherChanel("文件管理器");
                break;
            case R.id.fl3:
                //我的应用
                binding.fl3.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                //Glide.with(this).asBitmap().load(R.drawable.tp5).into(binding.bgIcon);
                AppsActivity.lunchAppsActivity(this, MY_APP_TYPE);
                switchToOtherChanel(v.getClass().getName());
                break;
            case R.id.fl7:
                //系统设置
                binding.fl7.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                //binding.bgIcon.setImageDrawable(GetDrawable("com.android.settings"));
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor("com.android.settings").getIcon()).into(binding.bgIv5);
                openSettings();

                switchToOtherChanel("系统设置");
                break;
            case R.id.fl8:
                //hdp 频道
                binding.fl8.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                drawable = GetDrawable(PackageName.hdp);
                //binding.bgIcon.setImageDrawable(drawable);
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor(PackageName.hdp).getIcon()).into(binding.bgIcon);
                launchApp(PackageName.hdp);
                switchToOtherChanel("hdp 频道");
                break;
            case R.id.fl1:
                //腾讯视频
                binding.fl1.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                drawable = GetDrawable(PackageName.qqTv);
                //binding.bgIcon.setImageDrawable(drawable);
                //Glide.with(this).asBitmap().load(mMyAppsManager.getAppInfor(PackageName.qqTv).getIcon()).into(binding.bgIcon);
                launchApp(PackageName.qqTv);
                //binding.bgIcon.setVisibility(View.VISIBLE);
                switchToOtherChanel("腾讯视频");
                break;
            case R.id.ad:
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                if (web.size() > 0) {
                    switchToOtherChanel(v.getClass().getName());
                    WebRedirection();
                } else if (web.size() == 0) {
                    Toast.makeText(mContext, R.string.no_browsers, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.fl2:
                binding.fl2.requestFocus();
                //binding.bgIv5.setImageResource(R.drawable.bb2);
                Object obj = v.getTag();
                if (obj == null || !isClick) {
                    switchToOtherChanel(v.getClass().getName());
                    ShowHotAppDialog(obj, id);
                } else if (obj != null && isClick) {
                    switchToOtherChanel("我的APP");
                    launchApp(obj.toString());
                }
                break;
            /*case R.id.fl11:
                //蓝牙
                binding.fl11.requestFocus();
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv111.setImageResource(R.drawable.xsb);
                binding.bgIv113.setImageResource(R.drawable.xac);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIv5.setImageResource(R.drawable.ly);
                binding.ivFill.setImageResource(R.drawable.bly);
                binding.bluetooth.setVisibility(View.VISIBLE);
                binding.bluetooth.setImageResource(R.drawable.bluetoothno);
                binding.bgIcon.setVisibility(View.GONE);
                myServiceSendBytes(BluetoothClose);

                TheLastSourceType = "蓝牙";
                break;*/
            /*case R.id.fl12:
                //同轴
                binding.fl12.requestFocus();
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xsa);
                binding.bgIv113.setImageResource(R.drawable.xac);
                binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIv5.setImageResource(R.drawable.tz);
                binding.ivFill.setImageResource(R.drawable.btz);

                binding.bluetooth.setVisibility(View.GONE);
                binding.bgIcon.setVisibility(View.GONE);

                myServiceSendBytes(CopperShaftClose);
                TheLastSourceType = "同轴";
                break;
            case R.id.fl13:
                //光纤
                binding.fl13.requestFocus();
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv113.setImageResource(R.drawable.xsd);
                binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIv5.setImageResource(R.drawable.opt);
                binding.ivFill.setImageResource(R.drawable.bopt);

                binding.bluetooth.setVisibility(View.GONE);
                binding.bgIcon.setVisibility(View.GONE);

                TheLastSourceType = "光纤";
                myServiceSendBytes(OpticalFiberClose);
                break;
            case R.id.fl14:
                //模拟
                binding.fl14.requestFocus();
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv114.setImageResource(R.drawable.xsc);
                binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv113.setImageResource(R.drawable.xac);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIv5.setImageResource(R.drawable.mn);
                binding.ivFill.setImageResource(R.drawable.bmn);
                binding.bluetooth.setVisibility(View.GONE);
                binding.bgIcon.setVisibility(View.GONE);

                myServiceSendBytes(SimulationLineInClose);

                TheLastSourceType = "模拟";
                break;
            case R.id.fl15:
                //系统播放器
                binding.fl15.requestFocus();
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv115.setImageResource(R.drawable.xse);
                binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv113.setImageResource(R.drawable.xac);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv5.setImageResource(R.drawable.usbortf);
                binding.ivFill.setImageResource(R.drawable.busbortf);
                binding.bluetooth.setVisibility(View.GONE);
                binding.bgIcon.setVisibility(View.GONE);
                myServiceSendBytes(UsbClose);
                launchApp("com.android.music");

                TheLastSourceType = "player";
                break;
            case R.id.iv_fill:
                binding.ivFill.setVisibility(View.INVISIBLE);
                binding.bgIv5.bringToFront();
                break;
            case R.id.bg_iv5:
                binding.ivFill.setVisibility(View.VISIBLE);
                break;*/
            case R.id.fl5:
                //binding.ivFill.setVisibility(View.VISIBLE);
                break;
        }

        //getSharedPreferences("TheLastSourceType", MODE_PRIVATE).edit().putString("TheLastSourceType", TheLastSourceType).commit();
    }

    Runnable HandleSerialPortrunnable = new Runnable() {
        @Override
        public void run() {
            if (mSerialCommand.equals("0201050000020000010B7E") || mSerialCommand.equals("0101050000020000010A7E")) {
                pauseSystemMusic();
                //同轴
                /*TheLastSourceType = "同轴";
                binding.fl12.requestFocus();
                binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv113.setImageResource(R.drawable.xac);
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xsa);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);
                binding.bgIcon.setVisibility(View.GONE);

                binding.bgIv5.setImageResource(R.drawable.tz);
                binding.ivFill.setImageResource(R.drawable.btz);*/

            } else if (mSerialCommand.equals("0201050000020004000E7E") || mSerialCommand.equals("0101050000020004000D7E")) {
                //蓝牙
                pauseSystemMusic();
                TheLastSourceType = "蓝牙";

               /* binding.bgIv111.setImageResource(R.drawable.xsb);
                binding.bgIv113.setImageResource(R.drawable.xac);
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIcon.setVisibility(View.GONE);
                binding.ivFill.setImageResource(R.drawable.bly);
                binding.bgIv5.setImageResource(R.drawable.ly);
                binding.fl11.requestFocus();*/
            } else if (mSerialCommand.equals("0201050000020080008A7E") || mSerialCommand.equals("010105000002008000897E")) {
                pauseSystemMusic();
                //模拟
                TheLastSourceType = "模拟";

                /*binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv113.setImageResource(R.drawable.xac);
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xsc);
                binding.bgIv115.setImageResource(R.drawable.xae);

                binding.bgIcon.setVisibility(View.GONE);
                binding.bluetooth.setVisibility(View.INVISIBLE);


                binding.bgIv5.setImageResource(R.drawable.mn);
                binding.ivFill.setImageResource(R.drawable.bmn);
                binding.fl14.requestFocus();*/
            } else if (mSerialCommand.equals("0201050000020000020C7E") || mSerialCommand.equals("0101050000020000020B7E")) {
                pauseSystemMusic();
                //光纤
                TheLastSourceType = "光纤";

                /*binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv113.setImageResource(R.drawable.xsd);
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);
                binding.bgIcon.setVisibility(View.GONE);

                binding.bgIv5.setImageResource(R.drawable.opt);
                binding.ivFill.setImageResource(R.drawable.bopt);
                binding.bluetooth.setVisibility(View.INVISIBLE);
                binding.fl13.requestFocus();*/

            } else if (mSerialCommand.equals("0201050000020000000A7E") || mSerialCommand.equals("010105000002000000097E")) {
                pauseSystemMusic();
                //最后的app  I2S 通道
                TheLastSourceType = "last";
                /*binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv113.setImageResource(R.drawable.xac);
                //binding.bgIv116.setImageResource(R.drawable.blue6);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xae);*/
                binding.bluetooth.setVisibility(View.INVISIBLE);

            } else if (mSerialCommand.equals("020105000002000800127E") || mSerialCommand.equals("010105000002000800117E")) {
                pauseSystemMusic();
                //usb/TF
                TheLastSourceType = "player";
                /*binding.bgIv111.setImageResource(R.drawable.xaa);
                binding.bgIv113.setImageResource(R.drawable.xac);
                //binding.bgIv116.setImageResource(R.drawable.nnn);
                binding.bgIv112.setImageResource(R.drawable.xab);
                binding.bgIv114.setImageResource(R.drawable.xad);
                binding.bgIv115.setImageResource(R.drawable.xse);

                binding.bluetooth.setVisibility(View.INVISIBLE);
                binding.fl15.requestFocus();*/
                launchApp("com.android.music");

            } else if (mSerialCommand.equals("")) {
                //mic A开
                binding.micA.setImageResource(R.drawable.ano);
            } else if (mSerialCommand.equals("")) {
                //mic A关
                binding.micA.setVisibility(View.GONE);
            } else if (mSerialCommand.equals("")) {
                //mic B开
                binding.micB.setImageResource(R.drawable.bno);
            } else if (mSerialCommand.equals("")) {
                //mic B关
                binding.micB.setVisibility(View.GONE);
            }

            if (mSerialCommand.equals("0201050000020004000E7E") || mSerialCommand.equals("0101050000020004000D7E")) {
                if (blutoothConnected) {
                    binding.bluetooth.setVisibility(View.VISIBLE);
                    binding.bluetooth.setImageResource(R.drawable.bluetoothhave);
                }
            }
            //getSharedPreferences("TheLastSourceType", MODE_PRIVATE).edit().putString("TheLastSourceType", TheLastSourceType).commit();
        }
    };

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MyService.Binder binder = (MyService.Binder) service;
        myService = binder.getService();

        final Handler SerialPortReceiveHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        myService.setActionCallback(new MyService.Callback() {
            @Override
            public void onDataChange(String data) {
                mSerialCommand = data;
                Log.i("Callback.onDataChange", "data=" + data);
                if (null != SerialPortReceiveHandler) {
                    //Intent in = new Intent();
                    //in.setClassName("com.zhuchao.android.tianpu", "com.zhuchao.android.tianpu.activities.MainActivity");
                    //startActivity(in);
                    SerialPortReceiveHandler.post(HandleSerialPortrunnable);
                } else {
                    Log.e("tag", "SerialPortReceiveHandler=null");
                }
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e("tag", "后台服务已断开！");
    }

    /**
     * 点击广告图片，跳转到相对应的网页
     */
    private void WebRedirection() {
        String url = null;
        url = web.get(ClickOnTheAD);
        //Log.e("tag", "url=" + url);
        caculateUserclickAd(AD_Name);
        if (!"".equals(url) && null != url) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            this.startActivity(intent);
        } else {
            Toast.makeText(mContext, R.string.no_browsers, Toast.LENGTH_SHORT).show();
        }
    }

    public void launchApp(String packageName) {

        if (mMyAppsManager.isTheAppExist(packageName))
            mMyAppsManager.startTheApp(packageName);
        else
            Toast.makeText(mContext, "Not found the app you want:" + packageName, Toast.LENGTH_LONG).show();
    }


    public void ShowHotAppDialog(Object obj, int id) {

        HotAppDialog mHotAppDialog = HotAppDialog.showHotAppDialog(this, obj != null ? obj.toString() : null, id);
    }

    private void inputNumber(String i) {
        long inputTime = System.currentTimeMillis();
        if (inputTime - TimeTickCount < 1000) {
            //1s内输入有效
            if (i.equals("BACK")) {
                nba++;
            }
            InpputNumStr += i;
        } else {
            //如果输入时间超过1s,num统计的值重置为输入值
            InpputNumStr = i;
            nba = 0;
        }
        TimeTickCount = inputTime;

        switch (nba) {
            case 8:
                TimeTickCount = 0;
                clearData();
                nba = 0;
                break;
        }

        boolean b = false;

        switch (InpputNumStr) {
            case StartDragonTest:
                //重置输入
                InpputNumStr = "";
                TimeTickCount = 0;

                //startActivity(new Intent().setClassName("com.wxs.scanner", "com.wxs.scanner.activity.workstation.CheckActivity"));
                b = mMyAppsManager.startTheApp("com.wxs.scanner");
                if (!b)
                    break;
                Toast.makeText(mContext, "未安装老化测试App" + "com.wxs.scanner", Toast.LENGTH_SHORT).show();
            case StartDragonAging:
                //重置输入
                InpputNumStr = "";
                TimeTickCount = 0;
                b = mMyAppsManager.startTheApp("com.softwinner.agingdragonbox");
                if (!b)
                    break;
                Toast.makeText(mContext, "未安装老化测试App" + "com.wxs.scanner", Toast.LENGTH_SHORT).show();
                break;
            case versionInfo:
                InpputNumStr = "";
                TimeTickCount = 0;
                String deviceName;
                if ("702".equals(DeviceModelNumber)) {
                    deviceName = "柴喜";
                } else if ("701".equals(DeviceModelNumber)) {
//                    deviceName = "拓普赛特";
                    deviceName = "Mở rộng khu vực";
                } else if ("704".equals(DeviceModelNumber)) {
                    deviceName = "老凤祥";
                } else if ("696".equals(DeviceModelNumber)) {
                    deviceName = "精合智";
                } else {
                    deviceName = "其它";
                }
                new AlertDialog.Builder(mContext)
                        .setTitle("Phiên bản thông tin")
                        .setMessage(appName + "-" + BuildConfig.VERSION_NAME +
                                "\nPhạm vi dịch vụ：" + (host.startsWith("http://192.168.") ? "Mạng nội bộ" : "Mạng bên ngoài") +
                                "\nThương hiệu：" + deviceName)
                        .show();
                break;
            case "8888":
                InpputNumStr = "";
                TimeTickCount = 0;
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.test_cache, null);
                //((TextView) view1.findViewById(R.id.tv_content)).setText("" + removeResult);
                new AlertDialog.Builder(mContext)
                        .setTitle("remove result")
                        .setView(view1)
                        .show();

                break;
        }
    }

    @Override
    public void onTimeDate(String time, String date) {
        //Log.e(TAG, "onTimeDate " + time + " " + date);
        String times = time.split("#")[0];
        String week = time.split("#")[1];
        if (!TextUtils.isEmpty(date)) {
            binding.dateTv.setText(date);
        }
        if (!TextUtils.isEmpty(time)) {
            binding.timeTv.setText(times);
            binding.weekTv.setText(week);
        }
    }

    @Override
    public void wallperUpdate() {
        Log.d(TAG, "wallperUpdate");
        List<String> tmpImgUrls = null;
        if (tmpImgUrls == null || tmpImgUrls.size() == 0) {
            return;
        }
        binding.adBg.stopAutoPlay();
        this.imgUrls = tmpImgUrls;
        binding.adBg.setData(R.layout.ad_item, imgUrls, null);
        binding.adBg.setPageTransformer(Transformer.Cube);
        binding.adBg.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                String url = imgUrls.get(position);
                if (!TextUtils.isEmpty(url)) {
                    ((SimpleDraweeView) view).setImageURI(Uri.parse(imgUrls.get(position)));
                }
            }
        });
        binding.adBg.startAutoPlay();
    }


    @Override
    public void OnAppsChanged(String s, AppInfor appInfor) {
        //Log.d(TAG, s + ">>>>>>>>>>>>>>");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (s.equals("ADDTOMYAPPS")) {
                    GlideMgr.loadNormalDrawableImg(MainActivity.this, appInfor.getIcon(), binding.ivAdd1);
                    binding.tvAdd1.setText(appInfor.getName());
                    binding.fl2.setTag(appInfor.getPackageName());
                    //Log.d(TAG, s + ">>>>>>>>>>>>>>" + appInfor.toString());
                }
                if (s.equals(DELFROMMYAPPS_ACTION)) {
                    binding.ivAdd1.setImageResource(R.drawable.add);
                    binding.fl2.setTag(null);
                    binding.tvAdd1.setText(R.string.add);
                }
                if (s.equals(SCANING_COMPLETE_ACTION)) {
                    String pkg = SPreference.getSharedPreferences(mContext, "MyAppInfors", "MyAppInfors");
                    AppInfor appInfor1 = mMyAppsManager.getAppInfor(pkg);
                    if (appInfor1 != null) {
                        GlideMgr.loadNormalDrawableImg(MainActivity.this, appInfor1.getIcon(), binding.ivAdd1);
                        binding.tvAdd1.setText(appInfor1.getName());
                        binding.fl2.setTag(appInfor1.getPackageName());
                    }
                }
            }
        });
        //Log.d(TAG, s + ">>>>>>>>>>>>>>");
    }

    @Override
    public void onNetStateChanged(boolean b, int i, String s, String s1, String s2, String s3, String s4) {
        Log.d(TAG, "onNetStateChanged>>>>>>>>>> " + b + " " + i + " " + s + " " + s1 + " " + s2 + " " + s3 + " " + s4);
        if (b) {//网络有效
            if (netUtils.isNetCanConnect()) {//网络可以建立连接
                caculateOnlineTime();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            checkDeviceIsAvailable(netUtils.getDeviceID().toUpperCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
        synUpdateUI();
    }


    @Override
    public void onWifiLevelChanged(int i) {
        Log.d(TAG, "onWifiLevelChanged>>>>>>>>>>" + i);

        if (!netUtils.isWifiConnected()) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (i) {
                    case 0:
                        binding.netIv.setImageResource(R.drawable.wifi0);
                        break;
                    case 1:
                        binding.netIv.setImageResource(R.drawable.wifi1);
                        break;
                    case 2:
                        binding.netIv.setImageResource(R.drawable.wifi2);
                        break;
                    case 3:
                        binding.netIv.setImageResource(R.drawable.wifi3);
                        break;
                    default: // 2 -> ETH
                        binding.netIv.setImageResource(R.drawable.wifi_out_of_range);
                        break;
                }
            }
        });
    }

    public void synUpdateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.netIv.setVisibility(View.VISIBLE);

                if (netUtils.isNetCanConnect()) {
                    if (netUtils.isWifiConnected()) {
                        binding.netIv.setImageResource(R.drawable.wifi3);
                    } else
                        binding.netIv.setImageResource(R.drawable.net);
                } else
                    binding.netIv.setImageResource(R.drawable.netno);
            }
        });
    }

    private void showMACDialog(String type) {
        if (mDialog == null || mDialog.isShowing() != true) {
            mDialog = new Mac_Dialog(this);
            mDialog.setVolumeAdjustListener(MainActivity.this);
            mDialog.setCancelable(false);
            mDialog.show();
        }
        mDialog.adjustVolume(true, type);
    }

    // DMN DeviceModelNumber, // DID :Device ID, // CID: Custorm id, // ip: net ip , // RID: ip region,
    private String getMyUrl(String api, String DMN, String DID, int CID, String IP, String RID, String LuncherName) {
        String url = "";
        if (CustomId != -1) {
            url = host + api +
                    "cy_brand_id=" + DeviceModelNumber +
                    "&mac=" + netUtils.getDeviceID().toUpperCase() +
                    "&netCardMac=" + netUtils.getMAC().toUpperCase() +
                    "&CustomId=" + CID +
                    "&codeIp=" + IP +
                    "&region=" + RID +
                    "&CustomId=" + CustomId +
                    "&lunchname=" + lunchname;
        } else {
            url = host + api +
                    "cy_brand_id=" + DeviceModelNumber +
                    "&mac=" + netUtils.getDeviceID().toUpperCase() +
                    "&netCardMac=" + netUtils.getMAC().toUpperCase() +
                    "&codeIp=" + IP +
                    "&region=" + RID +
                    "&lunchname=" + lunchname;
        }
        return url;
    }

    private void checkDeviceIsAvailable(String mac) {
        String url = "";

        if (TextUtils.isEmpty(mac)) return;

        if (CustomId != -1) {
            url = host + "jhzBox/box/loadBox.do?cy_brand_id=" + DeviceModelNumber + "&mac=" + mac +
                    "&netCardMac=" + netUtils.getMAC().toUpperCase() +
                    "&CustomId=" + CustomId +
                    "&codeIp=" + netUtils.getIP0() +
                    "&region=" + netUtils.getChineseRegion(netUtils.getLocation());
        } else {
            url = host + "jhzBox/box/loadBox.do?cy_brand_id=" + DeviceModelNumber + "&mac=" + mac +
                    "&netCardMac=" + netUtils.getMAC().toUpperCase() +
                    "&codeIp=" + netUtils.getIP0() +
                    "&region=" + netUtils.getChineseRegion(netUtils.getLocation());
        }

        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                if (i >= 0 && !TextUtils.isEmpty(s)) {
                    final CheckMacBean checkMacBean = new Gson().fromJson(s, CheckMacBean.class);
                    if (checkMacBean.getStatus() == 0) {
                        asynUpdateUI(); //设备已经授权
                    } else  //设备不可用
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMACDialog(netUtils.getDeviceID().toUpperCase());
                            }
                        });
                    }
                    if (!isCheckedVersion) {
                        isCheckedVersion = true;
                        checkSoftwareVersion();
                    }
                }
            }
        });
    }

    private synchronized void asynUpdateUI() {
        new Thread() {
            @Override
            public void run() {
                try {
                    getRecommendApp();
                    getRecommendAd();
                    getRecommendLogo();
                    getRecommendMarquee();
                    getRecommendBgImg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取卸载的app列表
     */
    private void getRemoveApp() {
        String url = host + "jhzBox/box/unload.do?cy_brand_id=" + DeviceModelNumber;
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                RemoveAppBean removeAppBean = new Gson().fromJson(s, RemoveAppBean.class);
                if (removeAppBean.getStatus() == 0) {
                    //卸载app
                    List<String> data = removeAppBean.getData();
                    for (String pck : data) {
                        mMyAppsManager.uninstall(pck);
                    }
                }
            }
        });
    }

    /**
     * 获取推荐的app列表
     */
    private void getRecommendApp() {
        //String url= "";
        String url = getMyUrl("jhzBox/box/loadPushApp.do?pitClass=01&", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                final RecommendBean rBean = new Gson().fromJson(s, RecommendBean.class);
                if (rBean.getStatus() == 0) {
                    //加载成功
                    final List<RecommendBean.DataBean> data = rBean.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //保存缓存
                            SPreference.saveSharedPreferences(MainActivity.this, "my_setting", "recommend_cache", s);
                            recommendBean = rBean;
                            //加载坑位的图片
                            if (data != null && data.size() >= ImageViewList.length) {
                                for (int i = 0; i < ImageViewList.length; i++) {
                                    if (!TextUtils.isEmpty(data.get(i).getSyy_app_img())) {
                                        Glide.with(mContext).load(data.get(i).getSyy_app_img()).into(ImageViewList[i]);
                                    }
                                }
                            }
                            //加载指定app主页图片
                            if (data != null && data.size() != 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    String pkn = data.get(i).getSyy_app_packageName();
                                    String img = data.get(i).getSyy_appstatus_img();
                                    if (!"".equals(img) && null != img) {
                                        MainImgList.put(pkn, img);
                                    }
                                }
                            }
                            //是否显示未下载标签
                            setupItemBottomTag();
                        }

                    });
                }
            }
        });
    }

    /**
     * 获取推荐的跑马灯
     */
    private void getRecommendMarquee() {
        String url = getMyUrl("jhzBox/box/loadMarquee.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {

            @Override
            public void onRequestComplete(String s, int i) {
                final RecommendmarqueeBean rBean = new Gson().fromJson(s, RecommendmarqueeBean.class);
                if (rBean.getStatus() == 0) {
                    //加载成功
                    final RecommendmarqueeBean.DataBean data = rBean.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SPreference.saveSharedPreferences(MainActivity.this, "my_setting", "recommend_marquee_cache", s);
                            //显示跑马灯
                            if (data != null) {
                                binding.scrollTv.setText(data.getMarquee());
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取推荐的背景图
     */
    private void getRecommendBgImg() {
        String url = getMyUrl("jhzBox/box/backgroundImg.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                if (i < 0) return;
                final RecommendbgBean rBean = new Gson().fromJson(s, RecommendbgBean.class);
                if (rBean.getStatus() == 0) {
                    SPreference.saveSharedPreferences(MainActivity.this, "my_setting", "recommend_bg_cache", s);
                    if (rBean.getStatus() == 0) {
                        //加载成功
                        final List<RecommendbgBean.DatabgBean> data = rBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示背景图
                                if (data != null && data.size() != 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        if (!TextUtils.isEmpty(data.get(i).getBackgroundImgAddress())) {
                                            Glide.with(mContext).load(data.get(i).getBackgroundImgAddress()).into(binding.ivBg);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取推荐的logo列表
     */
    private void getRecommendLogo() {
        String url = getMyUrl("jhzBox/box/loadLogo.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                final RecommendmarqueeBean rBean = new Gson().fromJson(s, RecommendmarqueeBean.class);
                if (rBean.getStatus() == 0) {
                    SPreference.saveSharedPreferences(MainActivity.this, "my_setting", "recommend_logo_cache", s);
                    //加载成功
                    final RecommendlogoBean logoBean = new Gson().fromJson(s, RecommendlogoBean.class);
                    if (logoBean.getStatus() == 0) {
                        //加载成功
                        final RecommendlogoBean.DataBean data = logoBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 显示品牌logo
                                if (data != null) {
                                    Glide.with(mContext).load(data.getSyy_special_fileOne()).into(binding.logoIv);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getRecommendAd() {
        String url = getMyUrl("jhzBox/box/loadAdv.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                if (i < 0) return;
                final Recommend3Bean rBean = new Gson().fromJson(s, Recommend3Bean.class);
                SPreference.saveSharedPreferences(MainActivity.this, "my_setting", "recommend_ad_cache", s);
                if (rBean.getStatus() == 0) {
                    //加载成功
                    final List<Recommend3Bean.DataBean> data = rBean.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示广告
                            String urls = null;
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    //广告链接
                                    web.put(data.get(i).getCy_advertisement_imgAddress(), data.get(i).getAdvLink());
                                    //视频广告链接
                                    if (data.get(i).getCy_advertisement_videoAddress() != null) {
                                        urls = (String) data.get(i).getCy_advertisement_videoAddress();
                                    }
                                }
                                if (urls == null) {
                                    binding.adBg.stopAutoPlay();
                                    binding.adBg.setData(R.layout.ad_item, data, null);
                                    binding.adBg.setmAdapter(new XBanner.XBannerAdapter() {
                                        @Override
                                        public void loadBanner(XBanner banner, Object model, View view, int position) {
                                            String url = data.get(position).getCy_advertisement_imgAddress();
                                            //获取被点击广告页的网址链接
                                            //String ad_id = null;
                                            if (position == 0) {
                                                ClickOnTheAD = data.get(data.size() - 1).getCy_advertisement_imgAddress();
                                                AD_Name = data.get(data.size() - 1).getCy_advertisement_id();
                                            } else {
                                                ClickOnTheAD = data.get(position - 1).getCy_advertisement_imgAddress();
                                                AD_Name = data.get(position - 1).getCy_advertisement_id();
                                            }
                                            if (!TextUtils.isEmpty(url)) {
                                                Glide.with(mContext).load(url).into((SimpleDraweeView) view);
                                            }
                                        }
                                    });
                                    binding.adBg.startAutoPlay();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 统计用户在线时长
     */
    private void caculateOnlineTime() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String url = getMyUrl("jhzBox/box/onlineTime.do?&", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
                        OkHttpUtils.request(url, new NormalRequestCallback() {
                            @Override
                            public void onRequestComplete(String s, int i) {

                            }
                        });
                        sleep(1000 * 60 * 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 清理数据
     */
    private void clearData() {
        String url = getMyUrl("jhzBox/box/removeIp.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {

            }
        });
    }

    /**
     * 统计用户点击app次数
     */
    private void CaculateclickApp(String app) {
        String url = getMyUrl("jhzBox/box/appLike.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {

            }
        });

    }

    /**
     * 统计用户点击广告的次数
     */
    private void caculateUserclickAd(String ad_) {
        String url = getMyUrl("jhzBox/box/advLike.do?cy_advertisement_id=" + ad_ + "&", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {

            }
        });
    }

    /**
     * 检查更新版本
     */
    private void checkSoftwareVersion() {
        String url = getMyUrl("jhzBox/box/appOnlineVersion.do?versionNum=" + BuildConfig.VERSION_NAME + "&cy_versions_name=" + appName + "&", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                if (i < 0) return;
                final RecommendversionBean versionBean = new Gson().fromJson(s, RecommendversionBean.class);
                if (versionBean.getStatus() == 0) {
                    final RecommendversionBean.DataBean data = versionBean.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.version_updating)
                                    .setMessage(data.getCy_versions_info())
                                    .setNegativeButton(R.string.cancles, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(mContext, R.string.background_download, Toast.LENGTH_SHORT).show();
                                            downloadApk(data.getCy_versions_path());
                                        }
                                    }).show();
                        }
                    });
                }
            }
        });
    }

    private void getRecommendVideo() {
        String url = getMyUrl("xpBox/box/loadBoot.do?", DeviceModelNumber, netUtils.getDeviceID().toUpperCase(), CustomId, netUtils.getIP0(), netUtils.getChineseRegion(netUtils.getLocation()), lunchname);
        OkHttpUtils.request(url, new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {

            }
        });
    }

    /**
     * 带进度下载apk
     *
     * @param dataBean
     * @param index
     */
    private void downloadApk(final RecommendBean.DataBean dataBean, final int index) {
        String url = dataBean.getSyy_app_download();//
        String toFilePath = mMyAppsManager.getDownloadDir() + dataBean.getSyy_app_download().substring(dataBean.getSyy_app_download().lastIndexOf("/") + 1);
        OkHttpUtils.Download(url, toFilePath, dataBean.getSyy_app_packageName(), new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i >= 0) {
                            flItems[index].setEnabled(true);
                            pbItems[index].setVisibility(View.GONE);
                            tvItems[index].setVisibility(View.GONE);
                        } else {
                            flItems[index].setEnabled(true);
                            tvItems[index].setText(R.string.download_failed);
                        }
                    }
                });
            }
        });
    }

    /**
     * 普通下载apk安装
     *
     * @param url
     */
    private void downloadApk(final String url) {
        String toFilePath = mMyAppsManager.getDownloadDir() + url.substring(url.lastIndexOf("/") + 1);
        OkHttpUtils.Download(url, toFilePath, this.getLocalClassName(), new NormalRequestCallback() {
            @Override
            public void onRequestComplete(String s, int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i >= 0) {
                            mMyAppsManager.install(toFilePath);
                        } else {
                            Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void OnSessionComplete(int i, String s) {
        handler.sendEmptyMessage(0x11);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                MediaLibrary.setupCategoryList();
                Log.d(TAG,"MediaLibrary.MOVIE_CATEGORY  size ="+ MOVIE_CATEGORY.size());
                if(MOVIE_CATEGORY.size()<=0) return;
                List<OMedia> list = MediaLibrary.getMediaListByIndex(0);
                if(list != null && list.size()>0)
                {
                    if(list.get(0) != null) {
                        oMedia = (OMedia) list.get(0);
                        oMedia.with(MainActivity.this).playOn(binding.surfaceView);
                    }
                }
                else {
                   oMedia = new OMedia("http://ivi.bupt.edu.cn/hls/cctv10.m3u8");
                   oMedia.with(MainActivity.this).playOn(binding.surfaceView);
                }
            }
        }
    };

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String pull_from = "52129";
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            String _action = bundle.getString("_Action");

            if (_action == null) return;

            if ((_action.contains("首页")) || (_action.contains("桌面")) || (_action.contains("主页"))) {
                //binding.ivFill.setVisibility(View.GONE);
            } else if (_action.contains("蓝牙")) {
                pauseSystemMusic();
                //onClick(binding.fl11);
                //launchApp("com.h3launcher");
            } else if ((_action.contains("同轴")) || (_action.contains("同舟"))) {
                //pauseSystemMusic();
                //onClick(binding.fl12);
            } else if (_action.contains("光纤")) {
                //pauseSystemMusic();
                //onClick(binding.fl13);
                //launchApp("com.h3launcher");
            } else if ((_action.contains("输入")) || (_action.contains("Line in")) || (_action.contains("模拟"))) {
                pauseSystemMusic();
                //OnMainPageViewClick(binding.fl14, -1, true);
            } else if (_action.contains("USB") || _action.contains("U盘") || _action.contains("TF卡") || _action.contains("优盘") || _action.contains("卡")) {
                //onClick(binding.fl15);
                Intent freshIntent = new Intent();
                freshIntent.setAction("com.android.music.musicservicecommand");
                freshIntent.putExtra("command", "play");
                sendBroadcast(freshIntent);
            } else if (_action.contains("文件")) {
                pauseSystemMusic();
                onClick(binding.fl6);
                //launchApp("com.softwinner.TvdFileManager");
            } else if (_action.contains("设置") || _action.contains("网络")) {
                onClick(binding.fl7);
            } else if (_action.contains("频道")) {
                pauseSystemMusic();
                //binding.ivFill.setVisibility(View.GONE);
                onClick(binding.fl8);
            } else if ((_action.contains("全民K歌")) || (_action.contains("我要唱歌")) || (_action.contains("我想唱歌")) || (_action.contains("K歌")) || (_action.contains("KTV"))) {
                OnMainPageViewClick(binding.fl0, -1, true);
            } else if ((_action.contains("腾讯视频")) || (_action.contains("云视听"))) {
                OnMainPageViewClick(binding.fl1, -1, true);
            } else if (_action.contains("应用") || _action.contains("程序")) {
                AppsActivity.lunchAppsActivity(MainActivity.this, MY_APP_TYPE);
            } else if (_action.contains("最近")) {
                AppsActivity.lunchAppsActivity(MainActivity.this, RECENT_TYPE);
            } else if ((_action != null) && (_action.equals("music") || _action.equals("ktv"))) {
            }
        }
    }

    class HomeWatcherReceiver extends BroadcastReceiver {

        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
        private static final String ACTION_BATTERY_CHARGE = "BATTERY_CHARGE";
        private static final String ACTION_BATTERY_INFO = "BATTERY_INFO";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    //binding.ivFill.setVisibility(View.GONE);
                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    Log.i(LOG_TAG, "long press home key or activity switch");
                    //binding.ivFill.setVisibility(View.GONE);
                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    //Log.i(LOG_TAG, "lock");
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    //binding.ivFill.setVisibility(View.GONE);
                }
                return;
            }

            if (action.equals(ACTION_BATTERY_CHARGE)) {
                isCharging = intent.getBooleanExtra("isCharge", false);
                if (isCharging) {
                    binding.ivBattery.setImageResource(R.drawable.charge);
                    // binding.ivBwi.setVisibility(View.INVISIBLE);
                } else {
                    //binding.ivBwi.setVisibility(View.VISIBLE);
                }
                return;
            }

            if (action.equals(ACTION_BATTERY_INFO) && !isCharging) {
                int value = intent.getIntExtra("value", -1);
                binding.ivBattery.setVisibility(View.VISIBLE);
                //binding.ivBwi.setVisibility(View.VISIBLE);

//                if ((value < 5)) {
//                    binding.ivBwi.setVisibility(View.VISIBLE);
//                    binding.ivBwi.setImageResource(R.drawable.battery_warnning);
//                    binding.ivBwi.bringToFront();
//                }


                if (value < 10)
                    binding.ivBattery.setImageResource(R.drawable.lowbattery);
                else if (value <= 20)
                    binding.ivBattery.setImageResource(R.drawable.cell1);
                else if (value <= 60)
                    binding.ivBattery.setImageResource(R.drawable.cell2);
                else if (value <= 80)
                    binding.ivBattery.setImageResource(R.drawable.cell3);
                else if (value > 80)
                    binding.ivBattery.setImageResource(R.drawable.cell4);

                return;
            }

            if (action.equals("BLUTOOLTH_STATUS")) {
                blutoothConnected = intent.getBooleanExtra("BLUTOOLTH_STATUS", false);
                if (blutoothConnected) {
                    binding.bluetooth.setVisibility(View.VISIBLE);
                    binding.bluetooth.setImageResource(R.drawable.bluetoothhave);
                } else {
                    binding.bluetooth.setVisibility(View.VISIBLE);
                    binding.bluetooth.setImageResource(R.drawable.bluetoothno);
                }
                return;
            }

        }
    }

    public class BootCompletedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                if (intent.getAction().equals("com.iflytek.xiri.init.start")) {
                    Intent iii;
                    iii = new Intent(MainActivity.this, iflytekService.class);
                    Log.d(TAG, "com.iflytek.xiri.init.start");
                    startService(iii);
                }
                if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                    Intent iii;
                    iii = new Intent(MainActivity.this, iflytekService.class);
                    Log.d(TAG, "android.intent.action.BOOT_COMPLETED");
                    startService(iii);
                }
            }
        }
    }


}

