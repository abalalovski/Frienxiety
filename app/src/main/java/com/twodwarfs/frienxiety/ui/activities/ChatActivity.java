package com.twodwarfs.frienxiety.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.LoginCallback;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.qbox.QBloxHelper;
import com.twodwarfs.frienxiety.qbox.UserHolder;
import com.twodwarfs.frienxiety.services.FrienxietyService;
import com.twodwarfs.frienxiety.ui.fragments.ChatFragment;
import com.twodwarfs.frienxiety.utils.NotificationsUtils;
import com.twodwarfs.frienxiety.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends BaseChatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolbar();

        setActionBarTitle(getString(R.string.please_wait));

        Bundle extras = getIntent().getExtras();
        boolean isReceiving = false;
        if (extras != null) {
            isReceiving = extras.getBoolean(Constants.Fields.IS_RECEIVING);
        }

        if (isReceiving) {
            showFragment(R.id.container_chat_fragments,
                    ChatFragment.newInstance(extras), false, true);

            UserHolder holder = AppPrefs.getUser(this);
            if (holder.toQBUser() != null) {
                QBloxHelper.setUserOccupied(holder.toQBUser(), true);
            }
        } else {
            findAvailableUserAndStartChat();
        }

        NotificationsUtils.cancelNotification(this);
    }

    private void findAvailableUserAndStartChat() {
        showProgressDialog("", getString(R.string.please_wait));
        QBlox.instance().createSession(AppPrefs.getUser(this), new LoginCallback() {
            @Override
            public void onSignUp(UserHolder userHolder, boolean success, String msg) {
            }

            @Override
            public void onLogin(UserHolder userHolder, boolean success, String msg) {

                if (success) {
                    QBlox.instance().loginForChat(userHolder);

                    QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
                    pagedRequestBuilder.setPage(1);
                    pagedRequestBuilder.setPerPage(100);

                    QBUsers.getUsers(pagedRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                        @Override
                        public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                            hideProgressDialog();

                            if (users.size() > 0) {
                                ArrayList<QBUser> filteredUsers = filteredUsers(users);

                                if (filteredUsers != null && filteredUsers.size() > 0) {
                                    Collections.shuffle(filteredUsers);

                                    Bundle extras = new Bundle();
                                    extras.putSerializable(Constants.Fields.USER, filteredUsers.get(0));
                                    extras.putString(Constants.Fields.MESSAGE, Utils.getRandomHelloMessage(ChatActivity.this));
                                    extras.putBoolean(Constants.Fields.IS_RECEIVING, false);

                                    showFragment(R.id.container_chat_fragments,
                                            ChatFragment.newInstance(extras), false, true);
                                } else {
                                    Utils.doToast(ChatActivity.this, R.string.no_available_users);
                                    finish();
                                }
                            } else {
                                Utils.doToast(ChatActivity.this, R.string.no_available_users);
                                finish();
                            }
                        }

                        @Override
                        public void onError(List<String> errors) {
                            QBlox.instance().printErrors(errors);
                        }
                    });
                }
            }
        });
    }

    private ArrayList<QBUser> filteredUsers(ArrayList<QBUser> users) {
        ArrayList<QBUser> filteredUsers = new ArrayList<>();

        for (QBUser user : users) {
            StringifyArrayList tags = user.getTags();
            boolean isHelper = tags.contains(Constants.Fields.HELPER_TAG);
            boolean notThisUser = QBloxHelper.isLocalUser(this, user);
            boolean notAdmin = !user.getLogin().equals(Constants.QBlox.ADMIN_LOGIN);
            boolean isOnline = QBloxHelper.isUserOnline(user);

            if (notThisUser && isHelper && notAdmin /*&& isOnline*/) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(this, FrienxietyService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStartChat(Bundle messageExtras) {

    }

}
