package com.twodwarfs.frienxiety.interfaces;

import android.os.Bundle;

import com.quickblox.users.model.QBUser;

/**
 * Created by Aleksandar Balalovski
 **/

public interface DialogCallback {
    void onMessageReceived(Bundle message);

    void onMessageSent(Bundle message);
}
