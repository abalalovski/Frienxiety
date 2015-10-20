package com.twodwarfs.frienxiety.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.qbox.QBloxHelper;
import com.twodwarfs.frienxiety.utils.Logger;
import com.twodwarfs.frienxiety.utils.NotificationsUtils;

/**
 * Created by Aleksandar Balalovski on 6/9/15.
 */

public abstract class BaseChatActivity extends BaseActivity {

    protected abstract void onStartChat(Bundle messageExtras);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiver, new IntentFilter(Constants.Actions.MESSAGE_RECEIVED));
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.doLog(intent.toString());

            final Context mContext = BaseChatActivity.this;

            String content = getString(R.string.chat_announce_content);
            final Bundle extras = intent.getExtras().getBundle(Constants.Fields.EXTRAS);
            QBUser user = null;
            String message = "";

            if (extras != null) {
                user = (QBUser) extras.getSerializable(Constants.Fields.USER);
                message = extras.getString(Constants.Fields.MESSAGE);

                if (user != null) {
                    content = String.format(getString(R.string.notification_content), user.getLogin());
                }
            }

            final QBUser finalUser = user;
            final String finalMessage = message;
            if (intent.getAction().equals(Constants.Actions.MESSAGE_RECEIVED)) {

                NotificationsUtils.playNotification(mContext, R.raw.start_chat);

                new MaterialDialog.Builder(mContext)
                        .title(R.string.chat_announce_content)
                        .content(content)
                        .positiveText(android.R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                onStartChat(extras);

                                if (finalUser != null) {
                                    Intent chatActivityIntent = new Intent(mContext, ChatActivity.class);
                                    chatActivityIntent.putExtra(Constants.Fields.USER, finalUser);
                                    chatActivityIntent.putExtra(Constants.Fields.MESSAGE, finalMessage);
                                    chatActivityIntent.putExtra(Constants.Fields.IS_RECEIVING, true);
                                    startActivity(chatActivityIntent);
                                }
                            }
                        })
                        .show();

                NotificationsUtils.cancelNotification(BaseChatActivity.this);
            }
        }
    };
}
