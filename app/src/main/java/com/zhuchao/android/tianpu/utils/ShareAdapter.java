package com.zhuchao.android.tianpu.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ShareAdapter {

	private final String TAG = getClass().getSimpleName();

	private static ShareAdapter shareAdapter;
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;

	private ShareAdapter() {}

	public static ShareAdapter getInstance() {
		if (shareAdapter == null) {
			shareAdapter = new ShareAdapter();
			preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.ctx());
			editor = preferences.edit();
		}
		return shareAdapter;
	}

	public void saveStr(String key, String val) {

		if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(val)) {
			editor.remove(key);
			editor.putString(key, val);
			editor.apply();
		}
	}

	public String getStr(String key) {

		if(!TextUtils.isEmpty(key)) {
			return preferences.getString(key, null);
		}

		return null;
	}

	public void saveInt(String key, int val) {

		if(!TextUtils.isEmpty(key)) {
			editor.remove(key);
			editor.putInt(key, val);
			editor.apply();
		}
	}

	public int getInt(String key) {

		if(!TextUtils.isEmpty(key)) {
			return preferences.getInt(key, -1);
		}

		return -1;
	}

	public void saveLong(String key, long val) {

		if(!TextUtils.isEmpty(key)) {
			editor.remove(key);
			editor.putLong(key, val);
			editor.apply();
		}
	}

	public long getLong(String key) {

		if(!TextUtils.isEmpty(key)) {
			return preferences.getLong(key, -1);
		}

		return -1;
	}



	public boolean saveObj(String key, Object object) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			String obj2Str = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			editor.remove(key);
			editor.putString(key, obj2Str);
			editor.apply();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Save Fav list err " + e);
			return false;
		}
		return true;
	}

	public Object getObj(String key) {
		Object object = null;

		try {

			String objStr = preferences.getString(key, "");
			byte[] objBytes = Base64.decode(objStr, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
			ObjectInputStream bis = new ObjectInputStream(bais);
			object = bis.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return object;
	}

	public boolean keyExists(String key) {

		if(!TextUtils.isEmpty(key)) {
			return preferences.contains(key);
		}
		return false;
	}

	public void remove(String key) {

		if(!TextUtils.isEmpty(key)) {
			editor.remove(key);
			editor.apply();
		}
	}

	public void destroy() {

		editor = null;
		preferences = null;
	}
}
