package com.twodwarfs.frienxiety.qbox;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.QBRoster;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.listeners.QBSubscriptionListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.BuildConfig;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.ChatCallback;
import com.twodwarfs.frienxiety.interfaces.DialogCallback;
import com.twodwarfs.frienxiety.interfaces.LoginCallback;
import com.twodwarfs.frienxiety.interfaces.LogoutCallback;
import com.twodwarfs.frienxiety.utils.Logger;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.List;

/**
 * Created by Aleksandar Balalovski
 **/

public class QBlox {

    private static QBlox mInstance;
    private QBChatService mChatService;
    private XMPPConnection mXmppConnection;
    private QBPrivateChat mPrivateChat;

    private DialogCallback mDialogCallback;

    private QBlox() {
    }

    public static QBlox instance() {
        if (mInstance == null) {
            mInstance = new QBlox();
        }

        return mInstance;
    }

    public void initChatService(Context context) {
        if (!QBChatService.isInitialized()) {
            QBChatService.init(context);
            mChatService = QBChatService.getInstance();
        }
    }

    public QBChatService getChatService() {
        return mChatService;
    }

    /**
     * Used for Login *
     */
    public void createSession(UserHolder user, LoginCallback callback) {
        createSession(true, user, callback);
    }

    /**
     * Used for Registration *
     */
    public void createSession(final boolean hasAccount, final UserHolder userHolder, final LoginCallback callback) {
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                if (hasAccount) {
                    doLogin(userHolder, callback);
                } else {
                    doSignup(userHolder, callback);
                }
            }

            @Override
            public void onError(List<String> errors) {
                printErrors(errors);
            }
        });
    }

    public void doSignup(final UserHolder userHolder,
                         final LoginCallback callback) {
        final QBUser user = new QBUser(userHolder.getNickname(), userHolder.getPassword());
        final StringifyArrayList<String> tags = new StringifyArrayList<>();

        if (userHolder.isHelper()) {
            tags.add(Constants.Fields.HELPER_TAG);
        }

        user.setTags(tags);

        QBUsers.signUp(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                user.setPassword(userHolder.getPassword());
                userHolder.setId(user.getId());

                doLogin(userHolder, callback);

                callback.onSignUp(userHolder, true, "");
            }

            @Override
            public void onError(List<String> errors) {
                printErrors(errors);
                callback.onSignUp(userHolder, false, errors.get(0));
            }
        });
    }

    public void doLogin(final UserHolder userHolder, final LoginCallback callback) {
        QBUser user = new QBUser(userHolder.getNickname(), userHolder.getPassword());

        QBUsers.signIn(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Logger.doLog(user);
                userHolder.setId(user.getId());

                if (callback != null) {
                    callback.onLogin(userHolder, true, "");
                }
            }

            @Override
            public void onError(List<String> errors) {
                printErrors(errors);
                if (callback != null) {
                    callback.onLogin(userHolder, false, errors.get(0));
                }
            }
        });
    }

    public void doLogin(UserHolder userHolder) {
        doLogin(userHolder, null);
    }

    public void doLogout(final LogoutCallback callback) {
        QBUsers.signOut(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
                callback.onSignOut();
            }

            @Override
            public void onError(List errors) {
                Logger.doLog(errors.get(0));
            }
        });
    }

    public void loginForChat(UserHolder userHolder) {
        boolean isLoggedIn = QBlox.instance().getChatService().isLoggedIn();

        if (isLoggedIn) {

        } else {
            QBlox.instance().loginToChat(userHolder, new ChatCallback() {
                @Override
                public void onSuccess() {
                    Logger.doLog("Login for chat successful");
                }

                @Override
                public void onFailure(String error) {
                    Logger.doLog("Login for chat failed. Cause: " + error);
                }
            });
        }
    }

    private void loginToChat(UserHolder userHolder, final ChatCallback callback) {

        final QBUser user = new QBUser(userHolder.getNickname(),
                userHolder.getPassword());

        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                user.setId(session.getUserId());

                if (mChatService.isLoggedIn()) {
                    callback.onSuccess();
                } else {
                    mChatService.login(user, new QBEntityCallbackImpl() {
                        @Override
                        public void onSuccess() {
                            callback.onSuccess();

                            if (mManagerListener != null && mChatService != null) {
                                mChatService.getInstance().getPrivateChatManager().
                                        addPrivateChatManagerListener(mManagerListener);
                            }
                        }

                        @Override
                        public void onError(List errors) {
                            callback.onFailure((String) errors.get(0));
                        }

                    });
                }
            }

            @Override
            public void onError(List<String> errors) {
                printErrors(errors);
            }
        });
    }

    public void logoutForChat(final LogoutCallback callback) {
        if (mChatService != null && mChatService.getInstance().isLoggedIn()) {
            mChatService.getInstance().logout(new QBEntityCallbackImpl() {

                @Override
                public void onSuccess() {
                    Logger.doLog("Logout for chat success");

                    callback.onLogoutForChat();
                    mChatService.getInstance().destroy();
                }

                @Override
                public void onError(List errors) {
                    printErrors(errors);
                }
            });
        }
    }

    public void sendMessage(Bundle message) {
        sendMessage(message, BuildConfig.DEBUG);
    }

    public void sendMessage(Bundle messageExtras, boolean saveToHistory) {

        QBUser poiUser = (QBUser) messageExtras.getSerializable(
                Constants.Fields.USER);
        String message = messageExtras.getString(Constants.Fields.MESSAGE);
        messageExtras.putBoolean(Constants.Fields.IS_RECEIVING, false);

        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(message);
        chatMessage.setProperty(Constants.Fields.SAVE_TO_HISTORY, saveToHistory ? "1" : "0");

        Integer opponentId = poiUser.getId();

        QBPrivateChatManager privateChatManager = QBlox.instance().
                getChatService().getPrivateChatManager();
        mPrivateChat = privateChatManager.getChat(opponentId);

        if (mPrivateChat == null) {
            mPrivateChat = privateChatManager.createChat(opponentId, mMessageListener);
        }

        try {
            mPrivateChat.sendMessage(chatMessage);
            onMessageSent(messageExtras);
        } catch (XMPPException e) {
            Logger.doLogException(e);
        } catch (SmackException.NotConnectedException e) {
            Logger.doLogException(e);
        }
    }

    public boolean isChatInitialized() {
        if (mChatService != null) {
            return mChatService.isInitialized();
        }

        return false;
    }

    private void onMessageSent(Bundle message) {
        if (mDialogCallback != null) {
            mDialogCallback.onMessageSent(message);
        }
    }

    private void onMessageReceived(Bundle message) {
        if (mDialogCallback != null) {
            mDialogCallback.onMessageReceived(message);
        }
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        mDialogCallback = dialogCallback;
    }

    QBMessageListener<QBPrivateChat> mMessageListener = new QBMessageListener<QBPrivateChat>() {
        @Override
        public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
            Logger.doLog(chatMessage.getBody());

            QBUser user = new QBUser(chatMessage.getSenderId());
            String receivedMessage = chatMessage.getBody();

            Bundle messageExtras = new Bundle();
            messageExtras.putSerializable(Constants.Fields.USER, user);
            messageExtras.putString(Constants.Fields.MESSAGE, receivedMessage);
            messageExtras.putBoolean(Constants.Fields.IS_RECEIVING, true);

            if (!TextUtils.isEmpty(receivedMessage)) {
                onMessageReceived(messageExtras);
            }
        }

        @Override
        public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage) {
            // error
        }

        @Override
        public void processMessageDelivered(QBPrivateChat privateChat, String messageID) {

        }

        @Override
        public void processMessageRead(QBPrivateChat privateChat, String messageID) {
        }
    };

    QBPrivateChatManagerListener mManagerListener = new QBPrivateChatManagerListener() {
        @Override
        public void chatCreated(final QBPrivateChat privateChat, final boolean createdLocally) {
            if (!createdLocally) {
                privateChat.addMessageListener(mMessageListener);
            }
        }
    };

    public void printErrors(List<String> errors) {
        if (errors != null) {
            for (String err : errors) {
                Logger.doLog("Error: " + err);
            }
        }
    }

    public void init(Context context) {
        QBSettings.getInstance().fastConfigInit(Constants.QBlox.QB_APP_ID,
                Constants.QBlox.QB_APP_KEY, Constants.QBlox.QB_APP_SCRET);
        initChatService(context);

    }
}
