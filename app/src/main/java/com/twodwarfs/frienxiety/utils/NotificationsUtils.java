package com.twodwarfs.frienxiety.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.ui.activities.ChatActivity;

/**
 * Created by Aleksandar Balalovski
 */

public class NotificationsUtils {

    public static void showNotification(Context context, Bundle messageExtras, String title, String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setTicker(title)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(content);

        QBUser user = (QBUser) messageExtras.getSerializable(Constants.Fields.USER);
        String message = messageExtras.getString(Constants.Fields.MESSAGE);

        Intent resultIntent = new Intent(context, ChatActivity.class);
        resultIntent.putExtra(Constants.Fields.USER, user);
        resultIntent.putExtra(Constants.Fields.MESSAGE, message);
        resultIntent.putExtra(Constants.Fields.IS_RECEIVING, true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void cancelNotification(Context context) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager manager = (NotificationManager) context.getSystemService(ns);
        manager.cancel(1);
    }

    public static void playNotification(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playNotification(Context context, int rawResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, rawResId);
        mediaPlayer.start();
    }
}
