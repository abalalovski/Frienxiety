package com.twodwarfs.frienxiety.prefs;

import android.app.Activity;
import android.content.Context;

import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.qbox.UserHolder;

/**
 * Created by Aleksandar Balalovski
 **/

public class AppPrefs extends PrefsManager {

    public static void setLoggedIn(Context context, boolean loggedIn) {
        setBooleanValue(context, Constants.Prefs.IS_LOGGED_IN, loggedIn);
    }

    public static boolean isLoggedIn(Context context) {
        return getBooleanValue(context, Constants.Prefs.IS_LOGGED_IN, false);
    }

    public static void setNickname(Context context, String fullname) {
        setStringValue(context, Constants.Prefs.NICKNAME, fullname);
    }

    public static String getNickname(Context context) {
        return getStringValue(context, Constants.Prefs.NICKNAME, "");
    }

    public static void setPassword(Context context, String password) {
        setStringValue(context, Constants.Prefs.PASSWORD, password);
    }

    public static String getPassword(Context context) {
        return getStringValue(context, Constants.Prefs.PASSWORD, "");
    }

    public static void setUserId(Context context, int id) {
        setIntegerValue(context, Constants.Prefs.USER_ID, id);
    }

    public static int getUserId(Context context) {
        return getIntegerValue(context, Constants.Prefs.USER_ID, -1);
    }

    public static void setIsHelper(Context context, boolean isHelper) {
        setBooleanValue(context, Constants.Prefs.IS_HELPER, isHelper);
    }

    public static boolean isHelper(Context context) {
        return getBooleanValue(context, Constants.Prefs.IS_HELPER, false);
    }

    public static void saveUser(Context context, UserHolder holder) {
        setLoggedIn(context, true);
        setUserId(context, holder.getId());
        setNickname(context, holder.getNickname());
        setPassword(context, holder.getPassword());
        setIsHelper(context, holder.isHelper());
    }

    public static UserHolder getUser(Context context) {
        UserHolder holder = new UserHolder();
        holder.setId(getUserId(context));
        holder.setNickname(getNickname(context));
        holder.setPassword(getPassword(context));
        holder.setIsHelper(isHelper(context));

        return holder;
    }

    public static void setUserStatus(Context context, String status) {
        setStringValue(context, Constants.Prefs.USER_STATUS, status);
    }

    public static String getUserStatus(Context context) {
        return getStringValue(context, Constants.Prefs.USER_STATUS, "Hello, I am here to help you!");
    }

    public static boolean isChatDisclaimerAccepted(Context context) {
        return getBooleanValue(context, Constants.Prefs.IS_DISCLAIMER_ACCEPTED, false);
    }

    public static void setChatDisclaimerAccepted(Context context, boolean accepted) {
        setBooleanValue(context, Constants.Prefs.IS_DISCLAIMER_ACCEPTED, accepted);
    }

    public static void clearCredentials(Context context) {
        removeKey(context, Constants.Prefs.IS_LOGGED_IN);
        removeKey(context, Constants.Prefs.NICKNAME);
        removeKey(context, Constants.Prefs.PASSWORD);
        removeKey(context, Constants.Prefs.USER_ID);
    }
}
