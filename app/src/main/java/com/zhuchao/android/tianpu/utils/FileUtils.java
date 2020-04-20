package com.zhuchao.android.tianpu.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by hl on 2018/6/30.
 */
public class FileUtils {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator + "socketDemo/";


    /**
     * 根据文件路径获取文件名称
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 生成本地文件路径
     * @param filePath
     * @return
     */
    public static File gerateLocalFile(String filePath) {
        String fileNmae = getFileName(filePath);
        File dirFile = new File(ROOT_PATH);
        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return new File(dirFile, fileNmae);
    }

    /**
     * 转换文件大小
     *
     * @param fileSize
     * @return
     */
    public static String FormetFileSize(long fileSize) {
        if(fileSize <= 0) {
            return "0KB";
        }

        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            size = fis.available();
        } else {
            f.createNewFile();
        }
        return size;
    }

    //找到所有音乐（实则为音乐所在地址） 并存入集合中
    public static String getMusics(String path) {
        //获得外部存储的根目录
        File dir = new File(Environment.getExternalStorageDirectory() + path);
        String musics = recursionFile(dir);
        //调用遍历所有文件的方法
        //返回文件路径集合
//        Log.e("TAG","dir="+dir+"   mu="+musics);
        return musics;
    }
    //遍历手机所有文件 并将路径名存入集合中 参数需要 路径和集合
    public static String recursionFile(File dir) {
        //得到某个文件夹下所有的文件
        File[] files = dir.listFiles();
        String musics = "";
        //文件为空
        if (files == null) {
            return null;
        }
        //遍历当前文件下的所有文件
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                //则递归(方法自己调用自己)继续遍历该文件夹
                recursionFile(file);
            } else { //如果不是文件夹 则是文件
                //如果文件名以 .mp3结尾则是mp3文件
//                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wma") || file.getName().endsWith(".flac")
//                        || file.getName().endsWith(".aac") || file.getName().endsWith(".mmf") || file.getName().endsWith(".amr")
//                        || file.getName().endsWith(".m4a") || file.getName().endsWith(".wav") || file.getName().endsWith(".wv")
//                        || file.getName().endsWith(".ape")) {
                    //往音乐集合中 添加音乐的路径
                    musics=file.getAbsolutePath();
//                    Log.e("tag==","file="+file+"    musics= "+musics);
//                }
            }
        }
        return musics;
    }


    /**
     * 扫描根目录所有文件夹，当扫描到该文件夹里有音乐文件时，添加为HashMap的key，音乐文件的集合为Value
     * @return   HashMap<文件夹,音乐文件的集合>
     */

    //找到所有音乐（实则为音乐所在地址） 并存入集合中
    public static String getMusicss() {
        //获得外部存储的根目录
        File dir = new File(Environment.getExternalStorageDirectory()+"");
//        Log.e("Tag","dir="+dir);
        //一个文件夹里的音乐文件
        String musics = "";

        //调用遍历所有文件的方法
        recursionFiles(dir,musics);
        //返回文件路径集合
//        Log.e("Tag","music="+musics);
        return musics;
    }

    //遍历手机所有文件 并将路径名存入集合中 参数需要 路径和集合
    public static void recursionFiles(File dir, String musics) {
        //得到某个文件夹下所有的文件
        File[] files = dir.listFiles();
        //文件为空
        if (files == null) {
            return;
        }
        //遍历当前文件下的所有文件
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                //则递归(方法自己调用自己)继续遍历该文件夹
                recursionFiles(file,musics);
            } else { //如果不是文件夹 则是文件
                //如果文件名以 .mp3结尾则是mp3文件
//                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wma") || file.getName().endsWith(".flac")
//                        || file.getName().endsWith(".aac") || file.getName().endsWith(".mmf") || file.getName().endsWith(".amr")
//                        || file.getName().endsWith(".m4a") || file.getName().endsWith(".wav") || file.getName().endsWith(".wv")
//                        || file.getName().endsWith(".ape")) {
                    //往音乐集合中 添加音乐的路径
                    musics=file.getAbsolutePath();
//                }
            }
        }
    }
}
