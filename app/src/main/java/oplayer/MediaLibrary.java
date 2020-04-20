/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package oplayer;

import android.content.Context;
import android.util.Log;

import com.zhuchao.android.playsession.OPlayerSession;
import com.zhuchao.android.playsession.OPlayerSessionManager;
import com.zhuchao.android.playsession.SchedulePlaybackSession;
import com.zhuchao.android.video.OMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MediaLibrary { // implements SessionCompleteCallback
    private static final String TAG = "MediaLibrary ---------->";
    private static long count = 0;
    private static String MobileBlockName = null;
    private static String MobileBlockPath = null;
    public static SchedulePlaybackSession schedulePlaybackSession = null;

    //获得OPlayerSessionManager，也就是媒体库的入口
    private static OPlayerSessionManager mSessionManager = null;

    //mSessions这里面保存的是媒体库的分类对象信息，例如：直播、电影、USB、FTP、
    //注意这里存储的是对象OPlayerSession
    private static Map<Integer, OPlayerSession> mSessions = null;// new HashMap<Integer, OPlayerSession>();

    //mVideoCategory 保存是分类的名字，每个分类都有一个名字和ID 例如：直播、电影、电视、本地音乐、USB音乐、图片
    //这里存储的是对象的名字和ID
    public static Map<Integer, String> mVideoCategory = null;//new HashMap<Integer, String>();
    //mVideoTypes 这个里面存储媒体库中媒体的类型名字例如：恐怖、戏剧、动作、科幻 等等
    public static Map<Integer, String> mVideoTypes = null;//new HashMap<Integer, String>();


    //下面两个数组是适配用的，可以根据自己项目选择是否需要是使用它
    public static ArrayList MOVIE_CATEGORY = new ArrayList();//存储对象的名字
    public static ArrayList MOVIE_Types = new ArrayList();//存储媒体库中媒体的类型名字例如：恐怖、戏剧、动作、科幻


    public static synchronized OPlayerSessionManager getSessionManager(Context context) {//通过上下文得到实例，用实例去调用非静态方法
        if (mSessionManager == null)
            mSessionManager = new OPlayerSessionManager(context, null, null);
        return mSessionManager;
        //return mSessionManager == null ? (new OPlayerSessionManager(context,null, null)) : mSessionManager;
    }

    //释放OPlayerSessionManager
    public static void ClearOPlayerSessionManager() {
        if (mSessionManager != null)
           mSessionManager.free();
        mSessionManager = null;
    }


    //下面这个方法是初始化媒体库到本地UI 的方法，必须在OPlayerSessionManager 对象创建完成后，
    //在OPlayerSessionManager的回调方法中使用该方法来初始化本地UI，否则无法获得媒体资源
    public static void setupCategoryList() {
        //if (mVideoCategory == null)
        mVideoCategory = mSessionManager.getmTopSession().getmVideoCategoryNameList();//获取大的分类列表
        //if (mVideoTypes == null)
        mVideoTypes = mSessionManager.getmTopSession().getmVideoTypeNameList();//获取视频类型信息

        InitVideoArrayList();//将mVideoCategory Map 转换成 ArrayList 根据项目可以不需要
    }

    //在OPlayerSessionManager创建和初始化完成后，调用该方法，打印输出会话里面的媒体ID,名字
    //通过这个方法知道都有哪些媒体会话资源，知道它们的ID,方便后续根据根据ID 获得自己需要的媒体资源
    public static void printCategoryList() {
        for (Map.Entry<Integer, OPlayerSession> entry : mSessions.entrySet()) {
            Log.d(TAG, "ID=" + entry.getKey() + " CategoryName=" + entry.getValue());
        }
    }

    //该方法可以获得具体的媒体列表，可以播放的媒体资源列表，例如、在线电影、电视、音乐、USB 音乐
    //通过调用这个方法获得列表 创建本地 UI界面
    //参数是MOVIE_CATEGORY 的ID,这个ID 是固定的，由OPlayerSessionManager 分配
    //返回值是videos列表，每个video对象可以是视频、音乐、图片
    //通过video.with(this).playInto(mSurfaceView); 这样的调用形式可以将这个媒体播放出来
    public static List<OMedia> getMediaListByID(int categoryID) {
        List<OMedia> videos = null;
        if (mSessionManager.isInitComplete() && (categoryID > 0)) {
            mSessions = mSessionManager.getmSessions();//获取板块分类集合
            videos = mSessions.get(categoryID).getVideos();//从集合中得到直播视频列表
        }
        return videos;
    }

    //该方法可以获得具体的媒体列表，可以播放的媒体资源列表，例如、在线电影、电视、音乐、USB 音乐
    //通过调用这个函数获得列表 创建本地 UI界面
    //参数是MOVIE_CATEGORY 的Index 不是CATEGORY ID 如果不用MOVIE_CATEGORY数组来适配，则该方法无效
    //
    public static List<OMedia> getMediaListByIndex(int categoryIndex) {
        List<OMedia> videos = null;
        String categoryName = MOVIE_CATEGORY.get(categoryIndex).toString();
        int categoryId = getCategoryIdByValue(categoryName);


        mSessions = mSessionManager.getmSessions();//获取板块分类集合
        if (mSessions.get(categoryId) != null)
            videos = mSessions.get(categoryId).getVideos();//从集合中得到直播视频列表

        return videos;
    }

    //当U盘插上的时候调用该方法，该方法会得到U盘中的所有媒体资源包括图片、音乐、视频
    public static void updateMobileMedia(String DeviceName, String DevicePath) {
        if (mSessionManager == null) return;
        mSessionManager.initSessionFromMobileDisc();

        if (schedulePlaybackSession != null)
            schedulePlaybackSession.updateSchedulePlaybackSession();
    }

    //该方法判断mSessionManager 是否完成了媒体库的创建和初始化
    public static boolean isSessionManagerInitComplete() {
        if (mSessionManager == null) return false;
        return mSessionManager.isInitComplete();
    }

    //将mVideoCategory Map 转换成 ArrayList 根据项目可以不需要
    private static void InitVideoArrayList() {
        MOVIE_CATEGORY.clear();
        mSessions = mSessionManager.getmSessions();
        for (Map.Entry<Integer, OPlayerSession> entry : mSessions.entrySet()) {
            String name = entry.getValue().getmVideoCategoryNameList().get(entry.getKey());
            MOVIE_CATEGORY.add(name);
        }
        MOVIE_Types.clear();

        for (Map.Entry<Integer, String> entry : mVideoTypes.entrySet()) {
            MOVIE_Types.add(entry.getValue());
        }
    }

    private static int getCategoryIdByValue(String value) {
        for (Map.Entry entry : mVideoCategory.entrySet()) {
            if (value.equals(entry.getValue()))
                return (int) entry.getKey();
        }
        return -10000;
    }

    //该方法为OPlayerSessionManager 的回调方法，当OPlayerSessionManager 完成了一个任务后，
    // 或者媒体库有更新会回调该方法
    //SessionCompleteCallback 该接口也可以放到具体的ACTIVITY 中去实现，方便更新UI
/*    @Override
    public void OnSessionComplete(int i, String s) {
        Message msg = new Message();
        msg.what = i;
        mMyHandler.sendMessage(msg);
    }

    Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (isSessionManagerInitComplete())
                    setupCategoryList();//例如在里建立媒体库资源，和处理其它事情
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/
}