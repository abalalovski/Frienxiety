package com.twodwarfs.frienxiety.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PrefsManager {

    protected static void setStringValue(Context context, String key,
                                         String value) {
        Logger.doLog(key + ": " + String.valueOf(value));
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    protected static String getStringValue(Context context, String key,
                                           String defValue) {
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        return settings.getString(key, defValue);
    }

    protected static void setIntegerValue(Context context, String key, int value) {
        Logger.doLog(key + ": " + String.valueOf(value));
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(key, value);
        prefEditor.apply();
    }

    protected static int getIntegerValue(Context context, String key,
                                         int defValue) {
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        return settings.getInt(key, defValue);
    }

    protected static void setBooleanValue(Context context, String key,
                                          boolean value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    protected static boolean getBooleanValue(Context context, String key,
                                             boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        return settings.getBoolean(key, defValue);
    }


    public static void removeKey(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.Prefs.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.remove(key);
        prefEditor.apply();
    }

    public static String encrypt(Context context, String value)
            throws GeneralSecurityException, UnsupportedEncodingException {
        final byte[] bytes = value != null ? value.getBytes(Constants.Prefs.UTF8) : new byte[0];
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(Constants.Prefs.SACRED));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        pbeCipher.init(
                Cipher.ENCRYPT_MODE,
                key,
                new PBEParameterSpec(Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID).getBytes(Constants.Prefs.UTF8), 20)
        );

        return new String(Base64.encode(pbeCipher.doFinal(bytes),
                Base64.NO_WRAP), Constants.Prefs.UTF8);
    }

    public static String decrypt(Context context, String value)
            throws GeneralSecurityException, UnsupportedEncodingException {
        final byte[] bytes = value != null ? Base64.decode(value,
                Base64.DEFAULT) : new byte[0];
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(Constants.Prefs.SACRED));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        pbeCipher.init(
                Cipher.DECRYPT_MODE,
                key,
                new PBEParameterSpec(Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID).getBytes(Constants.Prefs.UTF8), 20)
        );

        return new String(pbeCipher.doFinal(bytes), Constants.Prefs.UTF8);
    }
}