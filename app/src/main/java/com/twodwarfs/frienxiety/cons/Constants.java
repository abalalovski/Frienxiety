package com.twodwarfs.frienxiety.cons;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class Constants {

    public static final String TAG = "Frienxiety";
    public static final long SPLASH_DELAY = 2000;

    public static class QBlox {
       
    }

    public static class Prefs {
        public static final String PREFS_NAME = "frienxietyPrefs";
        public static final String IS_LOGGED_IN = "isLoggedIn";
        public static final String NICKNAME = "nickname";
        public static final String PASSWORD = "password";
        public static final String IS_HELPER = "isHelper";
        public static final String UTF8 = "utf-8";

        public static final String USER_ID = "userId";
        public static final String IS_DISCLAIMER_ACCEPTED = "disclaimerAccepted";
        public static final String USER_STATUS = "userStatus";
    }

    public static class Fields {
        public static final String USER = "user";
        public static final String MESSAGE = "message";
        public static final String EXTRAS = "extras";
        public static final String IS_RECEIVING = "is_receiving";
        public static final String HELPER_TAG = "helper";
        public static final String RELAX_METOD = "relax_method";
        public static final String COLOR_INDEX = "color";
        public static final String SAVE_TO_HISTORY = "save_to_history";
    }

    public static class Actions {

        public static final String MESSAGE_RECEIVED = "action.message.received";
    }

    private Constants() {
    }

    public class Fragments {
        public static final String LOGIN = "loginFragment";
        public static final String MAIN = "mainFragment";
        public static final String RELAXATION = "chat";
        public static final String START_SCREEN = "startScreen";
        public static final String CHAT_DIALOG = "chatDialog";
        public static final String COLOR_FRAGMENT = "colorFragment";
    }
}
