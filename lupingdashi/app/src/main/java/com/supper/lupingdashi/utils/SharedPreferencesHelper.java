package com.supper.lupingdashi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

    public static final String SHARED_PREFERENCES_NAME = "lpdsSharedPreferences";

    public static final SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        return pref.getBoolean(key, defValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        return pref.getString(key, defValue);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        return pref.getLong(key, defValue);
    }

    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        return pref.getInt(key, defValue);
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences pref = getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

}
