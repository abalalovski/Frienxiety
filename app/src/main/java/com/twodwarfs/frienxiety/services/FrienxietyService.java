package com.twodwarfs.frienxiety.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.DialogCallback;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.utils.NotificationsUtils;

/**
 * Created by Aleksandar Balalovski on 6/9/15.
 */
public class FrienxietyService extends Service implements DialogCallback {

    @Override
    public void onCreate() {
        super.onCreate();

        QBlox.instance().setDialogCallback(this);
        QBlox.instance().loginForChat(AppPrefs.getUser(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onMessageReceived(Bundle messageExtras) {
        Intent intent = new Intent(Constants.Actions.MESSAGE_RECEIVED);
        intent.putExtra(Constants.Fields.EXTRAS, messageExtras);

        sendBroadcast(intent);

        NotificationsUtils.showNotification(this, messageExtras,
                getString(R.string.notification_title),
                getString(R.string.notification_content));
    }

    @Override
    public void onMessageSent(Bundle messageExtras) {

    }
}
