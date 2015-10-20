package com.twodwarfs.frienxiety.utils;

import android.text.TextUtils;
import android.util.Log;

import com.twodwarfs.frienxiety.BuildConfig;
import com.twodwarfs.frienxiety.cons.Constants;

/**
 * Created by Aleksandar Balalovski
 */

public class Logger {

    public static void doLog(Object text) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(text.toString())) {
            Log.i(Constants.TAG, text.toString());
        }
    }

    public static void doLogException(Exception e) {
        if (BuildConfig.DEBUG && e != null) {
            Log.e(Constants.TAG, "Exception", e);
        }
    }

}
