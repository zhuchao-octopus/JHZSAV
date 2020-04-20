package com.zhuchao.android.tianpu.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;


public class ProperityUtils {

    private static final String TAG = "ProperityUtils";

    public static String setProp(String key, String defaultValue) {
        try {
            final Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            final Method get = systemProperties.getMethod("set", String.class, String.class);
            return (String) get.invoke(null, key, defaultValue);
        } catch (Exception e) {
            // This should never happen
            Log.e(TAG, "Exception while getting system property: ", e);
            return defaultValue;
        }
    }


    public static String getProp(String propName) {
        Class<?> classType = null;
        String buildVersion = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{propName});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildVersion;
    }


    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }


}
