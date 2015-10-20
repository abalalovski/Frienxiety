package com.twodwarfs.frienxiety.qbox;

import android.content.Context;

import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.prefs.AppPrefs;

/**
 * @author Aleksandar Balalovski
 */

public class QBloxHelper {

    public static boolean isUserOnline(QBUser user) {
        long currentTime = System.currentTimeMillis();
        long userLastRequestAtTime = user.getLastRequestAt().getTime();

        return (currentTime - userLastRequestAtTime) < 5 * 60 * 1000;
    }

    public static boolean isLocalUser(Context context, QBUser user) {
        int userId = AppPrefs.getUserId(context);
        return !user.getId().equals(userId);
    }

    public static void setUserOccupied(QBUser awayUser, boolean isAway) {
        //todo set user "do not disturb"
    }

    public static boolean amIHelper(Context context) {
        return AppPrefs.isHelper(context);
    }
}
