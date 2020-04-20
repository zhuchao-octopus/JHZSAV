package com.zhuchao.android.tianpu.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by Oracle on 2017/11/29.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setMaxCacheSize(150 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(100 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(30 * ByteConstants.MB)
//                .setBaseDirectoryName(getCacheDir().getAbsolutePath())
//                .setBaseDirectoryPath(getCacheDir())
//                .setBaseDirectoryName(getExternalCacheDir().getAbsolutePath())
                .setBaseDirectoryPath(getExternalCacheDir())
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .setCacheKeyFactory(cacheKeyFactory)
//                .setDownsampleEnabled(true)
//                .setWebpSupportEnabled(true)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(diskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();
        Fresco.initialize(this,config);
    }

    public static Context ctx() {
        return context;
    }

    public static Resources res() {
        return context.getResources();
    }
}
