package com.zhuchao.android.tianpu.services;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.xiri.AppService;
import com.iflytek.xiri.Feedback;
import com.zhuchao.android.tianpu.data.PackageName;
import com.zhuchao.android.tianpu.utils.ForegroundAppUtil;
import com.zhuchao.android.tianpu.utils.TypeTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.List;

/**
 * 继承自AppService，显示AppService接口，父类Service启动时调用
 *
 * @author hhtao
 */
public class iflytekService extends AppService {

    private static final String TAG = "iflytekService";//iflytekService.class.getSimpleName();
    private Feedback mFeedback = null;
    //private boolean bReb = false;
    private String action_Cammand = null;
    private final String pull_from = "52129";
    private String action_music = null;
    private byte tbb[] = {0, 0, 0, 0};


    private String mVoiceCommandStr = null;
    //private byte[] SetMusicVolume = {0x02, 0x01, 0x02, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0b, 0x7E};//设置音乐音量  K70//
    private byte[] SetMusicVolumeK50 = {0x01, 0x01, 0x02, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0a, 0x7E};//设置音乐音量  K50
    //private byte[] SetMicVolume = {0x02, 0x01, 0x03, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0d, 0x7E};//设置话筒音量  K70//
    private byte[] SetMicVolumeK50 = {0x01, 0x01, 0x03, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0c, 0x7E};//设置话筒音量  K50//
    //private byte[] MICEffetive = {0x02, 0x01, 0x04, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x09, 0x7E};// 效果 1  K70
    private byte[] MICEffetiveK50 = {0x01, 0x01, 0x04, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x08, 0x7E};//效果 1  K50
    //private byte[] CommandMuteOn = {0x02, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x07, 0x0C, 0x7E};// 静音开  K70
    private byte[] CommandMuteOnK50 = {0x01, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x07, 0x0B, 0x7E};//静音开  1 K50
    private byte[] CommandMuteClose = {0x02, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x08, 0x0D, 0x7E};// 静音关  K70
    private byte[] CommandMuteCloseK50 = {0x01, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x08, 0x0C, 0x7E};//静音关    K50
    private byte[] CommandStandby = {0x02, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x09, 0x0E, 0x7E};// 待机  K70
    private byte[] CommandStandbyK50 = {0x01, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00, 0x09, 0x0D, 0x7E};//待机 1  K50

    //private byte[] CommandMuteOn = {0x02, 0x01, 0x01, 0x00, 0x00, 0x02, 0x00, 0x07,0x00,0x0d,0x7E};// 静音开  K70
    //private byte[] CommandMuteOnK50 = {0x01, 0x01, 0x01, 0x00, 0x00, 0x02, 0x00, 0x07,0x00,0x0c,0x7E};//静音开  1 K50
    //private byte[] CommandMuteClose = {0x02, 0x01, 0x01, 0x00, 0x00, 0x02, 0x00, 0x08,0x00,0x0e,0x7E};// 静音关  K70
    //private byte[] CommandMuteCloseK50 = {0x01, 0x01, 0x01, 0x00, 0x00, 0x02, 0x00, 0x08,0x00,0x0d, 0x7E};//静音关    K50

    //private byte[] I2SAppOpen = {0x02, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x0A, 0x7E};//最后使用的app  K70
    private byte[] I2SAppClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x09, 0x7E};//最后使用的app  K50

    private MyReceiver myReceiver = null;


    @Override
    protected void onInit() {
        UpLoadVoiceCommand();
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        mFeedback = new Feedback(this);
        setAppListener(mAppListener);
        setVideoIntentListener(mIVideoIntentListener);

        UpLoadVoiceCommand();

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.iflytek.xiri.init.start");
        registerReceiver(myReceiver, filter);
    }

    /**
     * 暂停系统播放器
     */
    private void pauseMusic() {
        Intent freshIntent = new Intent();
        freshIntent.setAction("com.android.music.musicservicecommand.pause");
        freshIntent.putExtra("command", "pause");
        sendBroadcast(freshIntent);
    }


    private IAppListener mAppListener = new IAppListener() {
        @Override
        public void onExecute(Intent intent) {
            mFeedback.begin(intent);
            boolean bReb = false;
            try {
                bReb = doAppAction(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (bReb == false) {
                VoiceFeedback("当前设备没有找到这个命令，你是要唱歌吗？");
            }
            return;
        }
    };

    private IVideoIntentListener mIVideoIntentListener = new IVideoIntentListener() {
        @Override
        public void onExecute(Intent intent) {
            if (intent.hasExtra("text")) {
                boolean b = false;
                try {
                    b = doAppAction(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String info = intent.getStringExtra("text") + "\r\n";

                if (info.contains("电视")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            pauseMusic();
                            //myServiceSendBytes(I2SAppOpen);
                            SendBytesTo(I2SAppClose);
                        }
                    }).start();
                    PackageManager packageManager = getPackageManager();
                    Intent openQQintent = new Intent();
                    openQQintent = packageManager.getLaunchIntentForPackage("com.dianshijia.newlive");
                    if (openQQintent == null) {
                        System.out.println("APP not found!");
                    } else {
                        if (!isTopActivity("com.dianshijia.newlive"))
                            startActivity(openQQintent);
                        else {
                            // bReb = true;
                            //mFeedback.feedback("已经在全民K歌APP", Feedback.DIALOG);
                            //return true;
                        }
                    }
                    return;
                }

                if (b == false) {

                    if (isTopActivity("com.tencent.karaoketv") && (!info.contains("电影"))) {
                        String SKey = intent.getStringExtra("text") + "\r\n";
                        String url = "karaoketv://?action=4&pull_from=" + pull_from + "&m0=1&m1=" + SKey + "&mb=false";
                        Log.d(TAG, url);
                        Intent i = new Intent();
                        i.setData(Uri.parse(url));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.tencent.karaoketv");
                        Log.d(TAG, "全名K歌曲点歌---->" + info);
                        //sendBroadcast(i);
                        startActivity(i);
                        pauseMusic();
                        return;

                    } else if (isTopActivity("com.zhuchao.android.tianpu")) {
                    }


                    //String info = intent.getStringExtra("text") + "\r\n";
                    final Intent opentintent = new Intent();
                    opentintent.setData(Uri.parse("tenvideo2://?action=9&search_key=" + info));
                    opentintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    opentintent.setPackage("com.ktcp.tvvideo");// 设置视频包名，要先确认包名
                    startMainActivity();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //要执行的任务
                            startActivity(opentintent);
                        }
                    }, 1200);

                    Log.d(TAG, "正在打开腾讯视频搜索 +" + info);

                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pauseMusic();
                        //myServiceSendBytes(I2SAppOpen);
                        SendBytesTo(I2SAppClose);
                    }
                }).start();
            }
        }
    };

    private boolean doAppAction(Intent intent) {
        action_Cammand = intent.getStringExtra("_command");
        action_music = intent.getStringExtra("exit_text");
        Log.e("TTTT", "=====" + URLDecoder.decode(intent.toURI()));


        if (action_Cammand == null) {
            action_Cammand = intent.getStringExtra("text");
            Log.e("TTTTtext", "=====" + URLDecoder.decode(intent.toURI()));
            action_music = action_Cammand;
        }

        if (action_Cammand == null) {
            return false;
        }

        if (false && action_Cammand.contains("当贝市场"))
        {
            if (isTopActivity(PackageName.dbMarket)) {
                VoiceFeedback("当贝市场已经打开");
                return true;
            }
            PackageManager packageManager = getPackageManager();
            Intent toIntent = packageManager.getLaunchIntentForPackage(PackageName.dbMarket);
            if (toIntent == null) {
                System.out.println("APP not found!");
                VoiceFeedback("没有找到相关的应用");
                return true;
            }
            pauseMusic();
            SendBytesTo(I2SAppClose);
            VoiceFeedback("正在打开当贝市场");
            startActivity(toIntent);

            return true;//已处理
        }

        if (action_Cammand.contains("打开飞视浏览器")) {
            return true;//已处理
        }

        if (action_Cammand.contains("打开乐播投屏")) {
            return true;//已处理
        }

        if (action_Cammand.equals("电视直播")) {
            if (isTopActivity("com.dianshijia.newlive")) {
                VoiceFeedback("电视直播已经打开");
                return true;
            }

            PackageManager packageManager = getPackageManager();
            Intent openQQintent = new Intent();
            openQQintent = packageManager.getLaunchIntentForPackage("com.dianshijia.newlive");
            if (openQQintent == null) {
                System.out.println("APP not found!");
                VoiceFeedback("没有找到相关的应用");
                return true;
            }
            pauseMusic();
            SendBytesTo(I2SAppClose);

            VoiceFeedback("正在打开电视家");
            startActivity(openQQintent);


            return true;
        }

        if (action_Cammand.equals("openqqtv")) {
            if (isTopActivity("com.ktcp.tvvideo")) {
                VoiceFeedback("已经在腾讯视频 APP");
                return true;
            }

            PackageManager packageManager = getPackageManager();
            Intent openQQintent = new Intent();
            openQQintent = packageManager.getLaunchIntentForPackage("com.ktcp.tvvideo");
            if (openQQintent == null) {
                System.out.println("APP not found!");
                VoiceFeedback("没有找到相关的应用");
                return true;
            }

            pauseMusic();
            SendBytesTo(I2SAppClose);

            VoiceFeedback("正在打开腾讯视频");
            startMainActivity();
            delayAction("正在打开腾讯视频");
            return true;
        }

        if (action_Cammand.contains("openktv")) {

            if (isTopActivity("com.tencent.karaoketv")) {
                VoiceFeedback("已经在全民K歌APP");
                return true;
            }

            PackageManager packageManager = getPackageManager();
            Intent openQQintent = new Intent();
            openQQintent = packageManager.getLaunchIntentForPackage("com.tencent.karaoketv");

            if (openQQintent == null) {
                System.out.println("APP not found!");
                VoiceFeedback("没有找到相关的应用");
                return true;
            }

            pauseMusic();
            SendBytesTo(I2SAppClose);

            VoiceFeedback("正在打开全民K歌");
            startMainActivity();
            delayAction("正在打开全民K歌");

            return true;
        }

        if (action_Cammand.equals("music") || action_Cammand.equals("ktv")) {
            String singer = intent.getStringExtra("singer");
            String song = intent.getStringExtra("song");
            String category = intent.getStringExtra("category");
            String SKey = null;

            if (singer != null)
                SKey = singer + song;
            else
                SKey = song;

            if (SKey == null) {
                PackageManager packageManager = getPackageManager();
                Intent openQQintent = new Intent();
                openQQintent = packageManager.getLaunchIntentForPackage("com.tencent.karaoketv");
                if (openQQintent == null) {
                    System.out.println("APP not found!");
                    VoiceFeedback("没法发现全民K歌 APP");
                    return true;
                } else {
                    if (isTopActivity("com.tencent.karaoketv")) {
                        VoiceFeedback("已经在全民K歌APP");
                        return true;
                    }
                    VoiceFeedback("正在打开全民K歌 APP");
                    startActivity(openQQintent);
                }
            } else {
                if ((singer.isEmpty()) || (singer == null)) {
                    if (isTopActivity("com.tencent.karaoketv"))
                        VoiceFeedback("正在搜索" + "歌曲" + song);
                    else
                        VoiceFeedback("正在打开全民K歌搜索" + "歌曲" + song);
                } else if ((song.isEmpty()) || (song == null)) {
                    if (isTopActivity("com.tencent.karaoketv"))
                        VoiceFeedback("正在搜索" + singer + "的歌曲");
                    else
                        VoiceFeedback("正在打开全民K歌搜索" + singer + "的歌曲");
                } else {
                    if (isTopActivity("com.tencent.karaoketv"))
                        VoiceFeedback("正在搜索" + singer + "的歌曲" + song);
                    else
                        VoiceFeedback("正在打开全民K歌搜索" + singer + "的歌曲" + song);
                }

                Intent i = new Intent();
                String url = "karaoketv://?action=4&pull_from=" + pull_from + "&m0=1&m1=" + SKey + "&mb=false";
                i.setData(Uri.parse(url));

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.tencent.karaoketv");
                startActivity(i);
                Log.d(TAG, "点歌------>" + url);
            }

            pauseMusic();
            SendBytesTo(I2SAppClose);
            return true;
        }


        if (action_Cammand.contains("PlayControl")) {

            if (isTopActivity("com.tencent.karaoketv")) {

                if (action_music.contains("暂停")) {
                    if (isTopActivity("com.android.music")) {
                        Intent freshIntent = new Intent();
                        freshIntent.setAction("com.android.music.musicservicecommand.pause");
                        freshIntent.putExtra("command", "pause");
                        sendBroadcast(freshIntent);
                    }

                    Intent i = new Intent();
                    String uri = "karaoketv://?action=18&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }

                if (action_music.contains("播放") && !action_music.contains("暂停")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=17&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }

                if (action_music.contains("停止")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=19&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }

                if (action_music.contains("上一首") || action_music.contains("上一")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=20&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }

                if ((action_music.contains("下一首") || action_music.contains("切割") || action_music.contains("切歌")) || action_music.contains("下一")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=21&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }

                if (action_music.contains("打开原唱")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=23&pull_from=" + pull_from + "&m0=1";
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("关闭原唱")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=23&pull_from=" + pull_from + "&m0=0";
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("唱歌")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=24&pull_from=" + pull_from + "&m0=1";
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("听歌")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=24&pull_from=" + pull_from + "&m0=0";
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if ((action_music.contains("重唱") || action_Cammand.contains("再唱一遍"))) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=25&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("上一页")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=26&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("下一页")) {
                    Intent i = new Intent();
                    String uri = "karaoketv://?action=27&pull_from=" + pull_from;
                    i.setData(Uri.parse(uri));
                    sendBroadcast(i);
                    VoiceFeedback(action_music);
                    return true;
                }
            } else if (isTopActivity("com.android.music")) { //|| isTopActivity("com.softwinner.TvdVideo")
                if (action_music.contains("播放") && !action_music.contains("暂停")) {
                    Intent freshIntent = new Intent();
                    freshIntent.setAction("com.android.music.musicservicecommand");
                    freshIntent.putExtra("command", "play");
                    sendBroadcast(freshIntent);
                    //sendKeyEvent(85);
                    //ProperityUtils.setProp("persist.sys.playkeyevent","play");//play,pause,play_pause bob 20190526 add
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("暂停")) {
                    Intent freshIntent = new Intent();
                    freshIntent.setAction("com.android.music.musicservicecommand.pause");
                    freshIntent.putExtra("command", "pause");
                    sendBroadcast(freshIntent);
                    //ProperityUtils.setProp("persist.sys.playkeyevent","pause");//play,pause,play_pause bob 20190526 add
                    //sendKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                    VoiceFeedback(action_music);
                    return true;
                }
                if ((action_music.contains("下一首") || action_music.contains("切割") || action_music.contains("切歌")) || action_music.contains("下一")) {
                    Intent freshIntent = new Intent();
                    freshIntent.setAction("com.android.music.musicservicecommand");
                    freshIntent.putExtra("command", "next");
                    sendBroadcast(freshIntent);
                    //sendKeyEvent(KEYCODE_F5);
                    VoiceFeedback(action_music);
                    return true;
                }
                if (action_music.contains("上一")) {
                    Intent freshIntent = new Intent();
                    freshIntent.setAction("com.android.music.musicservicecommand");
                    freshIntent.putExtra("command", "previous");
                    sendBroadcast(freshIntent);
                    VoiceFeedback(action_music);
                    return true;
                }
            } else {
                VoiceFeedback("当前页面不支持这项功能");
                return true;
            }

            return false;
        }

        if (action_Cammand.equals("全民K歌")) {

            if ((action_music != null) && (action_music.contains("拼音点歌") || action_music.contains("点歌拼音"))) {
                Intent i = new Intent();
                String uri = "karaoketv://?action=4&pull_from=52129&mb=false";
                i.setData(Uri.parse(uri));
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }

            if (!isTopActivity("com.tencent.karaoketv")) {
                return false;
            }

            if (action_music.contains("排行榜")) {
                Intent i = new Intent("com.tencent.karaokTV");
                i.putExtra("pull_from", pull_from);
                i.putExtra("action", 1);
                i.putExtra("mb", false);
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }
            if (action_music.contains("热门歌曲")) {
                Intent i = new Intent("com.tencent.karaokTV");
                i.putExtra("pull_from", pull_from);
                i.putExtra("action", 2);
                i.putExtra("mb", false);
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }
            if ((action_music.contains("消息") || action_music.contains("消息") || action_music.contains("信息"))) {
                return false;
            }
            if ((action_music.contains("中心") || action_music.contains("我的"))) {
                //mFeedback.feedback(action_music, Feedback.SILENCE);
                Intent i = new Intent();
                String uri = "karaoketv://?action=9&pull_from=52129&mb=false";
                i.setData(Uri.parse(uri));
                //Intent i = new Intent("com.tencent.karaokTV");
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }
            if ((action_music.contains("已点"))) {
                Intent i = new Intent();
                String uri = "karaoketv://?action=8&pull_from=52129&mb=false";
                i.setData(Uri.parse(uri));
                //Intent i = new Intent("com.tencent.karaokTV");
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }
            if (action_music.contains("历史") || action_music.contains("最近唱过")) {
                Intent i = new Intent();
                String uri = "karaoketv://?action=7&pull_from=52129&mb=false";
                i.setData(Uri.parse(uri));
                sendBroadcast(i);
                VoiceFeedback(action_music);
                return true;
            }
        }

        if (action_Cammand.equals("JHZSIGNAL0003")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("JHZSIGNAL0002")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("JHZSIGNAL0001")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("JHZSIGNAL0004")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("文件管理")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("清理缓存")) {
            startMainActivity();
            delayAction(action_music);
            return true;
        } else if (action_Cammand.equals("我的应用")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("设置")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("最近使用")) {
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.equals("JHZTPFUN0001")) {
            if (isTopActivity("com.android.music")) {
                VoiceFeedback("当前已经是在USB 播放界面");
                return true;
            }
            pauseMusic();
            startMainActivity();
            delayAction(action_music);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("主页")) {
            Intent i = new Intent();
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName("com.zhuchao.android.tianpu", "com.zhuchao.android.tianpu.activities.MainActivity");
            i.setComponent(cn);

            startActivity(i);
            Intent d = new Intent();
            d.putExtra("_Action", action_Cammand);
            d.setAction("com.zhuchao.android.tianpu.services");
            sendBroadcast(d);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZMICVOL0001")) {
            Message msg = new Message();
            msg.what = 1;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZMICVOL0002")) {
            Message msg = new Message();
            msg.what = 2;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZMUSICVOL0001")) {
            Message msg = new Message();
            msg.what = 3;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if ((action_Cammand.contains("JHZMUSICVOL0002"))) {
            Message msg = new Message();
            msg.what = 4;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if ((action_Cammand.contains("JHZMAINVOL0001"))) {
            Message msg = new Message();
            msg.what = 5;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if ((action_Cammand.contains("JHZMAINVOL0002"))) {
            Message msg = new Message();
            msg.what = 55;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0001")) {
            Message msg = new Message();
            msg.what = 6;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0002")) {
            Message msg = new Message();
            msg.what = 7;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0003")) {
            Message msg = new Message();
            msg.what = 8;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0004")) {
            Message msg = new Message();
            msg.what = 9;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0005")) {
            Message msg = new Message();
            msg.what = 10;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZEFFECT0006")) {
            Message msg = new Message();
            msg.what = 11;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        } else if (action_Cammand.contains("JHZTPFUN0002")) {
            Message msg = new Message();
            msg.what = 12;
            mMyHandler.sendMessage(msg);
            VoiceFeedback(action_music);
            return true;
        }

        return false;
    }

    private boolean isTopActivity(String packageName) {

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            String strt = ForegroundAppUtil.getForegroundActivityName(getApplicationContext());
            if (packageName.equals(strt))
                return true;

        } else {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(5);
            if (tasksInfo.size() > 0) {
                //应用程序位于堆栈的顶层
                String str = tasksInfo.get(0).topActivity.getPackageName();
                if (packageName.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void SendBytesTo(byte[] bytes) {
        Intent intent = new Intent();
        if (bytes != null) {
            //intent.putExtra("serial", bytes);
            Bundle b = new Bundle();
            b.putByteArray("SerialData", bytes);
            intent.putExtras(b);
            intent.setAction("com.iflytek.xiri2.hal.iflytekService");
            sendBroadcast(intent);
        }
    }


    Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int vv = 0;
            if (msg.what == 1) {

                vv = MyService.getMicVolume() + 4;

                if (vv > 60)
                    vv = 60;

                MyService.setMicVolume(vv);

                tbb = TypeTool.intToBytes(vv);

                //SetMicVolume[7] = tbb[3];
                //SetMicVolume[8] = tbb[2];
                //SetMicVolume[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(SetMicVolume, 9);

                SetMicVolumeK50[7] = tbb[3];
                SetMicVolumeK50[8] = tbb[2];
                SetMicVolumeK50[9] = TypeTool.CheckSumBytesAdd(SetMicVolumeK50, 9);

                //myServiceSendBytes(SetMicVolume);
                SendBytesTo(SetMicVolumeK50);

            } else if (msg.what == 2) {
                vv = MyService.getMicVolume() - 4;

                if (vv < 0)
                    vv = 0;

                MyService.setMicVolume(vv);

                tbb = TypeTool.intToBytes(vv);
                //SetMicVolume[7] = tbb[3];
                //SetMicVolume[8] = tbb[2];
                //SetMicVolume[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(SetMicVolume, 9);

                SetMicVolumeK50[7] = tbb[3];
                SetMicVolumeK50[8] = tbb[2];
                SetMicVolumeK50[9] = TypeTool.CheckSumBytesAdd(SetMicVolumeK50, 9);

                //myServiceSendBytes(SetMicVolume);
                SendBytesTo(SetMicVolumeK50);

            } else if (msg.what == 3) {
                vv = MyService.getMusicVolume() + 4;

                if (vv > 60)
                    vv = 60;

                MyService.setMusicVolume(vv);
                tbb = TypeTool.intToBytes(vv);

                //SetMusicVolume[7] = tbb[3];
                //SetMusicVolume[8] = tbb[2];
                //SetMusicVolume[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(SetMusicVolume, 9);

                SetMusicVolumeK50[7] = tbb[3];
                SetMusicVolumeK50[8] = tbb[2];
                SetMusicVolumeK50[9] = TypeTool.CheckSumBytesAdd(SetMusicVolumeK50, 9);

                //myServiceSendBytes(SetMusicVolume);
                SendBytesTo(SetMusicVolumeK50);


            } else if (msg.what == 4) {
                vv = MyService.getMusicVolume() - 4;
                if (vv <= 0)
                    vv = 0;

                MyService.setMusicVolume(vv);
                tbb = TypeTool.intToBytes(vv);

                //SetMusicVolume[7] = tbb[3];
                //SetMusicVolume[8] = tbb[2];
                //SetMusicVolume[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(SetMusicVolume, 9);

                SetMusicVolumeK50[7] = tbb[3];
                SetMusicVolumeK50[8] = tbb[2];
                SetMusicVolumeK50[9] = TypeTool.CheckSumBytesAdd(SetMusicVolumeK50, 9);

                //myServiceSendBytes(SetMusicVolume);
                SendBytesTo(SetMusicVolumeK50);

            } else if (msg.what == 5) {
                //myServiceSendBytes(CommandMuteOn);
                SendBytesTo(CommandMuteOnK50);

            } else if (msg.what == 55) {
                //myServiceSendBytes(CommandMuteClose);
                SendBytesTo(CommandMuteCloseK50);
            } else if (msg.what == 6) {
                //MICEffetive[7] = 1;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 1;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 7) {
                //MICEffetive[7] = 2;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 2;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 8) {
                //MICEffetive[7] = 3;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 3;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 9) {
                //MICEffetive[7] = 4;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 4;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 10) {
                //MICEffetive[7] = 5;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 5;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 11) {
                //MICEffetive[7] = 6;
                //MICEffetive[8] = 0;
                //MICEffetive[9] = com.zhuchao.android.tianpu.utils.TypeTool.CheckSumBytesAdd(MICEffetive, 9);
                MICEffetiveK50[7] = 6;
                MICEffetiveK50[8] = 0;
                MICEffetiveK50[9] = TypeTool.CheckSumBytesAdd(MICEffetiveK50, 9);
                //myServiceSendBytes(MICEffetive);
                SendBytesTo(MICEffetiveK50);
            } else if (msg.what == 12) {
                //myServiceSendBytes(CommandStandby);
                SendBytesTo(CommandStandbyK50);
            } else if (msg.what == 100) {
                pauseMusic();
                //myServiceSendBytes(I2SAppOpen);
                SendBytesTo(I2SAppClose);
                //Intent intent = new Intent();
                //intent.putExtra("_Action", "music");
                //intent.setAction("com.zhuchao.android.tianpu.services");
                //sendBroadcast(intent);
            } else if (msg.what == 1008611) {
                //myServiceSendBytes(Constants.BatteryCharge);
            }
            msg.what = 0;
        }
    };

    private void delayAction(final String cmd) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //要执行的任务
                Intent d = new Intent();
                d.putExtra("_Action", cmd);
                d.setAction("com.zhuchao.android.tianpu.services");
                sendBroadcast(d);
            }
        }, 1200);
    }

    private void startMainActivity() {
        Intent i = new Intent();
        ComponentName cn = new ComponentName("com.zhuchao.android.tianpu", "com.zhuchao.android.tianpu.activities.MainActivity");
        i.setComponent(cn);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


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


    public class MyReceiver extends BroadcastReceiver {
        //adb shell am broadcast -a com.iflytek.xiri2.hal.volume
        //adb shell am broadcast -a com.iflytek.xiri2.hal.volume --es volume 30
        //adb shell am broadcast -a com.zhuchao.android.tianpu.services
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("MyReceiver.xify--->", intent.getAction());
            UpLoadVoiceCommand();

        }
    }

    public synchronized void ReadVoiceCommands() {
        this.mVoiceCommandStr = "";
        try {
            InputStreamReader reader = new InputStreamReader(getResources().getAssets().open("readme.txt"));
            BufferedReader buff = new BufferedReader(reader);
            String line = "";

            while ((line = buff.readLine()) != null)
                this.mVoiceCommandStr += line;
            buff.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpLoadVoiceCommand() {
        new Thread() {     //不可在主线程中调用
            public void run() {
                try {
                    // 上传命令词
                    if (TextUtils.isEmpty(mVoiceCommandStr))
                        ReadVoiceCommands();
                    Log.d(TAG, "MyReceiver.updateGlobal Result:" + mVoiceCommandStr);
                    updateGlobal(iflytekService.this, mVoiceCommandStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void VoiceFeedback(String str) {

        if (str.contains("蓝牙")) {
            mFeedback.feedback(str, Feedback.SILENCE);
            return;
        }
        if (str.contains("模拟")) {
            mFeedback.feedback(str, Feedback.SILENCE);
            return;
        }

        if (str != null)
            mFeedback.feedback(str, Feedback.DIALOG);
        else if (action_music != null)
            mFeedback.feedback(action_music, Feedback.DIALOG);
        else if (action_Cammand != null)
            mFeedback.feedback(action_Cammand, Feedback.DIALOG);
        else
            mFeedback.feedback("关键字错误", Feedback.DIALOG);
    }
}



