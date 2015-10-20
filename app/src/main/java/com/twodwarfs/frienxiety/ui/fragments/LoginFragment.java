package com.twodwarfs.frienxiety.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.LoginCallback;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.qbox.UserHolder;
import com.twodwarfs.frienxiety.ui.activities.MainActivity;
import com.twodwarfs.frienxiety.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class LoginFragment extends BaseFragment {

    @InjectView(R.id.editText_nickname)
    EditText mNicknameEditText;

    @InjectView(R.id.editText_password)
    EditText mEditTextPassword;

    LoginCallback mLoginCallback = new LoginCallback() {
        @Override
        public void onSignUp(UserHolder userHolder,
                             boolean success, String msg) {
        }

        @Override
        public void onLogin(UserHolder user, boolean success, String msg) {
            String resultToast = success ? getString(R.string.login_success)
                    : getString(R.string.login_fail);
            resultToast = TextUtils.isEmpty(msg) ? resultToast : (resultToast += ": " + msg);

            Utils.doToast(getActivity(), resultToast);

            if (success) {
                clearFields();
                AppPrefs.saveUser(getActivity(), user);
                AppPrefs.setLoggedIn(getActivity(), true);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();

            }

            hideProgressDialog();
        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v.getId() == R.id.editText_nickname) {
                    mEditTextPassword.requestFocus();
                }

                return true;
            }

            if(actionId==EditorInfo.IME_ACTION_DONE) {
                if (v.getId() == R.id.editText_password) {
                    onLogin();
                }
            }

            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, rootView);

        mNicknameEditText.setOnEditorActionListener(mOnEditorActionListener);
        mEditTextPassword.setOnEditorActionListener(mOnEditorActionListener);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick(R.id.button_login)
    public void onLogin() {
        showProgressDialog("", getString(R.string.logging_in));

        UserHolder user = new UserHolder();
        user.setNickname(mNicknameEditText.getText().toString());
        user.setPassword(mEditTextPassword.getText().toString());
        QBlox.instance().createSession(user, mLoginCallback);
    }

    private void clearFields() {
        mNicknameEditText.getText().clear();
        mEditTextPassword.getText().clear();
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getName() {
        return Constants.Fragments.LOGIN;
    }

    public static BaseFragment newInstance() {
        return new LoginFragment();
    }
}
