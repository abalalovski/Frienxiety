package com.twodwarfs.frienxiety.interfaces;

import com.twodwarfs.frienxiety.qbox.UserHolder;

/**
 * Created by Aleksandar Balalovski
 **/

public interface LoginCallback {
    void onSignUp(UserHolder userHolder, boolean success, String msg);
    void onLogin(UserHolder userHolder, boolean success, String msg);
}
