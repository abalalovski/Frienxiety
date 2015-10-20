package com.twodwarfs.frienxiety.qbox;

import com.quickblox.users.model.QBUser;

public class UserHolder {

    private int mId;
    private String mNickname;
    private String mPassword;
    private boolean mIsHelper;
    private String mStatus;

    public int getId() {
        return mId;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getPassword() {
        return mPassword;
    }

    public boolean isHelper() {
        return mIsHelper;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setId(int userId) {
        mId = userId;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setIsHelper(boolean isHelper) {
        mIsHelper = isHelper;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public QBUser toQBUser() {
        QBUser user = new QBUser();
        user.setId(mId);
        user.setLogin(mNickname);
//        user.setPassword(mPassword);
        return user;
    }
}